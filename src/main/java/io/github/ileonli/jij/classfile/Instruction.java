package io.github.ileonli.jij.classfile;

public class Instruction {
    public enum Kind {
        /**
         * Opcode is not followed by any operands.
         */
        NO_OPERANDS(1),
        /**
         * Opcode is followed by a byte indicating a type.
         */
        ATYPE(2),
        /**
         * Opcode is followed by a 2-byte branch offset.
         */
        BRANCH(3),
        /**
         * Opcode is followed by a 4-byte branch offset.
         */
        BRANCH_W(5),
        /**
         * Opcode is followed by a signed byte value.
         */
        BYTE(2),
        /**
         * Opcode is followed by a 1-byte index into the constant pool.
         */
        CPREF(2),
        /**
         * Opcode is followed by a 2-byte index into the constant pool.
         */
        CPREF_W(3),
        /**
         * Opcode is followed by a 2-byte index into the constant pool,
         * an unsigned byte value.
         */
        CPREF_W_UBYTE(4),
        /**
         * Opcode is followed by a 2-byte index into the constant pool.,
         * an unsigned byte value, and a zero byte.
         */
        CPREF_W_UBYTE_ZERO(5),
        /**
         * Opcode is followed by variable number of operands, depending
         * on the instruction.
         */
        DYNAMIC(-1),
        /**
         * Opcode is followed by a 1-byte reference to a local variable.
         */
        LOCAL(2),
        /**
         * Opcode is followed by a 1-byte reference to a local variable,
         * and a signed byte value.
         */
        LOCAL_BYTE(3),
        /**
         * Opcode is followed by a signed short value.
         */
        SHORT(3),
        /**
         * Wide opcode is not followed by any operands.
         */
        WIDE_NO_OPERANDS(2),
        /**
         * Wide opcode is followed by a 2-byte index into the local variables array.
         */
        WIDE_LOCAL(4),
        /**
         * Wide opcode is followed by a 2-byte index into the constant pool.
         */
        WIDE_CPREF_W(4),
        /**
         * Wide opcode is followed by a 2-byte index into the constant pool,
         * and a signed short value.
         */
        WIDE_CPREF_W_SHORT(6),
        /**
         * Wide opcode is followed by a 2-byte reference to a local variable,
         * and a signed short value.
         */
        WIDE_LOCAL_SHORT(6),
        /**
         * Opcode was not recognized.
         */
        UNKNOWN(1);

        public final int length;

        Kind(int length) {
            this.length = length;
        }
    }

    public enum TypeKind {
        T_BOOLEAN(4, "boolean"),
        T_CHAR(5, "char"),
        T_FLOAT(6, "float"),
        T_DOUBLE(7, "double"),
        T_BYTE(8, "byte"),
        T_SHORT(9, "short"),
        T_INT(10, "int"),
        T_LONG(11, "long");

        public final int value;
        public final String name;

        TypeKind(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public static TypeKind valueFrom(int value) {
            return switch (value) {
                case 4 -> T_BOOLEAN;
                case 5 -> T_CHAR;
                case 6 -> T_FLOAT;
                case 7 -> T_DOUBLE;
                case 8 -> T_BYTE;
                case 9 -> T_SHORT;
                case 10 -> T_INT;
                case 11 -> T_LONG;
                default -> null;
            };
        }
    }

    private final byte[] codes;
    private final int pc;

    public final Opcode opcode;

    public Instruction(byte[] codes, int pc) {
        this.codes = codes;
        this.pc = pc;

        this.opcode = Opcode.valueFrom(codes[pc]);
    }

    public int readByte() {
        return readByte(1);
    }

    public int readByte(int offset) {
        return codes[pc + offset];
    }

    public int readUnsignedByte() {
        return readByte(1) & 0xff;
    }

    public int readUnsignedByte(int offset) {
        return readByte(offset) & 0xff;
    }

    public int readShort() {
        return (readByte(1) << 8) | readUnsignedByte(2);
    }

    public int readShort(int offset) {
        return (readByte(offset) << 8) | readUnsignedByte(offset + 1);
    }

