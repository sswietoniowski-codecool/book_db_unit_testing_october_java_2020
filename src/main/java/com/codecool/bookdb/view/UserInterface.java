package com.codecool.bookdb.view;

import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Date;
import java.util.Scanner;

public class UserInterface {
    Scanner scanner;
    PrintStream out;

    public UserInterface(InputStream in, PrintStream out) {
        this.scanner = new Scanner(in);
        this.out = out;
    }

    public void println(Object obj) {
        out.println(obj);
    }

    public void printTitle(String title) {
        out.println("\n -- " + title + " --");
    }

    public void printOption(char option, String description) {
        out.println("(" + option + ")" + " " + description);
    }

    public char choice(String options) {
        // Given string options -> "abcd"
        // keep asking user for input until one of provided chars is provided
        throw new RuntimeException("Implement me!");
    }

    public String readString(String prompt, String defaultValue) {
        // Ask user for data. If no data was provided use default value.
        // User must be informed what the default value is.
        throw new RuntimeException("Implement me!");
    }

    public Date readDate(String prompt, Date defaultValue) {
        // Ask user for a date. If no data was provided use default value.
        // User must be informed what the default value is.
        // If provided date is in invalid format, ask user again.
        throw new RuntimeException("Implement me!");
    }

    public int readInt(String prompt, int defaultValue) {
        // Ask user for a number. If no data was provided use default value.
        // User must be informed what the default value is.
        throw new RuntimeException("Implement me!");
    }

}
