package io.github.ileonli.jij.classfile.attribute;

import io.github.ileonli.jij.classfile.Attribute;
import io.github.ileonli.jij.classfile.ClassReader;

import java.io.IOException;

public class EnclosingMethodAttribute extends Attribute {
    public final int class_index;
    public final int method_index;

    public EnclosingMethodAttribute(ClassReader cr) throws IOException {
        super(cr);
        class_index = cr.readUnsignedShort();
        method_index = cr.readUnsignedShort();
    }
}
