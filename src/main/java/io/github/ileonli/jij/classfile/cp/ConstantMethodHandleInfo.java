package io.github.ileonli.jij.classfile.cp;

import io.github.ileonli.jij.classfile.ClassReader;
import io.github.ileonli.jij.classfile.ConstantPool;
import io.github.ileonli.jij.classfile.ConstantPool.RefKind;
import io.github.ileonli.jij.classfile.ConstantPoolInfo;

import java.io.IOException;

public class ConstantMethodHandleInfo extends ConstantPoolInfo {
    public final RefKind reference_kind;
    public final int reference_index;

    public ConstantMethodHandleInfo(ConstantPool cp, ClassReader cr) throws IOException {
        super(cp);
        reference_kind = RefKind.valueFrom(cr.readUnsignedByte());
        reference_index = cr.readUnsignedShort();
    }


    @Override
    public int getTag() {
        return ConstantPool.CONSTANT_MethodHandle;
    }
}