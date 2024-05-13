package io.github.ileonli.jij.classfile.cp;

import io.github.ileonli.jij.classfile.ClassReader;
import io.github.ileonli.jij.classfile.ConstantPool;
import io.github.ileonli.jij.classfile.ConstantPoolInfo;

import java.io.IOException;

public class ConstantInvokeDynamicInfo extends ConstantPoolInfo {
    public final int bootstrap_method_attr_index;
    public final int name_and_type_index;

    public ConstantInvokeDynamicInfo(ConstantPool cp, ClassReader cr) throws IOException {
        super(cp);
        bootstrap_method_attr_index = cr.readUnsignedShort();
        name_and_type_index = cr.readUnsignedShort();
    }

    public ConstantNameAndTypeInfo getNameAndType() {
        return cp.getConstantPoolInfo(name_and_type_index, ConstantNameAndTypeInfo.class);
    }

    @Override
    public int getTag() {
        return ConstantPool.CONSTANT_InvokeDynamic;
    }
}