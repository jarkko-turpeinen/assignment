package com.library.rest;

import java.text.DateFormat;

/**
 * Logging inside bluemix cloud is by default system output driven
 */
public final class Logger {

    private static void writeLog(String details) {
        System.out.println(details);
    }

    /**
     * Write logging details to system output
     *
     * @param details log description
     */
    public static void info(String details) { writeLog("<info> " + details); }

    /**
     * Write debugging details to system output
     *
     * @param details debug description
     */
    public static void debug(String details) {
        writeLog("<debug> "+ details);
    }

    /**
     * Write error details to system output
     *
     * @param details error description
     */
    public static void error(String details) {
        writeLog( "<error> " + details);
    }
}
