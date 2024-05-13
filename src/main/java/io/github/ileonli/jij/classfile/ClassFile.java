package io.github.ileonli.jij.classfile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static io.github.ileonli.jij.classfile.AccessFlags.ACC_INTERFACE;

public class ClassFile {
    public final Path path;

    public final int magic;
    public final int minor_version;
    public final int major_version;
    public final ConstantPool constant_pool;
    public final AccessFlags access_flags;
    public final int this_class;
    public final int super_class;
    public final Interfaces interfaces;
    public final Fields fields;
    public final Methods methods;
    public final Attributes attributes;

    public ClassFile(Path path) throws IOException {
        this(Files.newInputStream(path), path);
    }

    public ClassFile(InputStream in, Path path) throws IOException {
        Objects.requireNonNull(in);
        Objects.requireNonNull(path);
        this.path = path;

        ClassReader cr = new ClassReader(in, this);

        magic = cr.readInt();
        minor_version = cr.readUnsignedShort();
        major_version = cr.readUnsignedShort();
        constant_pool = new ConstantPool(cr);
        access_flags = new AccessFlags(cr);
        this_class = cr.readUnsignedShort();
        super_class = cr.readUnsignedShort();
        interfaces = new Interfaces(cr);
        fields = new Fields(cr);
        methods = new Methods(cr);
        attributes = new Attributes(cr);
    }

    public static ClassFile read(String path) throws IOException {
        return read(Path.of(path));
    }

    public static ClassFile read(Path input) throws IOException {
        return new ClassFile(input);
    }

    public <T> T getAttribute(Class<T> clazz) {
        return attributes.getAttribute(clazz);
    }

    public boolean isClass() {
        return !isInterface();
    }

    public boolean isInterface() {
        return access_flags.is(ACC_INTERFACE);
    }

    public String thisClassName() {
        return constant_pool.getClassInfo(this_class).getName();
    }

    public String superClassName() {
        return constant_pool.getClassInfo(super_class).getName();
    }
}
