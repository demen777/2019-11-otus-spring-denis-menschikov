package ru.otus.demen.books.model;

import lombok.NonNull;

import javax.persistence.Id;

public class Role {
    @Id
    @NonNull
    private String name;

    private String comment;
}
