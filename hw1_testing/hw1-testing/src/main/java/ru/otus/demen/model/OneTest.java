package ru.otus.demen.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class OneTest {
    private final String question;
    private final String expectedAnswer;
}
