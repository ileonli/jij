package io.github.ileonli.jij.classfile;

import java.io.IOException;

public class Fields {
    public final Field[] fields;

    public Fields(ClassReader cr) throws IOException {
        int fields_count = cr.readUnsignedShort();
        fields = new Field[fields_count];
        for (int i = 0; i < fields_count; i++) {
            fields[i] = new Field(cr);
        }
    }

    public int length() {
        return fields.length;
    }
}
