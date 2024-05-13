package io.github.ileonli.jij.classfile.attribute;

import io.github.ileonli.jij.classfile.Attribute;
import io.github.ileonli.jij.classfile.ClassReader;

import java.io.IOException;

public class SignatureAttribute extends Attribute {
    public final int signature_index;

    public SignatureAttribute(ClassReader cr) throws IOException {
        super(cr);

        signature_index = cr.readUnsignedShort();
    }

    public String getSignature() {
        return cp.getUtf8Value(signature_index);
    }
}
