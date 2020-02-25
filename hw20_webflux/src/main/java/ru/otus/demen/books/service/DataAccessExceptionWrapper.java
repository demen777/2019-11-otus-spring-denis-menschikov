package ru.otus.demen.books.service;

import org.springframework.dao.DataAccessException;
import ru.otus.demen.books.service.exception.DataAccessServiceException;

public class DataAccessExceptionWrapper {
    public static Throwable wrapDataAccessException(String message, Throwable error) {
        return error instanceof DataAccessException
                ? new DataAccessServiceException(message, error)
                : error;
    }
}
