package io.github.ileonli.jij.classfile;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

public class AccessFlags {

    // @formatter:off
    public static final int ACC_PUBLIC        = 0x0001; // class, inner, field, method
    public static final int ACC_PRIVATE       = 0x0002; //        inner, field, method
    public static final int ACC_PROTECTED     = 0x0004; //        inner, field, method
    public static final int ACC_STATIC        = 0x0008; //        inner, field, method
    public static final int ACC_FINAL         = 0x0010; // class, inner, field, method
    public static final int ACC_SUPER         = 0x0020; // class
    public static final int ACC_SYNCHRONIZED  = 0x0020; //                      method
    public static final int ACC_VOLATILE      = 0x0040; //               field
    public static final int ACC_BRIDGE        = 0x0040; //                      method
    public static final int ACC_TRANSIENT     = 0x0080; //               field
    public static final int ACC_VARARGS       = 0x0080; //                      method
    public static final int ACC_NATIVE        = 0x0100; //                      method
    public static final int ACC_INTERFACE     = 0x0200; // class, inner
    public static final int ACC_ABSTRACT      = 0x0400; // class, inner,        method
    public static final int ACC_STRICT        = 0x0800; //                      method
    public static final int ACC_SYNTHETIC     = 0x1000; // class, inner, field, method
    public static final int ACC_ANNOTATION    = 0x2000; // class, inner
    public static final int ACC_ENUM          = 0x4000; // class, inner, field
    public static final int ACC_MANDATED      = 0x8000; //                          method parameter
    public static final int ACC_MODULE        = 0x8000; // class
    // @formatter:on

    public enum Kind {Class, InnerClass, Field, Method}

    private static final int[] classModifiers = {
            ACC_PUBLIC, ACC_FINAL, ACC_ABSTRACT
    };

    private static final int[] classFlags = {
            ACC_PUBLIC, ACC_FINAL, ACC_SUPER, ACC_INTERFACE, ACC_ABSTRACT,
            ACC_SYNTHETIC, ACC_ANNOTATION, ACC_ENUM, ACC_MODULE
    };

    public Set<String> getClassModifiers() {
        int f = ((flags & ACC_INTERFACE) != 0 ? flags & ~ACC_ABSTRACT : flags);
        return getModifiers(f, classModifiers, Kind.Class);
    }

    public Set<String> getClassFlags() {
        return getFlags(classFlags, Kind.Class);
    }

    private static final int[] innerClassModifiers = {
            ACC_PUBLIC, ACC_PRIVATE, ACC_PROTECTED, ACC_STATIC, ACC_FINAL,
            ACC_ABSTRACT
    };

    private static final int[] innerClassFlags = {
            ACC_PUBLIC, ACC_PRIVATE, ACC_PROTECTED, ACC_STATIC, ACC_FINAL, ACC_SUPER,
            ACC_INTERFACE, ACC_ABSTRACT, ACC_SYNTHETIC, ACC_ANNOTATION, ACC_ENUM
    };

    public Set<String> getInnerClassModifiers() {
        int f = ((flags & ACC_INTERFACE) != 0 ? flags & ~ACC_ABSTRACT : flags);
        return getModifiers(f, innerClassModifiers, Kind.InnerClass);
    }

    public Set<String> getInnerClassFlags() {
        return getFlags(innerClassFlags, Kind.InnerClass);
    }

    private static final int[] fieldModifiers = {
            ACC_PUBLIC, ACC_PRIVATE, ACC_PROTECTED, ACC_STATIC, ACC_FINAL,
            ACC_VOLATILE, ACC_TRANSIENT
    };

    private static final int[] fieldFlags = {
            ACC_PUBLIC, ACC_PRIVATE, ACC_PROTECTED, ACC_STATIC, ACC_FINAL,
            ACC_VOLATILE, ACC_TRANSIENT, ACC_SYNTHETIC, ACC_ENUM
    };

    public Set<String> getFieldModifiers() {
        return getModifiers(fieldModifiers, Kind.Field);
    }

    public Set<String> getFieldFlags() {
        return getFlags(fieldFlags, Kind.Field);
    }

