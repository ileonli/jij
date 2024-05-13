package io.github.ileonli.jij.classfile.cp;

import io.github.ileonli.jij.classfile.ClassReader;
import io.github.ileonli.jij.classfile.ConstantPool;
import io.github.ileonli.jij.classfile.ConstantPoolInfo;

import java.io.IOException;

public class ConstantDoubleInfo extends ConstantPoolInfo {
    public final double value;

    public ConstantDoubleInfo(ClassReader cr) throws IOException {
        value = cr.readDouble();
    }

    @Override
    public int getTag() {
        return ConstantPool.CONSTANT_Double;
    }
}