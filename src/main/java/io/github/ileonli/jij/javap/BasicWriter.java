package io.github.ileonli.jij.javap;

import java.io.PrintWriter;

public abstract class BasicWriter {
    public static final int INDEX_WIDTH = 2;
    public static final int TAB_COLUMN = 40;

    private final int indentWidth;
    private int indentCount;

    private final StringBuilder buffer;
    private final PrintWriter writer;

    public BasicWriter() {
        this(INDEX_WIDTH);
    }

    public BasicWriter(int indentWidth) {
        this(indentWidth, 0);
    }

    public BasicWriter(int indentWidth, int indentCount) {
        this.indentWidth = indentWidth;
        this.indentCount = indentCount;

        this.buffer = new StringBuilder();
        this.writer = new PrintWriter(System.out, true);
    }

    public void space(int n) {
        buffer.append(" ".repeat(Math.max(0, n)));
    }

    public void indent(int delta) {
        indentCount += delta;
        if (indentCount < 0) {
            throw new IllegalStateException("Indentation should always bigger than 0");
        }
    }

    private void indent() {
        space(indentCount * indentWidth);
    }

    public void tab() {
        if (buffer.isEmpty()) indent();
        space(indentCount * indentWidth + TAB_COLUMN - buffer.length());
    }

    public void error(String err) {
        print("???" + err);
    }

    public void print(long n) {
        print(String.valueOf(n));
    }

    public void print(String s) {
        if (s == null) s = "null";
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\n') {
                println();
            } else {
                if (buffer.isEmpty()) indent();
                buffer.append(c);
            }
        }
    }

    public void println(long n) {
        println(String.valueOf(n));
    }

    public void println(String s) {
        print(s);
        println();
    }

    public void println(Object o) {
        print(o == null ? null : o.toString());
        println();
    }

    public void println() {
        // remove pending spaces
        int length = buffer.length();
        int spaceStart = length, spaceEnd = length;
        for (int i = length - 1; i >= 0; i--) {
            char c = buffer.charAt(i);
            if (c == '\n') {
                spaceEnd--;
            } else if (c == ' ') {
                spaceStart = i;
            } else {
                break;
            }
        }
        if (spaceEnd < spaceStart) {
            buffer.delete(spaceStart, spaceEnd + 1);
        }

        writer.println(buffer);
        buffer.setLength(0);
    }
}
