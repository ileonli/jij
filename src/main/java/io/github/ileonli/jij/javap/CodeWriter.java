package io.github.ileonli.jij.javap;

import io.github.ileonli.jij.classfile.*;
import io.github.ileonli.jij.classfile.attribute.CodeAttribute;
import io.github.ileonli.jij.classfile.attribute.LineNumberTableAttribute;
import io.github.ileonli.jij.classfile.attribute.LocalVariableTableAttribute;
import io.github.ileonli.jij.classfile.attribute.StackMapTableAttribute;

import java.util.List;

public class CodeWriter extends BasicWriter {
    public void writeInstr(Instruction instr, ConstantPool cp) {
        print(String.format("%4d: %-13s ", instr.PC(), instr.opcode.toMnemonic()));
        // compute the number of indentations for the body of multi-line instructions
        // This is 6 (the width of "%4d: "), divided by the width of each indentation level,
        // and rounded up to the next integer.
        int indentWidth = 4;
        int indent = (6 + indentWidth - 1) / indentWidth;

        DefaultInstructionVisitor div = new DefaultInstructionVisitor(
                new DefaultConstantPoolVisitor(cp, true), indent);

        instr.accept(new InstructionVisitor<>() {
            @Override
            public Void visitNoOperands(Instruction instr) {
                return null;
            }

            @Override
            public Void visitArrayType(Instruction instr, Instruction.TypeKind kind) {
                print(" " + div.visitArrayType(instr, kind));
                return null;
            }

            @Override
            public Void visitBranch(Instruction instr, int offset) {
                print(div.visitBranch(instr, offset));
                return null;
            }

            @Override
            public Void visitConstantPoolRef(Instruction instr, int index) {
                print("#" + index);
                tab();
                print("// ");
                print(div.visitConstantPoolRef(instr, index));
                return null;
            }

            @Override
            public Void visitConstantPoolRefAndValue(Instruction instr, int index, int value) {
                print("#" + index + ",  " + value);
                tab();
                print("// ");
                print(div.visitConstantPoolRefAndValue(instr, index, value));
                return null;
            }

            @Override
            public Void visitLocal(Instruction instr, int index) {
                print(div.visitLocal(instr, index));
                return null;
            }

            @Override
            public Void visitLocalAndValue(Instruction instr, int index, int value) {
                print(div.visitLocalAndValue(instr, index, value));
                return null;
            }

            @Override
            public Void visitLookupSwitch(Instruction instr, int default_, int npairs, int[] matches, int[] offsets) {
                print(div.visitLookupSwitch(instr, default_, npairs, matches, offsets));
                return null;
            }

            @Override
            public Void visitTableSwitch(Instruction instr, int default_, int low, int high, int[] offsets) {
                print(div.visitTableSwitch(instr, default_, low, high, offsets));
                return null;
            }

            @Override
            public Void visitValue(Instruction instr, int value) {
                print(div.visitValue(instr, value));
                return null;
            }

            @Override
            public Void visitUnknown(Instruction instr) {
                return null;
            }
        });
        println();
    }

    public void writeLineNumberTable(LineNumberTableAttribute lnta) {
        println("LineNumberTable:");
        for (var table : lnta.line_number_tables) {
            indent(1);
            println("line " + table.line_number + ": " + table.start_pc);
            indent(-1);
        }
    }

    private void writeStackMapTable(StackMapTableAttribute smta) {
        if (smta == null) {
            return;
        }

        DefaultStackMapFrameVisitor dsmfv = new DefaultStackMapFrameVisitor();

        println("StackMapTable: number_of_entries = " + smta.number_of_entries);
        for (StackMapTableAttribute.StackMapFrame e : smta.entries) {
            indent(1);
            println("frame_type = " + e.frame_type + " /* " + e.name() + " */");
            indent(1);
            List<String> list = e.accept(dsmfv);
            if (list != null) {
                for (String s : list) {
                    println(s);
                }
            }
            indent(-1);
            indent(-1);
        }
    }

    private void writeLocalVariableTable(LocalVariableTableAttribute lvta, ConstantPool cp) {
        if (lvta == null) {
            return;
        }

        println("LocalVariableTable:");
        LocalVariableTableAttribute.LocalVariableTable[] tables = lvta.local_variable_tables;

        indent(1);
        println("Start  Length  Slot  Name   Signature");
        for (LocalVariableTableAttribute.LocalVariableTable table : tables) {
            println(String.format("%5d %7d %5d %5s   %s",
                    table.start_pc, table.length, table.index,
                    cp.getUtf8Value(table.name_index), cp.getUtf8Value(table.descriptor_index)));
        }
        indent(-1);
    }

    public void write(CodeAttribute code, ConstantPool cp) {
        indent(3);
        List<Instruction> instructions = code.toInstructions();
        for (Instruction instruction : instructions) {
            writeInstr(instruction, cp);
        }

        Attributes attributes = code.attributes;

        LineNumberTableAttribute lnta = attributes.getAttribute(LineNumberTableAttribute.class);
        writeLineNumberTable(lnta);

        StackMapTableAttribute smta = attributes.getAttribute(StackMapTableAttribute.class);
        writeStackMapTable(smta);

        LocalVariableTableAttribute lvta = attributes.getAttribute(LocalVariableTableAttribute.class);
        writeLocalVariableTable(lvta, cp);

        indent(-3);
    }
}
