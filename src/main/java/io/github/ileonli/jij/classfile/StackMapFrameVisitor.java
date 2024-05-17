package io.github.ileonli.jij.classfile;

import io.github.ileonli.jij.classfile.attribute.StackMapTableAttribute.*;

public interface StackMapFrameVisitor<R> {
    R visit_same_frame(same_frame frame);

    R visit_same_locals_1_stack_item_frame(same_locals_1_stack_item_frame frame);

    R visit_same_locals_1_stack_item_frame_extended(same_locals_1_stack_item_frame_extended frame);

    R visit_chop_frame(chop_frame frame);

    R visit_same_frame_extended(same_frame_extended frame);

    R visit_append_frame(append_frame frame);

    R visit_full_frame(full_frame frame);
}
