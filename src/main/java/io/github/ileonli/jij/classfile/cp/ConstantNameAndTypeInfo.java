package io.github.ileonli.jij.classfile.cp;

import io.github.ileonli.jij.classfile.ClassReader;
import io.github.ileonli.jij.classfile.ConstantPool;
import io.github.ileonli.jij.classfile.ConstantPoolInfo;

import java.io.IOException;

public class ConstantNameAndTypeInfo extends ConstantPoolInfo {
    public final int name_index;
    public final int type_index;

    public ConstantNameAndTypeInfo(ConstantPool cp, ClassReader cr) throws IOException {
        super(cp);
        name_index = cr.readUnsignedShort();
        type_index = cr.readUnsignedShort();
    }

    @Override
    public int getTag() {
        return ConstantPool.CONSTANT_NameAndType;
    }

    public String getName() {
        return cp.getUtf8Value(name_index);
    }

    public String getType() {
        return cp.getUtf8Value(type_index);
    }
}