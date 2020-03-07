package ru.otus.demen.books.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.demen.books.service.exception.AlreadyExistsException;
import ru.otus.demen.books.service.exception.IllegalParameterException;

@ControllerAdvice
public class ExceptionHandlers {
    private ModelAndView createModelAndViewError400(String s, String message) {
        ModelAndView modelAndView = new ModelAndView("client_error");
        modelAndView.addObject("errorType", s);
        modelAndView.addObject("errorMessage", message);
        return modelAndView;
    }

    @ExceptionHandler(IllegalParameterException.class)
    public ModelAndView handleIllegalParameterException(IllegalParameterException e) {
        return createModelAndViewError400("Ошибка пользовательского ввода", e.getMessage());
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ModelAndView handleAlreadyExistsException(AlreadyExistsException e) {
        return createModelAndViewError400("Введенная информация дублирует уже существующую", e.getMessage());
    }
}