    private static final int[] methodModifiers = {
            ACC_PUBLIC, ACC_PRIVATE, ACC_PROTECTED, ACC_STATIC, ACC_FINAL,
            ACC_SYNCHRONIZED, ACC_NATIVE, ACC_ABSTRACT, ACC_STRICT
    };

    private static final int[] methodFlags = {
            ACC_PUBLIC, ACC_PRIVATE, ACC_PROTECTED, ACC_STATIC, ACC_FINAL,
            ACC_SYNCHRONIZED, ACC_BRIDGE, ACC_VARARGS, ACC_NATIVE, ACC_ABSTRACT,
            ACC_STRICT, ACC_SYNTHETIC
    };

    public final Integer flags;

    public AccessFlags(ClassReader cr) throws IOException {
        flags = cr.readUnsignedShort();
    }

    public static String flagToModifier(int flag, Kind t) {
        return switch (flag) {
            case ACC_PUBLIC -> "public";
            case ACC_PRIVATE -> "private";
            case ACC_PROTECTED -> "protected";
            case ACC_STATIC -> "static";
            case ACC_FINAL -> "final";
            case ACC_SYNCHRONIZED -> "synchronized";
            case 0x80 -> (t == Kind.Field ? "transient" : null);
            case ACC_VOLATILE -> "volatile";
            case ACC_NATIVE -> "native";
            case ACC_ABSTRACT -> "abstract";
            case ACC_STRICT -> "strictfp";
            case ACC_MANDATED -> "mandated";
            default -> null;
        };
    }

    public static String flagToName(int flag, Kind t) {
        return switch (flag) {
            case ACC_PUBLIC -> "ACC_PUBLIC";
            case ACC_PRIVATE -> "ACC_PRIVATE";
            case ACC_PROTECTED -> "ACC_PROTECTED";
            case ACC_STATIC -> "ACC_STATIC";
            case ACC_FINAL -> "ACC_FINAL";
            case 0x20 -> (t == Kind.Class ? "ACC_SUPER" : "ACC_SYNCHRONIZED");
            case 0x40 -> (t == Kind.Field ? "ACC_VOLATILE" : "ACC_BRIDGE");
            case 0x80 -> (t == Kind.Field ? "ACC_TRANSIENT" : "ACC_VARARGS");
            case ACC_NATIVE -> "ACC_NATIVE";
            case ACC_INTERFACE -> "ACC_INTERFACE";
            case ACC_ABSTRACT -> "ACC_ABSTRACT";
            case ACC_STRICT -> "ACC_STRICT";
            case ACC_SYNTHETIC -> "ACC_SYNTHETIC";
            case ACC_ANNOTATION -> "ACC_ANNOTATION";
            case ACC_ENUM -> "ACC_ENUM";
            case 0x8000 -> (t == Kind.Class ? "ACC_MODULE" : "ACC_MANDATED");
            default -> null;
        };
    }

    public Set<String> getMethodModifiers() {
        return getModifiers(methodModifiers, Kind.Method);
    }

    public Set<String> getMethodFlags() {
        return getFlags(methodFlags, Kind.Method);
    }

    private Set<String> getModifiers(int[] modifierFlags, Kind t) {
        return getModifiers(flags, modifierFlags, t);
    }

    private static Set<String> getModifiers(int flags, int[] modifierFlags, Kind t) {
        Set<String> s = new LinkedHashSet<>();
        for (int m : modifierFlags) {
            if ((flags & m) != 0)
                s.add(flagToModifier(m, t));
        }
        return s;
    }

    private Set<String> getFlags(int[] expectedFlags, Kind t) {
        Set<String> s = new LinkedHashSet<>();
        int f = flags;
        for (int e : expectedFlags) {
            if ((f & e) != 0) {
                s.add(flagToName(e, t));
                f = f & ~e;
            }
        }
        while (f != 0) {
            int bit = Integer.highestOneBit(f);
            s.add("0x" + Integer.toHexString(bit));
            f = f & ~bit;
        }
        return s;
    }

    public boolean is(int mask) {
        return (flags & mask) != 0;
    }
}
