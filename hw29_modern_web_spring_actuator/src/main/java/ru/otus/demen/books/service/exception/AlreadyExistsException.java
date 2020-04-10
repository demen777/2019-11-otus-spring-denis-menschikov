package ru.otus.demen.books.service.exception;

public class AlreadyExistsException extends ServiceException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
