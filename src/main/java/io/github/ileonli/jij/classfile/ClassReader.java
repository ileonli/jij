package io.github.ileonli.jij.classfile;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ClassReader {
    private final DataInputStream in;

    public ClassReader(InputStream in) {
        this.in = new DataInputStream(new BufferedInputStream(in));
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