    public int readUnsignedShort() {
        return readShort(1) & 0xFFFF;
    }

    public int readUnsignedShort(int offset) {
        return readShort(offset) & 0xFFFF;
    }

    public int readInt() {
        return (readShort(1) << 16) | (readUnsignedShort(3));
    }

    public int readInt(int offset) {
        return (readShort(offset) << 16) | (readUnsignedShort(offset + 2));
    }

    public int PC() {
        return this.pc;
    }

    public <R> R accept(InstructionVisitor<R> visitor) {
        return switch (opcode.kind) {
            case NO_OPERANDS, WIDE_NO_OPERANDS -> visitor.visitNoOperands(this);
            case ATYPE -> visitor.visitArrayType(this, TypeKind.valueFrom(readUnsignedByte()));
            case BRANCH -> visitor.visitBranch(this, readShort());
            case BRANCH_W -> visitor.visitBranch(this, readInt());
            case BYTE -> visitor.visitValue(this, readByte());
            case CPREF -> visitor.visitConstantPoolRef(this, readUnsignedByte());
            case CPREF_W -> visitor.visitConstantPoolRef(this, readUnsignedShort());
            case CPREF_W_UBYTE, CPREF_W_UBYTE_ZERO ->
                    visitor.visitConstantPoolRefAndValue(this, readUnsignedByte(), readByte());
            case DYNAMIC -> {
                switch (opcode) {
                    case TABLESWITCH -> {
                        int pad = align(PC() + 1) - PC();
                        int default_ = readInt(pad);
                        int low = readInt(pad + 4);
                        int high = readInt(pad + 8);
                        if (low > high)
                            throw new IllegalStateException();
                        int[] values = new int[high - low + 1];
                        for (int i = 0; i < values.length; i++)
                            values[i] = readInt(pad + 12 + 4 * i);
                        yield visitor.visitTableSwitch(this, default_, low, high, values);
                    }
                    case LOOKUPSWITCH -> {
                        int pad = align(PC() + 1) - PC();
                        int default_ = readInt(pad);
                        int npairs = readInt(pad + 4);
                        if (npairs < 0)
                            throw new IllegalStateException();
                        int[] matches = new int[npairs];
                        int[] offsets = new int[npairs];
                        for (int i = 0; i < npairs; i++) {
                            matches[i] = readInt(pad + 8 + i * 8);
                            offsets[i] = readInt(pad + 12 + i * 8);
                        }
                        yield visitor.visitLookupSwitch(this, default_, npairs, matches, offsets);
                    }
                    default -> throw new IllegalStateException();
                }
            }
            case LOCAL -> visitor.visitLocal(this, readUnsignedByte());
            case LOCAL_BYTE -> visitor.visitLocalAndValue(this, readUnsignedByte(), readByte(2));
            case SHORT -> visitor.visitValue(this, readShort());
            case WIDE_LOCAL -> visitor.visitLocal(this, readUnsignedShort(2));
            case WIDE_CPREF_W -> visitor.visitConstantPoolRef(this, readUnsignedShort(2));
            case WIDE_CPREF_W_SHORT -> visitor.visitConstantPoolRefAndValue(
                    this, readUnsignedShort(2), readUnsignedByte(4));
            case WIDE_LOCAL_SHORT -> visitor.visitLocalAndValue(
                    this, readUnsignedShort(2), readShort(4));

            case UNKNOWN -> visitor.visitUnknown(this);
        };
    }

    private static int align(int n) {
        return (n + 3) & ~3;
    }

    public int length() {
        if (opcode == null)
            return 1;

        switch (opcode) {
            case TABLESWITCH -> {
                int pad = align(pc + 1) - pc;
                int low = readInt(pad + 4);
                int high = readInt(pad + 8);
                return pad + 12 + 4 * (high - low + 1);
            }
            case LOOKUPSWITCH -> {
                int pad = align(pc + 1) - pc;
                int npairs = readInt(pad + 4);
                return pad + 8 + 8 * npairs;
            }
            default -> {
                return opcode.kind.length;
            }
        }
    }
}
