package io.github.ileonli.jij.classfile.cp;

import io.github.ileonli.jij.classfile.ClassReader;
import io.github.ileonli.jij.classfile.ConstantPool;
import io.github.ileonli.jij.classfile.ConstantPoolInfo;
import io.github.ileonli.jij.classfile.exception.ConstantPoolException;

import java.io.IOException;

public class ConstantPackageInfo extends ConstantPoolInfo {
    public final int name_index;

    public ConstantPackageInfo(ConstantPool cp, ClassReader cr) throws IOException {
        super(cp);
        name_index = cr.readUnsignedShort();
    }

    @Override
    public int getTag() {
        return ConstantPool.CONSTANT_Package;
    }

    public String getName() throws ConstantPoolException {
        return cp.getUtf8Value(name_index);
    }
}