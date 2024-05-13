package io.github.ileonli.jij.classfile.attribute;

import io.github.ileonli.jij.classfile.Attribute;
import io.github.ileonli.jij.classfile.ClassReader;
import io.github.ileonli.jij.classfile.cp.ConstantClassInfo;

import java.io.IOException;

public class ExceptionsAttribute extends Attribute {
    public final int number_of_exceptions;
    public final int[] exception_index_table;

    public ExceptionsAttribute(ClassReader cr) throws IOException {
        super(cr);
        number_of_exceptions = cr.readUnsignedShort();
        exception_index_table = new int[number_of_exceptions];
        for (int i = 0; i < number_of_exceptions; i++) {
            exception_index_table[i] = cr.readUnsignedShort();
        }
    }

    public ConstantClassInfo[] getExceptions() {
        ConstantClassInfo[] ccis = new ConstantClassInfo[number_of_exceptions];
        for (int i = 0; i < number_of_exceptions; i++) {
            ccis[i] = cp.getConstantPoolInfo(exception_index_table[i], ConstantClassInfo.class);
        }
        return ccis;
    }
}
