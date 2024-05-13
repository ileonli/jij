package io.github.ileonli.jij.classfile.attribute;

import io.github.ileonli.jij.classfile.Attribute;
import io.github.ileonli.jij.classfile.Attributes;
import io.github.ileonli.jij.classfile.ClassReader;
import io.github.ileonli.jij.classfile.exception.IllegalAttributeLengthException;

import java.io.IOException;

public class ConstantValueAttribute extends Attribute {
    public final int constantvalue_index;

    public ConstantValueAttribute(ClassReader cr) throws IOException {
        super(cr);
        int attribute_length = cr.readUnsignedByte();
        if (attribute_length != 2) {
            throw new IllegalAttributeLengthException(Attributes.ConstantValue, attribute_length);
        }
        constantvalue_index = cr.readUnsignedShort();
    }
}