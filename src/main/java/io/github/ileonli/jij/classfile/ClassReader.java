package io.github.ileonli.jij.classfile;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class ClassReader {
    private final DataInputStream in;
    public final ClassFile classFile;

    public ClassReader(InputStream in, ClassFile cf) {
        Objects.requireNonNull(in);
        Objects.requireNonNull(cf);
        this.in = new DataInputStream(new BufferedInputStream(in));
        this.classFile = cf;
    }

    public int readUnsignedByte() throws IOException {
        return in.readUnsignedByte();
    }

    public int readUnsignedShort() throws IOException {
        return in.readUnsignedShort();
    }

    public int readInt() throws IOException {
        return in.readInt();
    }

    public long readLong() throws IOException {
        return in.readLong();
    }

    public float readFloat() throws IOException {
        return in.readFloat();
    }

    public double readDouble() throws IOException {
        return in.readDouble();
    }

    public byte[] readBytes(int length) throws IOException {
        return in.readNBytes(length);
    }
}
