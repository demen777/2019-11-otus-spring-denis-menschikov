package ru.otus.demen.books.controller.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.demen.books.controller.dto.AuthorDto;
import ru.otus.demen.books.controller.dto.BookCommentDto;
import ru.otus.demen.books.controller.dto.BookDto;
import ru.otus.demen.books.controller.dto.GenreDto;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.BookComment;
import ru.otus.demen.books.model.Genre;

@Service
@RequiredArgsConstructor
public class BookMappersFacade {
    private final AuthorDtoMapper authorDtoMapper;
    private final GenreDtoMapper genreDtoMapper;
    private final BookDtoMapper bookDtoMapper;
    private final BookCommentDtoMapper bookCommentDtoMapper;

    public AuthorDto toAuthorDto(Author author) {
        return authorDtoMapper.toAuthorDto(author);
    }

    public BookCommentDto toBookCommentDto(BookComment bookComment) {
        return bookCommentDtoMapper.toBookCommentDto(bookComment);
    }

    public BookDto bookDto(Book book) {
        return bookDtoMapper.bookDto(book);
    }

    public GenreDto toGenreDto(Genre genre) {
        return genreDtoMapper.toGenreDto(genre);
    }
}
