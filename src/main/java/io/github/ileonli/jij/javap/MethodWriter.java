package io.github.ileonli.jij.javap;

import io.github.ileonli.jij.classfile.AccessFlags;
import io.github.ileonli.jij.classfile.ClassFile;
import io.github.ileonli.jij.classfile.Method;
import io.github.ileonli.jij.classfile.attribute.CodeAttribute;
import io.github.ileonli.jij.classfile.attribute.ExceptionsAttribute;
import io.github.ileonli.jij.classfile.cp.ConstantClassInfo;
import io.github.ileonli.jij.classfile.exception.InvalidDescriptorException;

import java.util.Arrays;
import java.util.Set;

public class MethodWriter extends BasicWriter {
    private final ClassFile cf;

    public MethodWriter(ClassFile cf) {
        this.cf = cf;
    }

    public void writeHeader(Method method) {
        indent(1);
        try {
            Set<String> modifiers = method.access_flags.getMethodModifiers();
            String returnType = method.descriptor.getReturnType();
            String parameterTypes = method.descriptor.getParameterTypes();
            print(String.join(" ", modifiers) + " " + returnType);

            String methodName = method.getName();
            switch (methodName) {
                case "<init>" -> methodName = cf.thisClassName();
                case "<clinit>" -> print("{}");
            }
            print(" " + methodName + parameterTypes);

            ExceptionsAttribute exceptions = method.attributes.getAttribute(ExceptionsAttribute.class);
            if (exceptions != null) {
                print(" throws ");
                ConstantClassInfo[] ccis = exceptions.getExceptions();
                print(String.join(", ", Arrays.stream(ccis).map(ConstantClassInfo::getName).toList()) + ";");
            }
            println();

            indent(1);
            println("descriptor: " + method.descriptor.descriptor);

            AccessFlags accessFlags = method.access_flags;
            println("flags: " + "(" + String.format("0x%04x", accessFlags.flags) + ")" + " " +
                    String.join(", ", accessFlags.getMethodFlags()));
            indent(-1);

            indent(-1);
        } catch (InvalidDescriptorException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(Method method) {
        writeHeader(method);

        indent(1);
        try {
            indent(1);
            println("Code:");

            int parameterSize = method.descriptor.getParameterSize();
            CodeAttribute attr = method.attributes.getAttribute(CodeAttribute.class);

            indent(1);
            println("stack=" + attr.max_stack +
                    ", locals=" + attr.max_locals +
                    ", args_size=" + parameterSize);
            indent(-1);

            CodeWriter codeWriter = new CodeWriter();
            codeWriter.write(attr, cf.constant_pool);

            indent(-1);
        } catch (Exception ignored) {
        }

        indent(-1);
    }
}
