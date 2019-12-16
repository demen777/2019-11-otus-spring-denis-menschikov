package ru.otus.demen.service;


class CorruptedTestFileError extends Error {
    CorruptedTestFileError(Throwable e) {
        super(e);
    }
}
