package ru.otus.demen.service;

import ru.otus.demen.model.Test;

import java.util.List;

interface TestProvider {
    List<Test> getTests();
}
