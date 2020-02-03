package ru.otus.demen.books.service.exception;

public class NotFoundException extends ServiceException {
    public NotFoundException(String message) {
        super(message);
    }
}
