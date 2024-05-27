package io.github.ileonli.jij.javap;

import io.github.ileonli.jij.classfile.ConstantPool;
import io.github.ileonli.jij.classfile.ConstantPoolInfo;
import io.github.ileonli.jij.classfile.ConstantPoolVisitor;
import io.github.ileonli.jij.classfile.DefaultConstantPoolVisitor;
import io.github.ileonli.jij.classfile.cp.*;

public class ConstantPoolWriter extends BasicWriter {
    private final ConstantPool cp;

    public ConstantPoolWriter(ConstantPool cp) {
        this.cp = cp;
    }

    public void writeInfo(ConstantPoolInfo info) {
        DefaultConstantPoolVisitor dcpv = new DefaultConstantPoolVisitor(cp);

        ConstantPoolVisitor<Void> v = new ConstantPoolVisitor<>() {
            @Override
            public Void visitUtf8(ConstantUtf8Info info) {
                println(dcpv.visitUtf8(info));
                return null;
            }

            @Override
            public Void visitInteger(ConstantIntegerInfo info) {
                println(dcpv.visitInteger(info));
                return null;
            }

            @Override
            public Void visitFloat(ConstantFloatInfo info) {
                println(dcpv.visitFloat(info));
                return null;
            }

            @Override
            public Void visitLong(ConstantLongInfo info) {
                println(dcpv.visitLong(info));
                return null;
            }

            @Override
            public Void visitDouble(ConstantDoubleInfo info) {
                println(dcpv.visitDouble(info));
                return null;
            }

            @Override
            public Void visitClass(ConstantClassInfo info) {
                print("#" + info.name_index);
                tab();
                println("// " + dcpv.visitClass(info));
                return null;
            }

            @Override
            public Void visitString(ConstantStringInfo info) {
                print("#" + info.string_index);
                tab();
                println("// " + dcpv.visitString(info));
                return null;
            }

            @Override
            public Void visitFieldref(ConstantFieldrefInfo info) {
                print("#" + info.class_index + ".#" + info.name_and_type_index);
                tab();
                println("// " + dcpv.visitFieldref(info));
                return null;
            }

            @Override
            public Void visitMethodref(ConstantMethodrefInfo info) {
                print("#" + info.class_index + ".#" + info.name_and_type_index);
                tab();
                println("// " + dcpv.visitMethodref(info));
                return null;
            }

            @Override
            public Void visitInterfaceMethodref(ConstantInterfaceMethodrefInfo info) {
                print("#" + info.class_index + ".#" + info.name_and_type_index);
                tab();
                println("// " + dcpv.visitInterfaceMethodref(info));
                return null;
            }

            @Override
            public Void visitNameAndType(ConstantNameAndTypeInfo info) {
                print("#" + info.name_index + ":#" + info.type_index);
                tab();
                println("// " + dcpv.visitNameAndType(info));
                return null;
            }

            @Override
            public Void visitMethodHandle(ConstantMethodHandleInfo info) {
                print(info.reference_kind.kind + ":#" + info.reference_index);
                tab();
                println("// " + dcpv.visitMethodHandle(info));
                return null;
            }

            @Override
            public Void visitMethodType(ConstantMethodTypeInfo info) {
                print("#" + info.descriptor_index);
                tab();
                println("//  " + dcpv.visitMethodType(info));
                return null;
            }

            @Override
            public Void visitDynamic(ConstantDynamicInfo info) {
                print("#" + info.bootstrap_method_attr_index + ":#" + info.name_and_type_index);
                tab();
                println("// " + dcpv.visitDynamic(info));
                return null;
            }

            @Override
            public Void visitInvokeDynamic(ConstantInvokeDynamicInfo info) {
                print("#" + info.bootstrap_method_attr_index + ":#" + info.name_and_type_index);
                tab();
                println("// " + dcpv.visitInvokeDynamic(info));
                return null;
            }

            @Override
            public Void visitModule(ConstantModuleInfo info) {
                print("#" + info.name_index);
                tab();
                println("// " + dcpv.visitModule(info));
                return null;
            }

            @Override
            public Void visitPackage(ConstantPackageInfo info) {
                print("#" + info.name_index);
                tab();
                println("// " + dcpv.visitPackage(info));
                return null;
            }
        };

        info.accept(v);
    }

    public void write(ConstantPool cp) {
        println("Constant pool:");
        indent(1);
        int width = String.valueOf(cp.length()).length() + 1;
        cp.forEach((idx, info) -> {
            print(String.format("%" + width + "s", ("#" + idx)));
            print(String.format(" = %-18s ", info.infoName()));
            writeInfo(info);
        });
        indent(-1);
    }
}
