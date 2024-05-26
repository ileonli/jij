package io.github.ileonli.jij.classfile;

import io.github.ileonli.jij.classfile.cp.*;
import io.github.ileonli.jij.classfile.exception.InvalidIndexException;
import io.github.ileonli.jij.classfile.exception.UnexpectedEntryException;

import java.io.IOException;
import java.util.Objects;
import java.util.function.BiConsumer;

public class ConstantPool {

    public static final int CONSTANT_Utf8 = 1;
    public static final int CONSTANT_Integer = 3;
    public static final int CONSTANT_Float = 4;
    public static final int CONSTANT_Long = 5;
    public static final int CONSTANT_Double = 6;
    public static final int CONSTANT_Class = 7;
    public static final int CONSTANT_String = 8;
    public static final int CONSTANT_Fieldref = 9;
    public static final int CONSTANT_Methodref = 10;
    public static final int CONSTANT_InterfaceMethodref = 11;
    public static final int CONSTANT_NameAndType = 12;
    public static final int CONSTANT_MethodHandle = 15;
    public static final int CONSTANT_MethodType = 16;
    public static final int CONSTANT_Dynamic = 17;
    public static final int CONSTANT_InvokeDynamic = 18;
    public static final int CONSTANT_Module = 19;
    public static final int CONSTANT_Package = 20;

    public enum RefKind {
        REF_getField(1),
        REF_getStatic(2),
        REF_putField(3),
        REF_putStatic(4),
        REF_invokeVirtual(5),
        REF_invokeStatic(6),
        REF_invokeSpecial(7),
        REF_newInvokeSpecial(8),
        REF_invokeInterface(9);

        public final int kind;

        RefKind(int kind) {
            this.kind = kind;
        }

        public static RefKind valueFrom(int kind) {
            return switch (kind) {
                case 1 -> REF_getField;
                case 2 -> REF_getStatic;
                case 3 -> REF_putField;
                case 4 -> REF_putStatic;
                case 5 -> REF_invokeVirtual;
                case 6 -> REF_invokeStatic;
                case 7 -> REF_invokeSpecial;
                case 8 -> REF_newInvokeSpecial;
                case 9 -> REF_invokeInterface;
                default -> throw new IllegalArgumentException("Unknown kind: " + kind);
            };
        }
    }

    private final ConstantPoolInfo[] pool;

    private final ClassFile cf;

    public ConstantPool(ClassReader cr) throws IOException {
        cf = cr.classFile;

        int count = cr.readUnsignedShort();
        pool = new ConstantPoolInfo[count];

        for (int i = 1; i < count; i++) {
            int tag = cr.readUnsignedByte();
            switch (tag) {
                case CONSTANT_Utf8 -> pool[i] = new ConstantUtf8Info(cr);
                case CONSTANT_Integer -> pool[i] = new ConstantIntegerInfo(cr);
                case CONSTANT_Float -> pool[i] = new ConstantFloatInfo(cr);
                case CONSTANT_Long -> {
                    pool[i] = new ConstantLongInfo(cr);
                    i++;
                }
                case CONSTANT_Double -> {
                    pool[i] = new ConstantDoubleInfo(cr);
                    i++;
                }
                case CONSTANT_Class -> pool[i] = new ConstantClassInfo(this, cr);
                case CONSTANT_String -> pool[i] = new ConstantStringInfo(this, cr);
                case CONSTANT_Fieldref -> pool[i] = new ConstantFieldrefInfo(this, cr);
                case CONSTANT_Methodref -> pool[i] = new ConstantMethodrefInfo(this, cr);
                case CONSTANT_InterfaceMethodref -> pool[i] = new ConstantInterfaceMethodrefInfo(this, cr);
                case CONSTANT_NameAndType -> pool[i] = new ConstantNameAndTypeInfo(this, cr);
                case CONSTANT_MethodHandle -> pool[i] = new ConstantMethodHandleInfo(this, cr);
                case CONSTANT_MethodType -> pool[i] = new ConstantMethodTypeInfo(this, cr);
                case CONSTANT_Dynamic -> pool[i] = new ConstantDynamicInfo(this, cr);
                case CONSTANT_InvokeDynamic -> pool[i] = new ConstantInvokeDynamicInfo(this, cr);
                case CONSTANT_Module -> pool[i] = new ConstantModuleInfo(this, cr);
                case CONSTANT_Package -> pool[i] = new ConstantPackageInfo(this, cr);
                default -> throw new IllegalArgumentException("Unknown tag: " + tag);
            }
        }
    }

    public int length() {
        return pool.length;
    }

    public ClassFile ownClassFile() {
        return this.cf;
    }

    public void forEach(BiConsumer<Integer, ConstantPoolInfo> info) {
        int len = pool.length;
        for (int i = 1; i < len; i++) {
            if (!Objects.isNull(pool[i]))
                info.accept(i, pool[i]);
        }
    }

    public ConstantPoolInfo getConstantPoolInfo(int index) throws InvalidIndexException {
        if (index <= 0 || index >= pool.length) {
            throw new InvalidIndexException(index);
        }
        ConstantPoolInfo info = pool[index];
        if (info == null) {
            // this occurs for indices referencing the "second half" of an
            // 8 byte constant, such as CONSTANT_Double or CONSTANT_Long
            throw new InvalidIndexException(index);
        }
        return info;
    }

    public ConstantPoolInfo getConstantPoolInfo(int index, int expectedTag) throws InvalidIndexException, UnexpectedEntryException {
        ConstantPoolInfo info = getConstantPoolInfo(index);
        assert info != null;

        int actualTag = info.getTag();
        if (actualTag != expectedTag) {
            throw new UnexpectedEntryException(index, expectedTag, actualTag);
        }
        return info;
    }

    public <T> T getConstantPoolInfo(int index, Class<T> clazz) throws InvalidIndexException {
        return clazz.cast(getConstantPoolInfo(index));
    }

    public ConstantUtf8Info getUtf8Info(int index) throws InvalidIndexException, UnexpectedEntryException {
        return (ConstantUtf8Info) getConstantPoolInfo(index, CONSTANT_Utf8);
    }

    public String getUtf8Value(int index) throws InvalidIndexException, UnexpectedEntryException {
        ConstantUtf8Info utf8Info = getUtf8Info(index);
        return utf8Info.value;
    }

    public ConstantClassInfo getClassInfo(int index) throws InvalidIndexException, UnexpectedEntryException {
        return (ConstantClassInfo) getConstantPoolInfo(index, CONSTANT_Class);
    }
}
