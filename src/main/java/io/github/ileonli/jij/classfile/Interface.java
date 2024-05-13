package io.github.ileonli.jij.classfile;

import java.io.IOException;

public class Interface {
    public final int class_index;

    public Interface(ClassReader cr) throws IOException {
        class_index = cr.readUnsignedShort();
    }
}
