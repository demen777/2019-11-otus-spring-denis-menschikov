package ru.otus.demen.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Test {
    private final String question;
    private final String expectedAnswer;
}
