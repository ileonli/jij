package io.github.ileonli.jij.classfile;

import io.github.ileonli.jij.javap.ClassWriter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.Path;

public class Test1 {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private final String className = "Test1";

    private final String code = String.format("""
            public class %s {
                public void newArray(int d) {
                    String[] ss = new String[d];
                    Object[] oo = new Object[d];
                    int[] ii = new int[d];
                }
                public int sum(int a, int b, int c, int d) {
                    return a + b + c + d;
                }
                public static void main(String[] args) throws Exception, NullPointerException {
                    int sum = 0;
                    for (int i = 0; i < 16; i++) {
                        sum += i;
                    }
                    if (sum == 100) {
                        System.out.println(sum);
                    }
                    throw new RuntimeException();
                }
            }
            """, className);

    @Test
    public void test() throws IOException {
        Path path = ClassFileTestUtils.createTestClass(folder, className, code);

        ClassFile cf = ClassFile.read(path);

        int magic = cf.magic;
        int major_version = cf.major_version;
        ConstantPool cp = cf.constant_pool;
        AccessFlags access_flags = cf.access_flags;
        int this_class = cf.this_class;
        int super_class = cf.super_class;
        Interfaces interfaces = cf.interfaces;
        Fields fields = cf.fields;
        Methods methods = cf.methods;
        Attributes attributes = cf.attributes;

        ClassWriter cw = new ClassWriter(cf);
        cw.write(cf);
    }
}
