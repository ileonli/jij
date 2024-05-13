package io.github.ileonli.jij.classfile;

public class DefaultInstructionVisitor implements InstructionVisitor<String> {
    private final DefaultConstantPoolVisitor cpVisitor;
    private final int indentWidth;

    public DefaultInstructionVisitor(DefaultConstantPoolVisitor cpVisitor, int indentWidth) {
        this.cpVisitor = cpVisitor;
        this.indentWidth = indentWidth;
    }

    @Override
    public String visitNoOperands(Instruction instr) {
        return "";
    }

    @Override
    public String visitArrayType(Instruction instr, Instruction.TypeKind kind) {
        return kind.name;
    }

    @Override
    public String visitBranch(Instruction instr, int offset) {
        return String.valueOf(instr.PC() + offset);
    }

    @Override
    public String visitConstantPoolRef(Instruction instr, int index) {
        return cpVisitor.visitConstantPoolInfo(index,
                (info) -> info.tagName() + " " + info.accept(cpVisitor));
    }

    @Override
    public String visitConstantPoolRefAndValue(Instruction instr, int index, int value) {
        return cpVisitor.visitConstantPoolInfo(index,
                (info) -> info.tagName() + " " + info.accept(cpVisitor));
    }

    @Override
    public String visitLocal(Instruction instr, int index) {
        return String.valueOf(index);
    }

    @Override
    public String visitLocalAndValue(Instruction instr, int index, int value) {
        return index + ", " + value;
    }

    @Override
    public String visitLookupSwitch(Instruction instr, int default_, int npairs, int[] matches, int[] offsets) {
        int pc = instr.PC();
        StringBuilder sb = new StringBuilder();
        sb.append("{ // ").append(npairs);
        for (int i = 0; i < npairs; i++) {
            sb.append(" ".repeat(indentWidth)).append(String.format("%n%12d: %d", matches[i], (pc + offsets[i])));
        }
        sb.append("\n").append(" ".repeat(5)).append("default: ").append(pc + default_).append("\n}");
        return sb.toString();
    }

    @Override
    public String visitTableSwitch(Instruction instr, int default_, int low, int high, int[] offsets) {
        int pc = instr.PC();
        StringBuilder sb = new StringBuilder();
        sb.append("{ // ").append(low).append(" to ").append(high);
        for (int i = 0; i < offsets.length; i++) {
            sb.append(" ".repeat(indentWidth)).append(String.format("%n%12d: %d", (low + i), (pc + offsets[i])));
        }
        sb.append("\n").append(" ".repeat(5)).append("default: ").append(pc + default_).append("\n}");
        return sb.toString();
    }

    @Override
    public String visitValue(Instruction instr, int value) {
        return String.valueOf(value);
    }

    @Override
    public String visitUnknown(Instruction instr) {
        return "";
    }
}
