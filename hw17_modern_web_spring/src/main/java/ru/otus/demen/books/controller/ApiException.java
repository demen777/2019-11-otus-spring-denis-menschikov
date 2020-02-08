package ru.otus.demen.books.controller;

import lombok.Value;

@Value
public class ApiException {
    public final String error_type;
    public final String message;
}
