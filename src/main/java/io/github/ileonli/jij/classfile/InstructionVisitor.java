package io.github.ileonli.jij.classfile;

public interface InstructionVisitor<R> {
    R visitNoOperands(Instruction instr);

    R visitArrayType(Instruction instr, Instruction.TypeKind kind);

    R visitBranch(Instruction instr, int offset);

    R visitConstantPoolRef(Instruction instr, int index);

    R visitConstantPoolRefAndValue(Instruction instr, int index, int value);

    R visitLocal(Instruction instr, int index);

    R visitLocalAndValue(Instruction instr, int index, int value);

    R visitLookupSwitch(Instruction instr, int default_, int npairs, int[] matches, int[] offsets);

    R visitTableSwitch(Instruction instr, int default_, int low, int high, int[] offsets);

    R visitValue(Instruction instr, int value);

    R visitUnknown(Instruction instr);
}
