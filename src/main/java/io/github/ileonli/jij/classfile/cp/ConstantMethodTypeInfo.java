package io.github.ileonli.jij.classfile.cp;

import io.github.ileonli.jij.classfile.ClassReader;
import io.github.ileonli.jij.classfile.ConstantPool;
import io.github.ileonli.jij.classfile.ConstantPoolInfo;
import io.github.ileonli.jij.classfile.exception.ConstantPoolException;

import java.io.IOException;

public class ConstantMethodTypeInfo extends ConstantPoolInfo {
    public final int descriptor_index;

    public ConstantMethodTypeInfo(ConstantPool cp, ClassReader cr) throws IOException {
        super(cp);
        descriptor_index = cr.readUnsignedShort();
    }

    @Override
    public int getTag() {
        return ConstantPool.CONSTANT_MethodType;
    }

    public String getType() throws ConstantPoolException {
        return cp.getUtf8Value(descriptor_index);
    }
}