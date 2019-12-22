package ru.otus.demen.books.service;

public class ServiceError extends Exception {
    public ServiceError(String message) {
        super(message);
    }

    public ServiceError(String message, Throwable throwable) {
        super(message, throwable);
    }
}
