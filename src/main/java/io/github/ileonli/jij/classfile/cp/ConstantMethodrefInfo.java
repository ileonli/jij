package io.github.ileonli.jij.classfile.cp;

import io.github.ileonli.jij.classfile.ClassReader;
import io.github.ileonli.jij.classfile.ConstantPool;
import io.github.ileonli.jij.classfile.ConstantPoolInfo;
import io.github.ileonli.jij.classfile.exception.InvalidIndexException;

import java.io.IOException;

public class ConstantMethodrefInfo extends ConstantPoolInfo {
    public final int class_index;
    public final int name_and_type_index;

    public ConstantMethodrefInfo(ConstantPool cp, ClassReader cr) throws IOException {
        super(cp);
        class_index = cr.readUnsignedShort();
        name_and_type_index = cr.readUnsignedShort();
    }

    public ConstantClassInfo getClassInfo() throws InvalidIndexException {
        return (ConstantClassInfo) cp.getConstantPoolInfo(class_index);
    }

    public ConstantNameAndTypeInfo getNameAndTypeInfo() throws InvalidIndexException {
        return (ConstantNameAndTypeInfo) cp.getConstantPoolInfo(name_and_type_index);
    }

    @Override
    public int getTag() {
        return ConstantPool.CONSTANT_Methodref;
    }

    @Override
    public String toString() {
        return "CONSTANT_Methodref_info{" +
                "class_index=" + class_index +
                ", name_and_type_index=" + name_and_type_index +
                '}';
    }
}