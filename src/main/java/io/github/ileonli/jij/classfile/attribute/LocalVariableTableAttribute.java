package io.github.ileonli.jij.classfile.attribute;

import io.github.ileonli.jij.classfile.Attribute;
import io.github.ileonli.jij.classfile.ClassReader;

import java.io.IOException;

public class LocalVariableTableAttribute extends Attribute {
    public final int local_variable_table_length;
    public final LocalVariableTable[] local_variable_tables;

    public LocalVariableTableAttribute(ClassReader cr) throws IOException {
        super(cr);
        local_variable_table_length = cr.readUnsignedShort();
        local_variable_tables = new LocalVariableTable[local_variable_table_length];
        for (int i = 0; i < local_variable_table_length; i++) {
            local_variable_tables[i] = new LocalVariableTable(cr);
        }
    }

    public static class LocalVariableTable {
        public final int start_pc;
        public final int length;
        public final int name_index;
        public final int descriptor_index;
        public final int index;

        public LocalVariableTable(ClassReader cr) throws IOException {
            start_pc = cr.readUnsignedShort();
            length = cr.readUnsignedShort();
            name_index = cr.readUnsignedShort();
            descriptor_index = cr.readUnsignedShort();
            index = cr.readUnsignedShort();
        }
    }
}
