package com.library.rest;

import java.text.DateFormat;

public final class Logger {

    private static void writeLog(String details) {
        System.out.print("<" + DateFormat.getDateTimeInstance().format(System.currentTimeMillis()) + "> ");
        System.out.println(details);
    }

    /**
     * Write logging details to system output
     *
     * @param details
     */
    public static void info(String details) { writeLog("<info> " + details); }

    /**
     * Write debugging details to system output
     *
     * @param details
     */
    public static void debug(String details) {
        writeLog("<debug> "+ details);
    }

    /**
     * Write error details to system output
     *
     * @param details
     */
    public static void error(String details) {
        writeLog( "<error> " + details);
    }
}
