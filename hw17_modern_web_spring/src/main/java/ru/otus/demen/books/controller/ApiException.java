package ru.otus.demen.books.controller;

import lombok.Value;

@Value
public class ApiException {
    public final String error;
    public final String message;
}
