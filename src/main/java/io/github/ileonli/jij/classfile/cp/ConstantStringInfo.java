package io.github.ileonli.jij.classfile.cp;

import io.github.ileonli.jij.classfile.ClassReader;
import io.github.ileonli.jij.classfile.ConstantPool;
import io.github.ileonli.jij.classfile.ConstantPoolInfo;
import io.github.ileonli.jij.classfile.exception.InvalidIndexException;
import io.github.ileonli.jij.classfile.exception.UnexpectedEntryException;

import java.io.IOException;

public class ConstantStringInfo extends ConstantPoolInfo {
    public final int string_index;

    public ConstantStringInfo(ConstantPool cp, ClassReader cr) throws IOException {
        super(cp);
        string_index = cr.readUnsignedShort();
    }

    @Override
    public int getTag() {
        return ConstantPool.CONSTANT_String;
    }

    public String getString() throws InvalidIndexException, UnexpectedEntryException {
        return cp.getUtf8Value(string_index);
    }
}