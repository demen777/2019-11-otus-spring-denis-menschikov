package ru.otus.demen.books.controller;

class ControllerTestUtils {
    @SuppressWarnings("SameParameterValue")
    static String createApiExceptionJson(String error, String message) {
        return String.format("{\"error\": \"%s\", \"message\": \"%s\"}", error, message);
    }
}
