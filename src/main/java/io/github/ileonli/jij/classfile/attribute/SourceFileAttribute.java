package io.github.ileonli.jij.classfile.attribute;

import io.github.ileonli.jij.classfile.Attribute;
import io.github.ileonli.jij.classfile.ClassReader;

import java.io.IOException;

public class SourceFileAttribute extends Attribute {
    public final int sourcefile_index;

    public SourceFileAttribute(ClassReader cr) throws IOException {
        super(cr);
        sourcefile_index = cr.readUnsignedShort();
    }

    public String getSourceFileName() {
        return cp.getUtf8Value(sourcefile_index);
    }
}
