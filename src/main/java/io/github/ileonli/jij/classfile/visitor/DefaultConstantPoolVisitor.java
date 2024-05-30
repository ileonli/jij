package io.github.ileonli.jij.classfile.visitor;

import io.github.ileonli.jij.classfile.ClassFileUtils;
import io.github.ileonli.jij.classfile.ConstantPool;
import io.github.ileonli.jij.classfile.ConstantPoolInfo;
import io.github.ileonli.jij.classfile.ConstantPoolVisitor;
import io.github.ileonli.jij.classfile.cp.*;

import java.util.function.Function;

import static io.github.ileonli.jij.classfile.ClassFileUtils.checkName;

public class DefaultConstantPoolVisitor implements ConstantPoolVisitor<String> {
    private final ConstantPool cp;
    private final boolean forInstruction;

    public DefaultConstantPoolVisitor(ConstantPool cp) {
        this(cp, false);
    }

    public DefaultConstantPoolVisitor(ConstantPool cp, boolean forInstruction) {
        this.cp = cp;
        this.forInstruction = forInstruction;
    }

    public String visitNameAndTypeInfo(ConstantNameAndTypeInfo nat) {
        String name = nat.getName();
        String type = nat.getType();
        return checkName(name) + ":" + type;
    }

    public String visitRefInfo(ConstantClassInfo clazz, ConstantNameAndTypeInfo nat, boolean simplify) {
        String nameAndType = visitNameAndTypeInfo(nat);
        if (forInstruction && simplify) {
            // simplify references within this class for instruction
            return nameAndType;
        }
        return clazz.getName() + "." + nameAndType;
    }

    public <T> T visitConstantPoolInfo(int idx, Function<ConstantPoolInfo, T> action) {
        ConstantPoolInfo info = cp.getConstantPoolInfo(idx);
        return action.apply(info);
    }

    private boolean withInClass(int class_index) {
        return class_index == cp.ownClassFile().this_class;
    }

    @Override
    public String visitUtf8(ConstantUtf8Info info) {
        String s = info.value;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '\t' -> sb.append('\\').append('t');
                case '\n' -> sb.append('\\').append('n');
                case '\r' -> sb.append('\\').append('r');
                case '\b' -> sb.append('\\').append('b');
                case '\f' -> sb.append('\\').append('f');
                case '\"' -> sb.append('\\').append('\"');
                case '\'' -> sb.append('\\').append('\'');
                case '\\' -> sb.append('\\').append('\\');
                default -> {
                    if (Character.isISOControl(c)) {
                        sb.append(String.format("\\u%04x", (int) c));
                        break;
                    }
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    @Override
    public String visitInteger(ConstantIntegerInfo info) {
        return info.value + "f";
    }

    @Override
    public String visitFloat(ConstantFloatInfo info) {
        return info.value + "f";
    }

    @Override
    public String visitLong(ConstantLongInfo info) {
        return info.value + "f";
    }

    @Override
    public String visitDouble(ConstantDoubleInfo info) {
        return info.value + "f";
    }

    @Override
    public String visitClass(ConstantClassInfo info) {
        return ClassFileUtils.checkName(info.getName());
    }

    @Override
    public String visitString(ConstantStringInfo info) {
        ConstantUtf8Info utf8 = cp.getConstantPoolInfo(info.string_index, ConstantUtf8Info.class);
        return visitUtf8(utf8);
    }

    @Override
    public String visitFieldref(ConstantFieldrefInfo info) {
        return visitRefInfo(info.getClassInfo(), info.getNameAndTypeInfo(), withInClass(info.class_index));
    }

    @Override
    public String visitMethodref(ConstantMethodrefInfo info) {
        return visitRefInfo(info.getClassInfo(), info.getNameAndTypeInfo(), withInClass(info.class_index));
    }

    @Override
    public String visitInterfaceMethodref(ConstantInterfaceMethodrefInfo info) {
        return visitRefInfo(info.getClassInfo(), info.getNameAndTypeInfo(), withInClass(info.class_index));
    }

    @Override
    public String visitNameAndType(ConstantNameAndTypeInfo info) {
        return visitNameAndTypeInfo(info);
    }

    @Override
    public String visitMethodHandle(ConstantMethodHandleInfo info) {
        int referenceIndex = info.reference_index;
        // charter 4.4.8
        return info.reference_kind + " " + switch (info.reference_kind) {
            case REF_getField, REF_getStatic, REF_putField, REF_putStatic -> {
                ConstantFieldrefInfo cfr = cp.getConstantPoolInfo(referenceIndex, ConstantFieldrefInfo.class);
                yield visitRefInfo(cfr.getClassInfo(), cfr.getNameAndTypeInfo(), withInClass(cfr.class_index));
            }
            case REF_invokeVirtual, REF_newInvokeSpecial -> {
                ConstantMethodrefInfo cmr = cp.getConstantPoolInfo(referenceIndex, ConstantMethodrefInfo.class);
                yield visitRefInfo(cmr.getClassInfo(), cmr.getNameAndTypeInfo(), withInClass(cmr.class_index));
            }
            case REF_invokeStatic, REF_invokeSpecial -> {
                ConstantMethodrefInfo cmr = cp.getConstantPoolInfo(referenceIndex, ConstantMethodrefInfo.class);
                yield visitRefInfo(cmr.getClassInfo(), cmr.getNameAndTypeInfo(), withInClass(cmr.class_index));
            }
            case REF_invokeInterface -> {
                ConstantInterfaceMethodrefInfo cim
                        = cp.getConstantPoolInfo(referenceIndex, ConstantInterfaceMethodrefInfo.class);
                yield visitRefInfo(cim.getClassInfo(), cim.getNameAndTypeInfo(), withInClass(cim.class_index));
            }
        };
    }

    @Override
    public String visitMethodType(ConstantMethodTypeInfo info) {
        return info.getType();
    }

    @Override
    public String visitDynamic(ConstantDynamicInfo info) {
        String callee = visitNameAndTypeInfo(info.getNameAndType());
        return "#" + info.bootstrap_method_attr_index + ":" + callee;
    }

    @Override
    public String visitInvokeDynamic(ConstantInvokeDynamicInfo info) {
        String callee = visitNameAndTypeInfo(info.getNameAndType());
        return "#" + info.bootstrap_method_attr_index + ":" + callee;
    }

    @Override
    public String visitModule(ConstantModuleInfo info) {
        return checkName(info.getName());
    }

    @Override
    public String visitPackage(ConstantPackageInfo info) {
        return checkName(info.getName());
    }
}
