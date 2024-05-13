package io.github.ileonli.jij.classfile;

import io.github.ileonli.jij.classfile.exception.InvalidDescriptorException;

import java.io.IOException;

public class Descriptor {
    private final int descriptor_index;
    public final String descriptor;
    private int count;

    public Descriptor(ClassReader cr) throws IOException {
        descriptor_index = cr.readUnsignedShort();

        ConstantPool cp = cr.classFile.constant_pool;
        descriptor = cp.getUtf8Value(descriptor_index);
    }

    public String parse(String desc, int start, int end) throws InvalidDescriptorException {
        int p = start;
        StringBuilder sb = new StringBuilder();
        int dims = 0;

        count = 0;
        while (p < end) {
            String type;
            switch (desc.charAt(p++)) {
                case '(' -> {
                    sb.append('(');
                    continue;
                }
                case ')' -> {
                    sb.append(')');
                    continue;
                }
                case '[' -> {
                    dims++;
                    continue;
                }
                case 'B' -> type = "byte";
                case 'C' -> type = "char";
                case 'D' -> type = "double";
                case 'F' -> type = "float";
                case 'I' -> type = "int";
                case 'J' -> type = "long";
                case 'L' -> {
                    int sep = desc.indexOf(';', p);
                    if (sep == -1)
                        throw new InvalidDescriptorException(desc, p - 1);
                    type = desc.substring(p, sep).replace('/', '.');
                    p = sep + 1;
                }
                case 'S' -> type = "short";
                case 'Z' -> type = "boolean";
                case 'V' -> type = "void";
                default -> throw new InvalidDescriptorException(desc, p - 1);
            }

            if (sb.length() > 1 && sb.charAt(0) == '(')
                sb.append(", ");
            sb.append(type);
            for (; dims > 0; dims--)
                sb.append("[]");
            count++;
        }
        return sb.toString();
    }

    public String getParameterTypes() throws InvalidDescriptorException {
        int end = descriptor.indexOf(")");
        if (end == -1) throw new InvalidDescriptorException(descriptor);
        return parse(descriptor, 0, end + 1);
    }

    public int getParameterSize() throws InvalidDescriptorException {
        getParameterTypes();
        return count;
    }

    public String getReturnType() throws InvalidDescriptorException {
        int end = descriptor.indexOf(")");
        if (end == -1) throw new InvalidDescriptorException(descriptor);
        return parse(descriptor, end + 1, descriptor.length());
    }

    public String getFieldType() throws InvalidDescriptorException {
        return parse(descriptor, 0, descriptor.length());
    }
}
