package io.github.ileonli.jij.classfile;

import java.util.HashMap;
import java.util.Map;

import static io.github.ileonli.jij.classfile.Instruction.Kind.*;

public enum Opcode {
    NOP(0x0),
    ACONST_NULL(0x1),
    ICONST_M1(0x2),
    ICONST_0(0x3),
    ICONST_1(0x4),
    ICONST_2(0x5),
    ICONST_3(0x6),
    ICONST_4(0x7),
    ICONST_5(0x8),
    LCONST_0(0x9),
    LCONST_1(0xa),
    FCONST_0(0xb),
    FCONST_1(0xc),
    FCONST_2(0xd),
    DCONST_0(0xe),
    DCONST_1(0xf),
    BIPUSH(0x10, BYTE),
    SIPUSH(0x11, SHORT),
    LDC(0x12, CPREF),
    LDC_W(0x13, CPREF_W),
    LDC2_W(0x14, CPREF_W),
    ILOAD(0x15, LOCAL),
    LLOAD(0x16, LOCAL),
    FLOAD(0x17, LOCAL),
    DLOAD(0x18, LOCAL),
    ALOAD(0x19, LOCAL),
    ILOAD_0(0x1a),
    ILOAD_1(0x1b),
    ILOAD_2(0x1c),
    ILOAD_3(0x1d),
    LLOAD_0(0x1e),
    LLOAD_1(0x1f),
    LLOAD_2(0x20),
    LLOAD_3(0x21),
    FLOAD_0(0x22),
    FLOAD_1(0x23),
    FLOAD_2(0x24),
    FLOAD_3(0x25),
    DLOAD_0(0x26),
    DLOAD_1(0x27),
    DLOAD_2(0x28),
    DLOAD_3(0x29),
    ALOAD_0(0x2a),
    ALOAD_1(0x2b),
    ALOAD_2(0x2c),
    ALOAD_3(0x2d),
    IALOAD(0x2e),
    LALOAD(0x2f),
    FALOAD(0x30),
    DALOAD(0x31),
    AALOAD(0x32),
    BALOAD(0x33),
    CALOAD(0x34),
    SALOAD(0x35),
    ISTORE(0x36, LOCAL),
    LSTORE(0x37, LOCAL),
    FSTORE(0x38, LOCAL),
    DSTORE(0x39, LOCAL),
    ASTORE(0x3a, LOCAL),
    ISTORE_0(0x3b),
    ISTORE_1(0x3c),
    ISTORE_2(0x3d),
    ISTORE_3(0x3e),
    LSTORE_0(0x3f),
    LSTORE_1(0x40),
    LSTORE_2(0x41),
    LSTORE_3(0x42),
    FSTORE_0(0x43),
    FSTORE_1(0x44),
    FSTORE_2(0x45),
    FSTORE_3(0x46),
    DSTORE_0(0x47),
    DSTORE_1(0x48),
    DSTORE_2(0x49),
    DSTORE_3(0x4a),
    ASTORE_0(0x4b),
    ASTORE_1(0x4c),
    ASTORE_2(0x4d),
    ASTORE_3(0x4e),
    IASTORE(0x4f),
    LASTORE(0x50),
    FASTORE(0x51),
    DASTORE(0x52),
    AASTORE(0x53),
    BASTORE(0x54),
    CASTORE(0x55),
    SASTORE(0x56),
    POP(0x57),
    POP2(0x58),
    DUP(0x59),
    DUP_X1(0x5a),
    DUP_X2(0x5b),
    DUP2(0x5c),
    DUP2_X1(0x5d),
    DUP2_X2(0x5e),
    SWAP(0x5f),
    IADD(0x60),
    LADD(0x61),
    FADD(0x62),
    DADD(0x63),
    ISUB(0x64),
    LSUB(0x65),
    FSUB(0x66),
    DSUB(0x67),
    IMUL(0x68),
    LMUL(0x69),
    FMUL(0x6a),
    DMUL(0x6b),
    IDIV(0x6c),
    LDIV(0x6d),
    FDIV(0x6e),
    DDIV(0x6f),
    IREM(0x70),
    LREM(0x71),
    FREM(0x72),
    DREM(0x73),
    INEG(0x74),
    LNEG(0x75),
    FNEG(0x76),
    DNEG(0x77),
    ISHL(0x78),
    LSHL(0x79),
    ISHR(0x7a),
    LSHR(0x7b),
    IUSHR(0x7c),
    LUSHR(0x7d),
    IAND(0x7e),
    LAND(0x7f),
    IOR(0x80),
    LOR(0x81),
    IXOR(0x82),
    LXOR(0x83),
    IINC(0x84, LOCAL_BYTE),
    I2L(0x85),
    I2F(0x86),
    I2D(0x87),
    L2I(0x88),
    L2F(0x89),
    L2D(0x8a),
    F2I(0x8b),
    F2L(0x8c),
    F2D(0x8d),
    D2I(0x8e),
    D2L(0x8f),
    D2F(0x90),
    I2B(0x91),
    I2C(0x92),
    I2S(0x93),
    LCMP(0x94),
    FCMPL(0x95),
    FCMPG(0x96),
    DCMPL(0x97),
    DCMPG(0x98),
    IFEQ(0x99, BRANCH),
    IFNE(0x9a, BRANCH),
    IFLT(0x9b, BRANCH),
    IFGE(0x9c, BRANCH),
    IFGT(0x9d, BRANCH),
    IFLE(0x9e, BRANCH),
    IF_ICMPEQ(0x9f, BRANCH),
    IF_ICMPNE(0xa0, BRANCH),
    IF_ICMPLT(0xa1, BRANCH),
    IF_ICMPGE(0xa2, BRANCH),
    IF_ICMPGT(0xa3, BRANCH),
    IF_ICMPLE(0xa4, BRANCH),
    IF_ACMPEQ(0xa5, BRANCH),
    IF_ACMPNE(0xa6, BRANCH),
    GOTO(0xa7, BRANCH),
    JSR(0xa8, BRANCH),
    RET(0xa9, LOCAL),
    TABLESWITCH(0xaa, DYNAMIC),
    LOOKUPSWITCH(0xab, DYNAMIC),
    IRETURN(0xac),
    LRETURN(0xad),
    FRETURN(0xae),
    DRETURN(0xaf),
    ARETURN(0xb0),
    RETURN(0xb1),
    GETSTATIC(0xb2, CPREF_W),
    PUTSTATIC(0xb3, CPREF_W),
    GETFIELD(0xb4, CPREF_W),
    PUTFIELD(0xb5, CPREF_W),
    INVOKEVIRTUAL(0xb6, CPREF_W),
    INVOKESPECIAL(0xb7, CPREF_W),
    INVOKESTATIC(0xb8, CPREF_W),
    INVOKEINTERFACE(0xb9, CPREF_W_UBYTE_ZERO),
    INVOKEDYNAMIC(0xba, CPREF_W_UBYTE_ZERO),
    NEW(0xbb, CPREF_W),
    NEWARRAY(0xbc, ATYPE),
    ANEWARRAY(0xbd, CPREF_W),
    ARRAYLENGTH(0xbe),
    ATHROW(0xbf),
    CHECKCAST(0xc0, CPREF_W),
    INSTANCEOF(0xc1, CPREF_W),
    MONITORENTER(0xc2),
    MONITOREXIT(0xc3),
    // wide 0xc4
    MULTIANEWARRAY(0xc5, CPREF_W_UBYTE),
    IFNULL(0xc6, BRANCH),
    IFNONNULL(0xc7, BRANCH),
    GOTO_W(0xc8, BRANCH_W),
    JSR_W(0xc9, BRANCH_W);

    public final int opcode;
    public final Instruction.Kind kind;

    Opcode(int opcode) {
        this.opcode = opcode;
        this.kind = NO_OPERANDS;
    }

    Opcode(int opcode, Instruction.Kind kind) {
        this.opcode = opcode;
        this.kind = kind;
    }

    private static final Map<Byte, Opcode> opcodeMap = new HashMap<>();

    static {
        for (Opcode value : Opcode.values()) {
            opcodeMap.put((byte) value.opcode, value);
        }
    }

    public static Opcode valueFrom(byte opcode) {
        return opcodeMap.get(opcode);
    }

    public String toMnemonic() {
        return this.toString().toLowerCase();
    }
}