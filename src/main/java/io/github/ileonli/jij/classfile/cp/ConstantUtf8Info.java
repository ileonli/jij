package io.github.ileonli.jij.classfile.cp;

import io.github.ileonli.jij.classfile.ClassReader;
import io.github.ileonli.jij.classfile.ConstantPool;
import io.github.ileonli.jij.classfile.ConstantPoolInfo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ConstantUtf8Info extends ConstantPoolInfo {
    public final int length;
    public final String value;

    public ConstantUtf8Info(ClassReader cr) throws IOException {
        length = cr.readUnsignedShort();

        byte[] bytes = cr.readBytes(length);
        value = new String(bytes, StandardCharsets.UTF_8);
    }

    @Override
    public int getTag() {
        return ConstantPool.CONSTANT_Utf8;
    }
}