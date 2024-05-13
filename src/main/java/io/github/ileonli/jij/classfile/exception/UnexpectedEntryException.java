package io.github.ileonli.jij.classfile.exception;

public class UnexpectedEntryException extends ConstantPoolException {
    private final int expectedTag;
    private final int actualTag;

    public UnexpectedEntryException(int index, int expectedTag, int actualTag) {
        super(index);
        this.expectedTag = expectedTag;
        this.actualTag = actualTag;
    }

    @Override
    public String getMessage() {
        return "Unexpected entry at index: " + index + ", expected: " + expectedTag + ", actual: " + actualTag;
    }
}