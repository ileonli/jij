package io.github.ileonli.jij.classfile;

import io.github.ileonli.jij.classfile.cp.*;

public interface ConstantPoolVisitor<R> {
    R visitUtf8(ConstantUtf8Info info);

    R visitInteger(ConstantIntegerInfo info);

    R visitFloat(ConstantFloatInfo info);

    R visitLong(ConstantLongInfo info);

    R visitDouble(ConstantDoubleInfo info);

    R visitClass(ConstantClassInfo info);

    R visitString(ConstantStringInfo info);

    R visitFieldref(ConstantFieldrefInfo info);

    R visitMethodref(ConstantMethodrefInfo info);

    R visitInterfaceMethodref(ConstantInterfaceMethodrefInfo info);

    R visitNameAndType(ConstantNameAndTypeInfo info);

    R visitMethodHandle(ConstantMethodHandleInfo info);

    R visitMethodType(ConstantMethodTypeInfo info);

    R visitDynamic(ConstantDynamicInfo info);

    R visitInvokeDynamic(ConstantInvokeDynamicInfo info);

    R visitModule(ConstantModuleInfo info);

    R visitPackage(ConstantPackageInfo info);
}