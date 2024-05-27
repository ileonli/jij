package io.github.ileonli.jij.classfile.attribute;

import io.github.ileonli.jij.classfile.Attribute;
import io.github.ileonli.jij.classfile.ClassReader;

import java.io.IOException;

public class LocalVariableTypeTableAttribute extends Attribute {
    public final int local_variable_type_table_length;
    public final LocalVariableTypeTable[] local_variable_type_table;

    public LocalVariableTypeTableAttribute(ClassReader cr) throws IOException {
        super(cr);
        local_variable_type_table_length = cr.readUnsignedShort();
        local_variable_type_table = new LocalVariableTypeTable[local_variable_type_table_length];
        for (int i = 0; i < local_variable_type_table_length; i++) {
            local_variable_type_table[i] = new LocalVariableTypeTable(cr);
        }
    }

    public static class LocalVariableTypeTable {
        public final int start_pc;
        public final int length;
        public final int name_index;
        public final int signature_index;
        public final int index;

        public LocalVariableTypeTable(ClassReader cr) throws IOException {
            start_pc = cr.readUnsignedShort();
            length = cr.readUnsignedShort();
            name_index = cr.readUnsignedShort();
            signature_index = cr.readUnsignedShort();
            index = cr.readUnsignedShort();
        }
    }
}
