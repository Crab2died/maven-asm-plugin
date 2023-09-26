package com.github.crab2died.maven.plugin.util;

public final class LogColor {

    private LogColor() {
    }

    public static final String DEBUG = "DEBUG";
    public static final String INFO = "INFO";
    public static final String WARN = "WARNING";
    public static final String ERROR = "ERROR";
    public static final String BOLD = "\u001B[1m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\033[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";
    public static final String REST = "\u001B[0m";

    public static final String LOG_DEBUG = "[" + BOLD + GREEN + DEBUG + REST + "] ";
    public static final String LOG_INFO = "[" + BOLD + BLUE + INFO + REST + "] ";
    public static final String LOG_WARN = "[" + BOLD + YELLOW + WARN + REST + "] ";
    public static final String LOG_ERROR = "[" + BOLD + RED + ERROR + REST + "] ";

    public static String bold(Object c) {
        return BOLD + c + REST;
    }

    public static String green(Object c) {
        return GREEN + c + REST;
    }

    public static String boldGreen(Object c) {
        return BOLD + GREEN + c + REST;
    }

    public static String blue(Object c) {
        return BLUE + c + REST;
    }

    public static String boldBlue(Object c) {
        return BOLD + BLUE + c + REST;
    }

    public static String yellow(Object c) {
        return YELLOW + c + REST;
    }

    public static String boldYellow(Object c) {
        return BOLD + YELLOW + c + REST;
    }

    public static String red(Object c) {
        return YELLOW + c + REST;
    }

    public static String boldRed(Object c) {
        return BOLD + YELLOW + c + REST;
    }

    public static String cyan(Object c) {
        return CYAN + c + REST;
    }

    public static String boldCyan(Object c) {
        return BOLD + CYAN + c + REST;
    }

}
