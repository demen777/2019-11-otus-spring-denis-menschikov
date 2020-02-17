package ru.otus.demen.books.controller.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class GenreDto {
    private long id;
    @NonNull
    private String name;
}
