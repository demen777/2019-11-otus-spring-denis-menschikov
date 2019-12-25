package ru.otus.demen.books.helper;

import ru.otus.demen.books.service.ServiceError;

import java.util.function.Supplier;

public class HandleExceptionUtils {
    public static String getSupplierOrExceptionMessage(Supplier<String> supplier) {
        try {
            return supplier.get();
        } catch (ServiceError error) {
            return error.getMessage();
        }
    }
}