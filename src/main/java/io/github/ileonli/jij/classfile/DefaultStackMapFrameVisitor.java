package io.github.ileonli.jij.classfile;

import io.github.ileonli.jij.classfile.attribute.StackMapTableAttribute;

import java.util.Arrays;
import java.util.List;

public class DefaultStackMapFrameVisitor implements StackMapFrameVisitor<List<String>> {
    @Override
    public List<String> visit_same_frame(StackMapTableAttribute.same_frame frame) {
        return List.of();
    }

    @Override
    public List<String> visit_same_locals_1_stack_item_frame(StackMapTableAttribute.same_locals_1_stack_item_frame frame) {
        StackMapTableAttribute.VerificationTypeInfo[] stack = frame.stack;
        return List.of();
    }

    @Override
    public List<String> visit_same_locals_1_stack_item_frame_extended(StackMapTableAttribute.same_locals_1_stack_item_frame_extended frame) {
        // TODO: unfinished
        return List.of();
    }

    @Override
    public List<String> visit_chop_frame(StackMapTableAttribute.chop_frame frame) {
        return List.of("offset_delta = " + frame.offset_delta);
    }

    @Override
    public List<String> visit_same_frame_extended(StackMapTableAttribute.same_frame_extended frame) {
        return List.of("offset_delta = " + frame.offset_delta);
    }

    @Override
    public List<String> visit_append_frame(StackMapTableAttribute.append_frame frame) {
        String offset_delta = "offset_delta = " + frame.offset_delta;
        List<String> tags = Arrays.stream(frame.locals)
                .map(StackMapTableAttribute.VerificationTypeInfo::tagToString).toList();
        String locals = "locals = " + "[ " + String.join(", ", tags) + "]";
        return List.of(offset_delta, locals);
    }

    @Override
    public List<String> visit_full_frame(StackMapTableAttribute.full_frame frame) {
        // TODO: unfinished
        return List.of();
    }
}
