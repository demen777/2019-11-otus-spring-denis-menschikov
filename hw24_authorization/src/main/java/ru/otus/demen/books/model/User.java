package ru.otus.demen.books.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class User {
    @Id
    @NonNull
    private String username;

    @NonNull
    private String passwordHash;

    @NonNull
    private boolean enabled;
}
