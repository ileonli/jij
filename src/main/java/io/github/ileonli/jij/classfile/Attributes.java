package io.github.ileonli.jij.classfile;

import io.github.ileonli.jij.classfile.attribute.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Attributes {

    // @formatter:off
    public static final String AnnotationDefault       = "AnnotationDefault";
    public static final String BootstrapMethods        = "BootstrapMethods";
    public static final String CharacterRangeTable     = "CharacterRangeTable";
    public static final String Code                    = "Code";
    public static final String ConstantValue           = "ConstantValue";
    public static final String CompilationID           = "CompilationID";
    public static final String Deprecated              = "Deprecated";
    public static final String EnclosingMethod         = "EnclosingMethod";
    public static final String Exceptions              = "Exceptions";
    public static final String InnerClasses            = "InnerClasses";
    public static final String LineNumberTable         = "LineNumberTable";
    public static final String LocalVariableTable      = "LocalVariableTable";
    public static final String LocalVariableTypeTable  = "LocalVariableTypeTable";
    public static final String MethodParameters        = "MethodParameters";
    public static final String Module                  = "Module";
    public static final String ModuleHashes            = "ModuleHashes";
    public static final String ModuleMainClass         = "ModuleMainClass";
    public static final String ModulePackages          = "ModulePackages";
    public static final String ModuleResolution        = "ModuleResolution";
    public static final String ModuleTarget            = "ModuleTarget";
    public static final String NestHost                = "NestHost";
    public static final String NestMembers             = "NestMembers";
    public static final String Record                  = "Record";
    public static final String RuntimeVisibleAnnotations             = "RuntimeVisibleAnnotations";
    public static final String RuntimeInvisibleAnnotations           = "RuntimeInvisibleAnnotations";
    public static final String RuntimeVisibleParameterAnnotations    = "RuntimeVisibleParameterAnnotations";
    public static final String RuntimeInvisibleParameterAnnotations  = "RuntimeInvisibleParameterAnnotations";
    public static final String RuntimeVisibleTypeAnnotations         = "RuntimeVisibleTypeAnnotations";
    public static final String RuntimeInvisibleTypeAnnotations       = "RuntimeInvisibleTypeAnnotations";
    public static final String PermittedSubclasses   = "PermittedSubclasses";
    public static final String Signature             = "Signature";
    public static final String SourceDebugExtension  = "SourceDebugExtension";
    public static final String SourceFile            = "SourceFile";
    public static final String SourceID              = "SourceID";
    public static final String StackMap              = "StackMap";
    public static final String StackMapTable         = "StackMapTable";
    public static final String Synthetic             = "Synthetic";
    // @formatter:on

    public final Attribute[] attributes;
    public final Map<Class<?>, Attribute> map;

    private final ConstantPool cp;

    public Attributes(ClassReader cr) throws IOException {
        cp = cr.classFile.constant_pool;

        int attributes_count = cr.readUnsignedShort();
        map = new HashMap<>(attributes_count);
        attributes = new Attribute[attributes_count];
        for (int i = 0; i < attributes_count; i++) {
            int attribute_name_index = cr.readUnsignedShort();
            String type = cp.getUtf8Value(attribute_name_index);
            Attribute attribute = getAttribute(cr, type);
            attributes[i] = attribute;
            map.put(attribute.getClass(), attributes[i]);
        }
    }

    private static Attribute getAttribute(ClassReader cr, String type) throws IOException {
        return switch (type) {
            case Code -> new CodeAttribute(cr);
            case ConstantValue -> new ConstantValueAttribute(cr);
            case Exceptions -> new ExceptionsAttribute(cr);
            case InnerClasses -> new InnerClassesAttribute(cr);
            case LineNumberTable -> new LineNumberTableAttribute(cr);
            case Signature -> new SignatureAttribute(cr);
            case SourceFile -> new SourceFileAttribute(cr);
            case StackMapTable -> new StackMapTableAttribute(cr);
            default -> throw new IllegalArgumentException("Illegal attribute type: " + type);
        };
    }

    public <T> T getAttribute(Class<T> type) {
        Attribute attribute = map.get(type);
        if (attribute == null) {
            return null;
        }
        return type.cast(attribute);
    }

    public int length() {
        return attributes.length;
    }
}
