package io.github.ileonli.jij.classfile;

import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class ClassFileTestUtils {
    public static Path createJavaFile(TemporaryFolder folder, String className, String code) throws IOException {
        Path filePath = Paths.get(className + ".java");
        File file = folder.newFile(String.valueOf(filePath));
        try (var writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(code);
        }
        return Path.of(file.getAbsolutePath());
    }

    public static void compileJavaFile(Path javaFilePath, Path outputDir) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("javac", javaFilePath.toString(), "-d", outputDir.toString());
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        try {
            process.waitFor();
            if (process.exitValue() != 0) {
                InputStream inputStream = process.getInputStream();
                String result = new BufferedReader(new InputStreamReader(inputStream))
                        .lines().collect(Collectors.joining(System.lineSeparator()));
                throw new RuntimeException("Process exited with code " + process.exitValue() + "\n" + result);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static Path createTestClass(TemporaryFolder folder, String className, String code) throws IOException {
        Path javaFile = createJavaFile(folder, className, code);

        Path outputDir = javaFile.getParent();
        compileJavaFile(javaFile, outputDir);
        String compiledClassPath = className.replace(".", "/") + ".class";
        return outputDir.resolve(compiledClassPath);
    }
}
