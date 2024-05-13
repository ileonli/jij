package io.github.ileonli.jij.classfile.exception;

public class ConstantPoolException extends ClassFileException {
    protected final int index;

    public ConstantPoolException(int index) {
        this.index = index;
    }
}
