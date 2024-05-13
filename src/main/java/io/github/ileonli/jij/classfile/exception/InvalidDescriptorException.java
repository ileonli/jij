package io.github.ileonli.jij.classfile.exception;

public class InvalidDescriptorException extends DescriptorException {
    private final String desc;
    private final int index;

    public InvalidDescriptorException(String desc) {
        this.desc = desc;
        this.index = -1;
    }
    public InvalidDescriptorException(String desc, int index) {
        this.desc = desc;
        this.index = index;
    }

    @Override
    public String getMessage() {
        if (index == -1) return "Invalid descriptor " + desc;
        else return "Descriptor is invalid at offset " + index + " in " + desc;
    }
}
