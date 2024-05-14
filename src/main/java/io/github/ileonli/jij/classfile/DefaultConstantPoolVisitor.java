package io.github.ileonli.jij.classfile;

import io.github.ileonli.jij.classfile.cp.*;

import java.util.function.Function;

import static io.github.ileonli.jij.classfile.ClassFileUtils.checkName;

public class DefaultConstantPoolVisitor implements ConstantPoolVisitor<String> {
    private final ConstantPool cp;

    public DefaultConstantPoolVisitor(ConstantPool cp) {
        this.cp = cp;
    }

    public String visitNameAndTypeInfo(ConstantNameAndTypeInfo nat) {
        String name = nat.getName();
        String type = nat.getType();
        return checkName(name) + ":" + type;
    }

    public String visitRefInfo(ConstantClassInfo clazz, ConstantNameAndTypeInfo nat) {
        String nameAndType = visitNameAndTypeInfo(nat);
        return clazz.getName() + "." + nameAndType;
    }

    public <T> T visitConstantPoolInfo(int idx, Function<ConstantPoolInfo, T> action) {
        ConstantPoolInfo info = cp.getConstantPoolInfo(idx);
        return action.apply(info);
    }

    @Override
    public String visitUtf8(ConstantUtf8Info info) {
        String s = info.value;
        StringBuilder sb = new StringBuilder();
        char[] charArray = s.toCharArray();
        for (char c : charArray) {
            switch (c) {
                case '\t' -> sb.append('\\').append('t');
                case '\n' -> sb.append('\\').append('n');
                case '\r' -> sb.append('\\').append('r');
                case '\"' -> sb.append('\\').append('\"');
                default -> sb.append(c);
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
        return info.getName();
    }

    @Override
    public String visitString(ConstantStringInfo info) {
        return info.getString();
    }

    @Override
    public String visitFieldref(ConstantFieldrefInfo info) {
        return visitRefInfo(info.getClassInfo(), info.getNameAndTypeInfo());
    }

    @Override
    public String visitMethodref(ConstantMethodrefInfo info) {
        return visitRefInfo(info.getClassInfo(), info.getNameAndTypeInfo());
    }

    @Override
    public String visitInterfaceMethodref(ConstantInterfaceMethodrefInfo info) {
        return visitRefInfo(info.getClassInfo(), info.getNameAndTypeInfo());
    }

    @Override
    public String visitNameAndType(ConstantNameAndTypeInfo info) {
        return visitNameAndTypeInfo(info);
    }

    @Override
    public String visitMethodHandle(ConstantMethodHandleInfo info) {
        int referenceIndex = info.reference_index;
        // charter 4.4.8
        return switch (info.reference_kind) {
            case REF_getField, REF_getStatic, REF_putField, REF_putStatic -> {
                ConstantFieldrefInfo cfr = cp.getConstantPoolInfo(referenceIndex, ConstantFieldrefInfo.class);
                yield visitRefInfo(cfr.getClassInfo(), cfr.getNameAndTypeInfo());
            }
            case REF_invokeVirtual, REF_newInvokeSpecial -> {
                ConstantMethodrefInfo cmr = cp.getConstantPoolInfo(referenceIndex, ConstantMethodrefInfo.class);
                yield visitRefInfo(cmr.getClassInfo(), cmr.getNameAndTypeInfo());
            }
            case REF_invokeStatic, REF_invokeSpecial -> {
                // TODO: handle two types
                yield "???";
            }
            case REF_invokeInterface -> {
                ConstantInterfaceMethodrefInfo cim
                        = cp.getConstantPoolInfo(referenceIndex, ConstantInterfaceMethodrefInfo.class);
                yield visitRefInfo(cim.getClassInfo(), cim.getNameAndTypeInfo());
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