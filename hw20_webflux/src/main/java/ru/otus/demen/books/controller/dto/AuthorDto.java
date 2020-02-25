package ru.otus.demen.books.controller.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class AuthorDto {
    private String id;
    @NonNull
    private String firstName;
    @NonNull
    private String surname;

    public String getName() { return firstName + " " + surname; }
}
