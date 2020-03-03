package ru.otus.demen.books.security;

import ru.otus.demen.books.model.Book;

public interface BookAclCreator {
    void createAcl(Book book);
}
