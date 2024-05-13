package io.github.ileonli.jij.classfile.exception;

public class IllegalAttributeLengthException extends AttributeException {
    private final String attributeName;
    private final int attributeLength;

    public IllegalAttributeLengthException(String attributeName, int attributeLength) {
        this.attributeName = attributeName;
        this.attributeLength = attributeLength;
    }

    @Override
    public String getMessage() {
        return "Illegal attribute length: " + attributeLength + " for attribute: " + attributeName;
    }
}
