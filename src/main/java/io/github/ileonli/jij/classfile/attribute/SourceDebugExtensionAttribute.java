package io.github.ileonli.jij.classfile.attribute;

import io.github.ileonli.jij.classfile.Attribute;
import io.github.ileonli.jij.classfile.ClassReader;

import java.io.IOException;

public class SourceDebugExtensionAttribute extends Attribute {
    public final byte[] debug_extension;

    public SourceDebugExtensionAttribute(ClassReader cr) throws IOException {
        super(cr);
        debug_extension = cr.readBytes(attribute_length);
    }
}
