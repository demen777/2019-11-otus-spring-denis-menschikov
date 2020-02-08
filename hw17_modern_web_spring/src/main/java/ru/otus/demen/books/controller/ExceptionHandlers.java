package ru.otus.demen.books.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.demen.books.service.exception.AlreadyExistsException;
import ru.otus.demen.books.service.exception.IllegalParameterException;

@ControllerAdvice
public class ExceptionHandlers {
    private ResponseEntity<Object> createResponseEntity(String error_type, String message) {
        return new ResponseEntity<>(new ApiException(error_type, message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalParameterException.class)
    public ResponseEntity<Object> handleIllegalParameterException(IllegalParameterException e) {
        return createResponseEntity("Ошибка пользовательского ввода", e.getMessage());
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<Object> handleAlreadyExistsException(AlreadyExistsException e) {
        return createResponseEntity("Введенная информация дублирует уже существующую", e.getMessage());
    }
}
