package io.github.ileonli.jij.classfile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;

public class Interfaces {
    public final Interface[] interfaces;

    public Interfaces(ClassReader cr) throws IOException {
        int interfaces_count = cr.readUnsignedShort();
        interfaces = new Interface[interfaces_count];
        for (int i = 0; i < interfaces_count; i++)
            interfaces[i] = new Interface(cr);
    }

    public void forEach(Consumer<? super Interface> action) {
        Objects.requireNonNull(action);
        Arrays.stream(interfaces).forEach(action);
    }

    public int length() {
        return interfaces.length;
    }
}
