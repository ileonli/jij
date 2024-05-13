package io.github.ileonli.jij.classfile.exception;

public class InvalidIndexException extends ConstantPoolException {
    public InvalidIndexException(int index) {
        super(index);
    }

    @Override
    public String getMessage() {
        return "Invalid constant pool index: " + index;
    }
}