package io.github.ileonli.jij.classfile.attribute;

import io.github.ileonli.jij.classfile.Attribute;
import io.github.ileonli.jij.classfile.ClassReader;

import java.io.IOException;

public class StackMapTableAttribute extends Attribute {
    private final int number_of_entries;
    private final StackMapFrame[] entries;

    public StackMapTableAttribute(ClassReader cr) throws IOException {
        super(cr);
        number_of_entries = cr.readUnsignedShort();
        entries = new StackMapFrame[number_of_entries];
        for (int i = 0; i < number_of_entries; i++) {
            entries[i] = StackMapTableAttribute.parseStackMapFrame(cr);
        }
    }

    public static VerificationTypeInfo parseVerificationTypeInfo(ClassReader cr) throws IOException {
        int frame_type = cr.readUnsignedByte();
        return switch (frame_type) {
            case VerificationTypeInfo.ITEM_Top, VerificationTypeInfo.ITEM_Integer, VerificationTypeInfo.ITEM_Float,
                 VerificationTypeInfo.ITEM_Long, VerificationTypeInfo.ITEM_Double, VerificationTypeInfo.ITEM_Null,
                 VerificationTypeInfo.ITEM_UninitializedThis -> new VerificationTypeInfo(frame_type);
            case VerificationTypeInfo.ITEM_Object -> new ObjectVariableInfo(cr);
            case VerificationTypeInfo.ITEM_Uninitialized -> new UninitializedVariableInfo(cr);
            default -> throw new IllegalArgumentException("Unrecognized verification_type_info tag: " + frame_type);
        };
    }

    public static StackMapFrame parseStackMapFrame(ClassReader cr) throws IOException {
        int frame_type = cr.readUnsignedByte();
        if (frame_type <= 63) return new same_frame(frame_type);
        else if (frame_type <= 127) return new same_locals_1_stack_item_frame(frame_type, cr);
        else if (frame_type <= 246) throw new IllegalArgumentException("Unknown frame_type: " + frame_type);
        else if (frame_type == 247) return new same_locals_1_stack_item_frame_extended(frame_type, cr);
        else if (frame_type <= 250) return new chop_frame(frame_type, cr);
        else if (frame_type == 251) return new same_frame_extended(frame_type, cr);
        else if (frame_type <= 254) return new append_frame(frame_type, cr);
        else return new full_frame(frame_type, cr);
    }

    // ----------------------------------------------------------------------------

    public static class VerificationTypeInfo {

        public static final int ITEM_Top = 0;
        public static final int ITEM_Integer = 1;
        public static final int ITEM_Float = 2;
        public static final int ITEM_Long = 4;
        public static final int ITEM_Double = 3;
        public static final int ITEM_Null = 5;
        public static final int ITEM_UninitializedThis = 6;
        public static final int ITEM_Object = 7;
        public static final int ITEM_Uninitialized = 8;

        public final int tag;

        public VerificationTypeInfo(int tag) throws IOException {
            this.tag = tag;
        }
    }

    public static class ObjectVariableInfo extends VerificationTypeInfo {
        public final int cpool_index;

        public ObjectVariableInfo(ClassReader cr) throws IOException {
            super(ITEM_Object);
            cpool_index = cr.readUnsignedShort();
        }
    }

    public static class UninitializedVariableInfo extends VerificationTypeInfo {
        public final int offset;

        public UninitializedVariableInfo(ClassReader cr) throws IOException {
            super(ITEM_Uninitialized);
            offset = cr.readUnsignedShort();
        }
    }

    // ----------------------------------------------------------------------------

    public static class StackMapFrame {
        public final int frame_type;

        public StackMapFrame(int frame_type) throws IOException {
            this.frame_type = frame_type;
        }
    }

    public static class same_frame extends StackMapFrame {
        public same_frame(int frame_type) throws IOException {
            super(frame_type);
        }
    }

    public static class same_locals_1_stack_item_frame extends StackMapFrame {
        public final VerificationTypeInfo[] stack;

        public same_locals_1_stack_item_frame(int frame_type, ClassReader cr) throws IOException {
            super(frame_type);
            stack = new VerificationTypeInfo[1];
            stack[0] = StackMapTableAttribute.parseVerificationTypeInfo(cr);
        }
    }

    public static class same_locals_1_stack_item_frame_extended extends StackMapFrame {
        public final int offset_delta;
        public final VerificationTypeInfo[] stack;

        public same_locals_1_stack_item_frame_extended(int frame_type, ClassReader cr) throws IOException {
            super(frame_type);
            offset_delta = cr.readUnsignedShort();
            stack = new VerificationTypeInfo[1];
            stack[0] = StackMapTableAttribute.parseVerificationTypeInfo(cr);
        }
    }

    public static class chop_frame extends StackMapFrame {
        public final int offset_delta;

        public chop_frame(int frame_type, ClassReader cr) throws IOException {
            super(frame_type);
            offset_delta = cr.readUnsignedShort();
        }
    }

    public static class same_frame_extended extends StackMapFrame {
        public final int offset_delta;

        public same_frame_extended(int frame_type, ClassReader cr) throws IOException {
            super(frame_type);
            offset_delta = cr.readUnsignedShort();
        }
    }

    public static class append_frame extends StackMapFrame {
        public final int offset_delta;
        public final VerificationTypeInfo[] locals;

        public append_frame(int frame_type, ClassReader cr) throws IOException {
            super(frame_type);
            offset_delta = cr.readUnsignedShort();
            locals = new VerificationTypeInfo[frame_type - 251];
            for (int i = 0; i < frame_type - 251; i++) {
                locals[i] = StackMapTableAttribute.parseVerificationTypeInfo(cr);
            }
        }
    }

    public static class full_frame extends StackMapFrame {
        public final int offset_delta;
        public final int number_of_locals;
        public final VerificationTypeInfo[] locals;
        public final int number_of_stack_items;
        public final VerificationTypeInfo[] stack;

        public full_frame(int frame_type, ClassReader cr) throws IOException {
            super(frame_type);
            offset_delta = cr.readUnsignedShort();
            number_of_locals = cr.readUnsignedShort();

            locals = new VerificationTypeInfo[number_of_locals];
            for (int i = 0; i < number_of_locals; i++) {
                locals[i] = StackMapTableAttribute.parseVerificationTypeInfo(cr);
            }

            number_of_stack_items = cr.readUnsignedShort();
            stack = new VerificationTypeInfo[number_of_stack_items];
            for (int i = 0; i < number_of_stack_items; i++) {
                stack[i] = StackMapTableAttribute.parseVerificationTypeInfo(cr);
            }
        }
    }
}
