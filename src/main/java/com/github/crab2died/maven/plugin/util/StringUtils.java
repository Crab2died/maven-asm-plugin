package com.github.crab2died.maven.plugin.util;


public final class StringUtils {

    private StringUtils() {
    }

    public static boolean isNoneBlank(CharSequence... c) {
        if (null == c || c.length == 0) return false;
        for (CharSequence ch : c) {
            if (null == ch || "".equals(ch)) return false;
        }
        return true;
    }

    public static boolean isBlank(CharSequence c) {
        return null == c || "".equals(c);
    }

    public static String join(Object... c) {
        if (null == c || c.length == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (Object ch : c) {
            sb.append(ch);
        }
        return sb.toString();
    }

}
