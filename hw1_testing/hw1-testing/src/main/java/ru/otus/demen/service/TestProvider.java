package ru.otus.demen.service;

import ru.otus.demen.model.OneTest;

import java.util.List;

interface TestProvider {
    List<OneTest> getTests();
}
