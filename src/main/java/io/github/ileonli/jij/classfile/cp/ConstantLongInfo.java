package io.github.ileonli.jij.classfile.cp;

import io.github.ileonli.jij.classfile.ClassReader;
import io.github.ileonli.jij.classfile.ConstantPool;
import io.github.ileonli.jij.classfile.ConstantPoolInfo;

import java.io.IOException;

public class ConstantLongInfo extends ConstantPoolInfo {
    public final long value;

    public ConstantLongInfo(ClassReader cr) throws IOException {
        value = cr.readLong();
    }

    @Override
    public int getTag() {
        return ConstantPool.CONSTANT_Long;
    }
}