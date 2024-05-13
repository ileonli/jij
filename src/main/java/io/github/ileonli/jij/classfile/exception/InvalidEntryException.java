package io.github.ileonli.jij.classfile.exception;

public class InvalidEntryException extends ConstantPoolException {
    public final int tag;

    InvalidEntryException(int index, int tag) {
        super(index);
        this.tag = tag;
    }

    @Override
    public String getMessage() {
        return "Unexpected entry at #" + index + ": " + tag;
    }
}