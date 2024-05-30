package io.github.ileonli.jij.classfile;

import io.github.ileonli.jij.classfile.cp.*;

public abstract class ConstantPoolInfo {
    protected final ConstantPool cp;

    public ConstantPoolInfo() {
        this(null);
    }

    public ConstantPoolInfo(ConstantPool cp) {
        this.cp = cp;
    }

    public abstract int getTag();

    public String infoName() {
        String n = this.getClass().getSimpleName();
        return n.replace("Constant", "").replace("Info", "");
    }

    public String tagName() {
        int tag = getTag();
        return switch (tag) {
            case ConstantPool.CONSTANT_Utf8 -> "Utf8";
            case ConstantPool.CONSTANT_Integer -> "int";
            case ConstantPool.CONSTANT_Float -> "float";
            case ConstantPool.CONSTANT_Long -> "long";
            case ConstantPool.CONSTANT_Double -> "double";
            case ConstantPool.CONSTANT_Class -> "class";
            case ConstantPool.CONSTANT_String -> "String";
            case ConstantPool.CONSTANT_Fieldref -> "Field";
            case ConstantPool.CONSTANT_MethodHandle -> "MethodHandle";
            case ConstantPool.CONSTANT_MethodType -> "MethodType";
            case ConstantPool.CONSTANT_Methodref -> "Method";
            case ConstantPool.CONSTANT_InterfaceMethodref -> "InterfaceMethod";
            case ConstantPool.CONSTANT_InvokeDynamic -> "InvokeDynamic";
            case ConstantPool.CONSTANT_Dynamic -> "Dynamic";
            case ConstantPool.CONSTANT_NameAndType -> "NameAndType";
            default -> "(unknown tag " + tag + ")";
        };
    }

    public <R> R accept(ConstantPoolVisitor<R> v) {
        return switch (this) {
            case ConstantUtf8Info info -> v.visitUtf8(info);
            case ConstantIntegerInfo info -> v.visitInteger(info);
            case ConstantFloatInfo info -> v.visitFloat(info);
            case ConstantLongInfo info -> v.visitLong(info);
            case ConstantDoubleInfo info -> v.visitDouble(info);
            case ConstantClassInfo info -> v.visitClass(info);
            case ConstantStringInfo info -> v.visitString(info);
            case ConstantFieldrefInfo info -> v.visitFieldref(info);
            case ConstantMethodrefInfo info -> v.visitMethodref(info);
            case ConstantInterfaceMethodrefInfo info -> v.visitInterfaceMethodref(info);
            case ConstantNameAndTypeInfo info -> v.visitNameAndType(info);
            case ConstantMethodHandleInfo info -> v.visitMethodHandle(info);
            case ConstantMethodTypeInfo info -> v.visitMethodType(info);
            case ConstantDynamicInfo info -> v.visitDynamic(info);
            case ConstantInvokeDynamicInfo info -> v.visitInvokeDynamic(info);
            case ConstantModuleInfo info -> v.visitModule(info);
            case ConstantPackageInfo info -> v.visitPackage(info);
            default -> throw new IllegalStateException("Unexpected value: " + this);
        };
    }
}