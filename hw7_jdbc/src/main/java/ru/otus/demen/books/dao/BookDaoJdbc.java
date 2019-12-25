package ru.otus.demen.books.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BookDaoJdbc implements BookDao {
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private final static BookRowMapper BOOK_ROW_MAPPER = new BookRowMapper();

    @Override
    public Book save(Book book) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", book.getName());
        params.addValue("authorId", book.getAuthor().getId());
        params.addValue("genreId", book.getGenre().getId());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcOperations.update(
                "insert into books (name, author_id, genre_id) values (:name, :authorId, :genreId)",
                params, keyHolder);
        book.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return book;
    }

    @Override
    public Collection<Book> findBySurname(String surname) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("surname", surname);
        @SuppressWarnings("UnnecessaryLocalVariable")
        List<Book> books = namedParameterJdbcOperations.query(
                "select b.id, b.name, b.author_id, a.first_name, a.surname, b.genre_id, g.name genre_name" +
                        " from books b join authors a on b.author_id = a.id" +
                        "  join genres g on b.genre_id = g.id" +
                        " where a.surname = :surname", params, BOOK_ROW_MAPPER);
        return books;
    }

    @Override
    public Optional<Book> findById(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        try {
            Book book = namedParameterJdbcOperations.queryForObject(
                    "select b.id, b.name, b.author_id, a.first_name, a.surname, b.genre_id, g.name genre_name" +
                            " from books b join authors a on b.author_id = a.id" +
                            "  join genres g on b.genre_id = g.id" +
                            " where b.id = :id", params, BOOK_ROW_MAPPER);
            assert book != null;
            return Optional.of(book);
        }
        catch (IncorrectResultSizeDataAccessException error) {
            log.info(String.format("Не найдена книга с id %d", id), error);
            return Optional.empty();
        }
    }

    private static class BookRowMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Author author = new Author(rs.getLong("author_id"), rs.getString("first_name"),
                    rs.getString("surname"));
            Genre genre = new Genre(rs.getLong("genre_id"), rs.getString("genre_name"));
            return new Book(rs.getLong("id"), rs.getString("name"), author, genre);
        }
    }
}
