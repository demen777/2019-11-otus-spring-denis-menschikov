package ru.otus.demen.books.service.exception;

public class DataAccessServiceException extends ServiceException {
    public DataAccessServiceException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
