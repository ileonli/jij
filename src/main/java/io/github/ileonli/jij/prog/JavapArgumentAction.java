package io.github.ileonli.jij.prog;

import io.github.ileonli.jij.classfile.ClassFile;
import io.github.ileonli.jij.javap.ClassWriter;
import net.sourceforge.argparse4j.inf.Argument;
import net.sourceforge.argparse4j.inf.ArgumentAction;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;

import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;

public class JavapArgumentAction implements ArgumentAction {
    @Override
    public void run(ArgumentParser parser, Argument arg,
                    Map<String, Object> attrs, String flag,
                    Object value) throws ArgumentParserException {
    }

    @Override
    public void run(ArgumentParser parser, Argument arg,
                    Map<String, Object> attrs, String flag,
                    Object value, Consumer<Object> valueSetter) throws ArgumentParserException {
        String path = (String) value;

        ClassFile cf;
        try {
            cf = ClassFile.read(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ClassWriter cw = new ClassWriter(cf);
        cw.write();
    }

    @Override
    public void onAttach(Argument arg) {
    }

    @Override
    public boolean consumeArgument() {
        return true;
    }
}
