package io.github.ileonli.jij.classfile;

import java.io.IOException;

public class Method {
    public final AccessFlags access_flags;
    public final int name_index;
    public final Descriptor descriptor;
    public final Attributes attributes;

    private final String methodName;

    public Method(ClassReader cr) throws IOException {
        ConstantPool cp = cr.classFile.constant_pool;

        access_flags = new AccessFlags(cr);

        name_index = cr.readUnsignedShort();
        methodName = cp.getUtf8Value(name_index);

        descriptor = new Descriptor(cr);
        attributes = new Attributes(cr);
    }

    public String getName() {
        return methodName;
    }
}
