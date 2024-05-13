package io.github.ileonli.jij.classfile.cp;

import io.github.ileonli.jij.classfile.ClassReader;
import io.github.ileonli.jij.classfile.ConstantPool;
import io.github.ileonli.jij.classfile.ConstantPoolInfo;

import java.io.IOException;

public class ConstantFloatInfo extends ConstantPoolInfo {
    public final float value;

    public ConstantFloatInfo(ClassReader cr) throws IOException {
        value = cr.readFloat();
    }

    @Override
    public int getTag() {
        return ConstantPool.CONSTANT_Float;
    }
}