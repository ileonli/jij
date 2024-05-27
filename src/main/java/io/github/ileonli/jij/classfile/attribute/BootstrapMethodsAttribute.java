package io.github.ileonli.jij.classfile.attribute;

import io.github.ileonli.jij.classfile.Attribute;
import io.github.ileonli.jij.classfile.ClassReader;

import java.io.IOException;

public class BootstrapMethodsAttribute extends Attribute {
    public final int num_bootstrap_methods;
    public final BootstrapMethod[] bootstrap_methods;

    public BootstrapMethodsAttribute(ClassReader cr) throws IOException {
        super(cr);
        num_bootstrap_methods = cr.readUnsignedShort();
        bootstrap_methods = new BootstrapMethod[num_bootstrap_methods];
        for (int i = 0; i < num_bootstrap_methods; i++) {
            bootstrap_methods[i] = new BootstrapMethod(cr);
        }
    }

    public static class BootstrapMethod {
        public final int bootstrap_method_ref;
        public final int num_bootstrap_arguments;
        public final int[] bootstrap_arguments;

        public BootstrapMethod(ClassReader cr) throws IOException {
            bootstrap_method_ref = cr.readUnsignedShort();
            num_bootstrap_arguments = cr.readUnsignedShort();
            bootstrap_arguments = new int[num_bootstrap_arguments];
            for (int i = 0; i < num_bootstrap_arguments; i++) {
                bootstrap_arguments[i] = cr.readUnsignedShort();
            }
        }
    }
}
