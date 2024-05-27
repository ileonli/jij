package io.github.ileonli.jij.classfile;

import java.io.IOException;

public class Attribute {
    public final int attribute_length;

    protected final ConstantPool cp;

    public Attribute(ClassReader cr) throws IOException {
        attribute_length = cr.readInt();

        cp = cr.classFile.constant_pool;
    }
}
