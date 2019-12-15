package ru.otus.demen.service;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class StreamIOService implements IOService {
    private final PrintStream printStream;
    private final Scanner scanner;

    public StreamIOService(InputStream inputStream, PrintStream printStream) {
        this.printStream = printStream;
        scanner = new Scanner(inputStream);
    }

    @Override
    public void println(String message) {
        printStream.println(message);
    }

    @Override
    public String getNextLine() {
        return scanner.nextLine();
    }
}
