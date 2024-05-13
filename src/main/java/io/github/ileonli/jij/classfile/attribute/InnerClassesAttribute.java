package io.github.ileonli.jij.classfile.attribute;

import io.github.ileonli.jij.classfile.AccessFlags;
import io.github.ileonli.jij.classfile.Attribute;
import io.github.ileonli.jij.classfile.ClassReader;

import java.io.IOException;

public class InnerClassesAttribute extends Attribute {
    public final Info[] classes;

    public InnerClassesAttribute(ClassReader cr) throws IOException {
        super(cr);
        int number_of_classes = cr.readUnsignedShort();
        classes = new Info[number_of_classes];
        for (int i = 0; i < number_of_classes; i++) {
            classes[i] = new Info(cr);
        }
    }

    public static class Info {
        public final int inner_class_info_index;
        public final int outer_class_info_index;
        public final int inner_name_index;
        public final AccessFlags inner_class_access_flags;

        public Info(ClassReader cr) throws IOException {
            inner_class_info_index = cr.readUnsignedShort();
            outer_class_info_index = cr.readUnsignedShort();
            inner_name_index = cr.readUnsignedShort();
            inner_class_access_flags = new AccessFlags(cr);
        }
    }
}
