package io.github.ileonli.jij.javap;

import io.github.ileonli.jij.classfile.AccessFlags;
import io.github.ileonli.jij.classfile.ClassFile;
import io.github.ileonli.jij.classfile.Method;
import io.github.ileonli.jij.classfile.Methods;
import io.github.ileonli.jij.classfile.attribute.SourceFileAttribute;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Set;

public class ClassWriter extends BasicWriter {
    private final ConstantPoolWriter poolWriter;

    public ClassWriter(ClassFile cf) {
        this(cf, 0);
    }

    public ClassWriter(ClassFile cf, int indentWidth) {
        super(INDEX_WIDTH, indentWidth);
        this.poolWriter = new ConstantPoolWriter(cf.constant_pool);
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

        SourceFileAttribute sourceFileAttribute = cf.getAttribute(SourceFileAttribute.class);
        println("Compiled from " + sourceFileAttribute.getSourceFileName());
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

    private void writeMethods(ClassFile cf) {
        println("{");
        Methods methods = cf.methods;

        MethodWriter methodWriter = new MethodWriter(cf);
        methods.forEach((idx, method) -> {
            methodWriter.write(method);
            if (idx != methods.length() - 1) println();
        });
        println("}");
    }

    public String getJavaName(String name) {
        return name.replace('/', '.');
    }

    public void write(ClassFile cf) {
        writeClassfile(cf);
        writeClassInfo(cf);
        writeConstantPool(cf);
        writeMethods(cf);
    }
}
