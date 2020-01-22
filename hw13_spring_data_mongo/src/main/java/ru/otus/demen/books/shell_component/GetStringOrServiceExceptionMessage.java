package ru.otus.demen.books.shell_component;

import ru.otus.demen.books.service.exception.ServiceException;

import java.util.function.Supplier;

public class GetStringOrServiceExceptionMessage {
    public static String call(Supplier<String> stringSupplier) {
        try {
            return stringSupplier.get();
        }
        catch (ServiceException e) {
            return e.getMessage();
        }
    }
}
