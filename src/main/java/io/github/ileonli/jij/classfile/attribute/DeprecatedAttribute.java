package io.github.ileonli.jij.classfile.attribute;

import io.github.ileonli.jij.classfile.Attribute;
import io.github.ileonli.jij.classfile.ClassReader;

import java.io.IOException;

public class DeprecatedAttribute extends Attribute {
    public DeprecatedAttribute(ClassReader cr) throws IOException {
        super(cr);
    }
}
