package ru.otus.demen.books.service.exception;

public abstract class ServiceException extends RuntimeException {
    protected ServiceException(String message) {
        super(message);
    }
    protected ServiceException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
