package io.github.ileonli.jij.classfile.attribute;

import io.github.ileonli.jij.classfile.Attribute;
import io.github.ileonli.jij.classfile.Attributes;
import io.github.ileonli.jij.classfile.ClassReader;
import io.github.ileonli.jij.classfile.Instruction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CodeAttribute extends Attribute {
    public final int max_stack;
    public final int max_locals;
    public final int code_length;
    public final byte[] code;
    public final int exception_table_length;
    public final ExceptionTable[] exception_table;
    public final Attributes attributes;

    public CodeAttribute(ClassReader cr) throws IOException {
        super(cr);
        max_stack = cr.readUnsignedShort();
        max_locals = cr.readUnsignedShort();
        code_length = cr.readInt();
        code = cr.readBytes(code_length);
        exception_table_length = cr.readUnsignedShort();
        exception_table = new ExceptionTable[exception_table_length];
        for (int i = 0; i < exception_table_length; i++) {
            exception_table[i] = new ExceptionTable(cr);
        }
        attributes = new Attributes(cr);
    }

    public static class ExceptionTable {
        public final int start_pc;
        public final int end_pc;
        public final int handler_pc;
        public final int catch_type;

        public ExceptionTable(ClassReader cr) throws IOException {
            start_pc = cr.readUnsignedShort();
            end_pc = cr.readUnsignedShort();
            handler_pc = cr.readUnsignedShort();
            catch_type = cr.readUnsignedShort();
        }
    }

    public List<Instruction> toInstructions() {
        List<Instruction> instructions = new ArrayList<>();
        int idx = 0;
        while (idx < code_length) {
            Instruction next = idx < code.length ? new Instruction(code, idx) : null;
            if (next == null) {
                break;
            }
            instructions.add(next);
            idx += next.length();
        }
        return instructions;
    }
}
