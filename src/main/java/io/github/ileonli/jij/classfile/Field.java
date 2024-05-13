package io.github.ileonli.jij.classfile;

import java.io.IOException;

public class Field {
    public final AccessFlags access_flags;
    public final int name_index;
    public final Descriptor descriptor;
    public final Attributes attributes;

    public Field(ClassReader cr) throws IOException {
        access_flags = new AccessFlags(cr);
        name_index = cr.readUnsignedShort();
        descriptor = new Descriptor(cr);
        attributes = new Attributes(cr);
    }
}
