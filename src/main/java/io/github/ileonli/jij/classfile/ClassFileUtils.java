package io.github.ileonli.jij.classfile;

import java.util.Objects;

public class ClassFileUtils {
    /* If name is a valid binary name, return it; otherwise quote it. */
    public static String checkName(String name) {
        if (name == null)
            return "null";

        int len = name.length();
        if (len == 0)
            return "\"\"";

        int cc = '/';
        int cp;
        for (int k = 0; k < len; k += Character.charCount(cp)) {
            cp = name.codePointAt(k);
            if ((cc == '/' && !Character.isJavaIdentifierStart(cp))
                    || (cp != '/' && !Character.isJavaIdentifierPart(cp))) {
                return "\"" + addEscapes(name) + "\"";
            }
            cc = cp;
        }
        return name;
    }

    /* If name requires escapes, put them in, so it can be a string body. */
    public static String addEscapes(String name) {
        String esc = "\\\"\n\t";
        String rep = "\\\"nt";
        StringBuilder buf = null;
        int nextk = 0;
        int len = name.length();
        for (int k = 0; k < len; k++) {
            char cp = name.charAt(k);
            int n = esc.indexOf(cp);
            if (n >= 0) {
                if (buf == null)
                    buf = new StringBuilder(len * 2);
                if (nextk < k)
                    buf.append(name, nextk, k);
                buf.append('\\');
                buf.append(rep.charAt(n));
                nextk = k + 1;
            }
        }
        if (buf == null)
            return name;
        if (nextk < len)
            buf.append(name, nextk, len);
        return buf.toString();
    }

    public static String getJavaName(String name) {
        Objects.requireNonNull(name);
        return name.replace('/', '.');
    }
}
