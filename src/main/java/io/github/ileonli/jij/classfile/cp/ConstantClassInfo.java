package io.github.ileonli.jij.classfile.cp;

import io.github.ileonli.jij.classfile.ClassReader;
import io.github.ileonli.jij.classfile.ConstantPool;
import io.github.ileonli.jij.classfile.ConstantPoolInfo;
import io.github.ileonli.jij.classfile.exception.InvalidIndexException;
import io.github.ileonli.jij.classfile.exception.UnexpectedEntryException;

import java.io.IOException;

public class ConstantClassInfo extends ConstantPoolInfo {
    public final int name_index;

    public ConstantClassInfo(ConstantPool cp, ClassReader cr) throws IOException {
        super(cp);
        name_index = cr.readUnsignedShort();
    }

    public String getName() throws InvalidIndexException, UnexpectedEntryException {
        return cp.getUtf8Value(name_index);
    }

    @Override
    public int getTag() {
        return ConstantPool.CONSTANT_Class;
    }
}