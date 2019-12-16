package ru.otus.demen.dao;


class CorruptedTestFileError extends Error {
    CorruptedTestFileError(Throwable e) {
        super(e);
    }
}
