package ru.otus.demen.books.controller;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ResultOk {
    public static final ResultOk INSTANCE = new ResultOk();
    private final String result;
    private ResultOk() { result = "OK"; }
}
