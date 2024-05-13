package io.github.ileonli.jij.classfile.cp;

import io.github.ileonli.jij.classfile.ClassReader;
import io.github.ileonli.jij.classfile.ConstantPool;
import io.github.ileonli.jij.classfile.ConstantPoolInfo;
import io.github.ileonli.jij.classfile.exception.ConstantPoolException;

import java.io.IOException;

public class ConstantModuleInfo extends ConstantPoolInfo {
    public final int name_index;

    public ConstantModuleInfo(ConstantPool cp, ClassReader cr) throws IOException {
        super(cp);
        name_index = cr.readUnsignedShort();
    }

    public String getName() throws ConstantPoolException {
        return cp.getUtf8Value(name_index);
    }

    @Override
    public int getTag() {
        return ConstantPool.CONSTANT_Module;
    }
}