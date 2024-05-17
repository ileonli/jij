package io.github.ileonli.jij.javap;

import io.github.ileonli.jij.classfile.*;
import io.github.ileonli.jij.classfile.attribute.CodeAttribute;
import io.github.ileonli.jij.classfile.attribute.ExceptionsAttribute;
import io.github.ileonli.jij.classfile.attribute.SourceFileAttribute;
import io.github.ileonli.jij.classfile.cp.ConstantClassInfo;
import io.github.ileonli.jij.classfile.exception.InvalidDescriptorException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

public class ClassWriter extends BasicWriter {
    private final ConstantPoolWriter poolWriter;
    private final CodeWriter codeWriter;

    public ClassWriter(ClassFile cf) {
        this(cf, 0);
    }

    public ClassWriter(ClassFile cf, int indentWidth) {
        super(INDEX_WIDTH, indentWidth);
        this.poolWriter = new ConstantPoolWriter(cf.constant_pool);
        this.codeWriter = new CodeWriter();
    }


    private BasicFileAttributes readAttributes(Path path) {
        try {
            return Files.readAttributes(path, BasicFileAttributes.class);
        } catch (IOException e) {
            return null;
        }
    }

    private String checksum(Path path) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(Files.readAllBytes(path));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = String.format("%02x", b);
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException | IOException e) {
            return "???";
        }
    }

    private String getSourceFileName(ClassFile cf) {
        SourceFileAttribute sourceFileAttribute = cf.getAttribute(SourceFileAttribute.class);
        return sourceFileAttribute.getSourceFileName();
    }

    private void writeClassfile(ClassFile cf) {
        println("Classfile " + cf.path.toString());
        indent(1);

        BasicFileAttributes attributes = readAttributes(cf.path);
        if (attributes != null) {
            FileTime lastModifiedTime = attributes.lastModifiedTime();
            Date date = new Date(lastModifiedTime.toMillis());
            DateFormat df = DateFormat.getDateInstance();
            print("Last modified " + df.format(date) + "; ");

            long size = attributes.size();
            if (size > 0) {
                println(("size " + size + " bytes"));
            }
        }

        println("SHA-256 checksum " + checksum(cf.path));

        println("Compiled from " + getSourceFileName(cf));
        indent(-1);
    }

    private void writeClassInfo(ClassFile cf) {
        AccessFlags accessFlags = cf.access_flags;
        Set<String> classModifiers = accessFlags.getClassModifiers();
        Set<String> classFlags = accessFlags.getClassFlags();

        String thisClassName = cf.thisClassName();
        String superClassName = cf.superClassName();

        println(String.join(" ", classModifiers) + (cf.isClass() ? " class " : " interface ") + thisClassName);

        indent(1);

        println("minor version: " + cf.minor_version);
        println("major version: " + cf.major_version);
        println("flags: " + "(" + String.format("0x%04x", accessFlags.flags) + ")" + " " + String.join(", ", classFlags));

        print("this_class: #" + cf.this_class);
        if (cf.this_class != 0) {
            tab();
            print("// " + thisClassName);
        }
        println();

        print("super_class: #" + cf.super_class);
        if (cf.super_class != 0) {
            tab();
            print("// " + superClassName);
        }
        println();

        print("interfaces: " + cf.interfaces.length());
        print(", fields: " + cf.fields.length());
        print(", methods: " + cf.methods.length());
        println(", attributes: " + cf.attributes.length());

        indent(-1);
    }

    private void writeConstantPool(ClassFile cf) {
        poolWriter.write(cf.constant_pool);
    }

    private void writeMethod(ClassFile cf, Method method) {
        String returnType, parameterTypes;
        int parameterSize = 0;
        try {
            returnType = method.descriptor.getReturnType();
            parameterTypes = method.descriptor.getParameterTypes();
            parameterSize = method.descriptor.getParameterSize();
        } catch (InvalidDescriptorException e) {
            returnType = "???";
            parameterTypes = "???";
        }

        indent(1);
        Set<String> modifiers = method.access_flags.getMethodModifiers();
        print(String.join(" ", modifiers) + " " + returnType);

        String methodName = method.getName();
        switch (methodName) {
            case "<init>" -> methodName = cf.thisClassName();
            case "<clinit>" -> print("{}");
        }
        print(" " + methodName + parameterTypes);

        String exceptionString = "";
        ExceptionsAttribute exceptions = method.attributes.getAttribute(ExceptionsAttribute.class);
        if (exceptions != null) {
            print(" throws ");
            ConstantClassInfo[] ccis = exceptions.getExceptions();
            exceptionString = String.join(", ",
                    Arrays.stream(ccis).map(e -> ClassFileUtils.getJavaName(e.getName())).toList()
            ) + ";";
            print(exceptionString);
        }
        println();

        indent(1);
        println("descriptor: " + method.descriptor.descriptor);

        AccessFlags accessFlags = method.access_flags;
        println("flags: " + "(" + String.format("0x%04x", accessFlags.flags) + ")" + " " +
                String.join(", ", accessFlags.getMethodFlags()));
        println("Code:");
        CodeAttribute attr = method.attributes.getAttribute(CodeAttribute.class);

        indent(1);
        println("stack=" + attr.max_stack +
                ", locals=" + attr.max_locals +
                ", args_size=" + parameterSize);
        indent(-1);

        codeWriter.write(attr, cf.constant_pool);

        if (exceptions != null) {
            println("Exceptions:");
            indent(1);
            println("throws " + exceptionString);
            indent(-1);
        }

        indent(-2);
    }

    private void writeMethods(ClassFile cf) {
        Methods methods = cf.methods;
        methods.forEach((idx, method) -> {
            writeMethod(cf, method);
            if (idx != methods.length() - 1) println();
        });
    }

    public void write(ClassFile cf) {
        writeClassfile(cf);
        writeClassInfo(cf);
        writeConstantPool(cf);

        println("{");
        writeFields(cf);
        writeMethods(cf);
        println("}");

        println(String.format("SourceFile: \"%s\"", getSourceFileName(cf)));
    }
}
