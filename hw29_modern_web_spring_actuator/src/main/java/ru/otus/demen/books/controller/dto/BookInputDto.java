package ru.otus.demen.books.controller.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class BookInputDto {
    @NonNull
    private String name;
    @NonNull
    private long authorId;
    @NonNull
    private long genreId;
}
