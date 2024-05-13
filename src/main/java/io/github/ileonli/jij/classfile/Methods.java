package io.github.ileonli.jij.classfile;

import java.io.IOException;
import java.util.Objects;
import java.util.function.BiConsumer;

public class Methods {
    public final Method[] methods;

    public Methods(ClassReader cr) throws IOException {
        int methods_count = cr.readUnsignedShort();
        methods = new Method[methods_count];
        for (int i = 0; i < methods_count; i++) {
            methods[i] = new Method(cr);
        }
    }

    public void forEach(BiConsumer<Integer, Method> action) {
        Objects.requireNonNull(action);
        for (int i = 0; i < methods.length; i++) {
            action.accept(i, methods[i]);
        }
    }

    public int length() {
        return methods.length;
    }
}
