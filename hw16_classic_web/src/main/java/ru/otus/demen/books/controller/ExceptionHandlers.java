package ru.otus.demen.books.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.demen.books.service.exception.IllegalParameterException;

@ControllerAdvice
public class ExceptionHandlers {
    @ExceptionHandler(IllegalParameterException.class)
    public ModelAndView handleIllegalParameterException(IllegalParameterException e) {
        ModelAndView modelAndView = new ModelAndView("error400");
        modelAndView.addObject("errorType", "Ошибка пользовательского ввода");
        modelAndView.addObject("errorMessage", e.getMessage());
        return modelAndView;
    }
}
