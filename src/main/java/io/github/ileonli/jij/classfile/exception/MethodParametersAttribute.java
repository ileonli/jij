package io.github.ileonli.jij.classfile.exception;

import io.github.ileonli.jij.classfile.Attribute;
import io.github.ileonli.jij.classfile.ClassReader;

import java.io.IOException;

public class MethodParametersAttribute extends Attribute {
    public final int parameters_count;
    public final Parameter[] parameters;

    public MethodParametersAttribute(ClassReader cr) throws IOException {
        super(cr);
        parameters_count = cr.readUnsignedByte();
        parameters = new Parameter[parameters_count];
        for (int i = 0; i < parameters_count; i++) {
            parameters[i] = new Parameter(cr);
        }
    }

    public static class Parameter {
        public final int name_index;
        public final int access_flags;

        public Parameter(ClassReader cr) throws IOException {
            name_index = cr.readUnsignedShort();
            access_flags = cr.readUnsignedShort();
        }
    }
}
