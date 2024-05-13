package io.github.ileonli.jij.classfile.attribute;

import io.github.ileonli.jij.classfile.Attribute;
import io.github.ileonli.jij.classfile.ClassReader;

import java.io.IOException;

public class LineNumberTableAttribute extends Attribute {
    public final int line_number_table_length;
    public final LineNumberTable[] line_number_tables;

    public LineNumberTableAttribute(ClassReader cr) throws IOException {
        super(cr);
        line_number_table_length = cr.readUnsignedShort();
        line_number_tables = new LineNumberTable[line_number_table_length];
        for (int i = 0; i < line_number_table_length; i++) {
            line_number_tables[i] = new LineNumberTable(cr);
        }
    }

    public static class LineNumberTable {
        public final int start_pc;
        public final int line_number;

        public LineNumberTable(ClassReader cr) throws IOException {
            start_pc = cr.readUnsignedShort();
            line_number = cr.readUnsignedShort();
        }
    }
}
