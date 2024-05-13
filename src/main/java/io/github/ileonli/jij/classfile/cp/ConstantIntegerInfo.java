package io.github.ileonli.jij.classfile.cp;

import io.github.ileonli.jij.classfile.ClassReader;
import io.github.ileonli.jij.classfile.ConstantPool;
import io.github.ileonli.jij.classfile.ConstantPoolInfo;

import java.io.IOException;


public class ConstantIntegerInfo extends ConstantPoolInfo {
    public final int value;

    public ConstantIntegerInfo(ClassReader cr) throws IOException {
        value = cr.readInt();
    }

    @Override
    public int getTag() {
        return ConstantPool.CONSTANT_Integer;
    }
}