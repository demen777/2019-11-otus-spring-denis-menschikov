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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AuthorDaoJdbc implements AuthorDao {
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private final static AuthorRowMapper AUTHOR_ROW_MAPPER = new AuthorRowMapper();

    @Override
    public Optional<Author> findById(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        try {
            Author author = namedParameterJdbcOperations.queryForObject(
                    "select id, first_name, surname from authors where id = :id", params, AUTHOR_ROW_MAPPER);
            assert author != null;
            return Optional.of(author);
        }
        catch (IncorrectResultSizeDataAccessException error) {
            log.info(String.format("Не найден единственный автор с id %d", id), error);
            return Optional.empty();
        }
    }

    @Override
    public Author save(Author author) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("first_name", author.getFirstName());
        params.addValue("surname", author.getSurname());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcOperations.update(
                "insert into authors (first_name, surname) values (:first_name, :surname)",
                params, keyHolder);
        author.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return author;
    }

    @Override
    public Collection<Author> getAll() {
        @SuppressWarnings("UnnecessaryLocalVariable")
        List<Author> authors = namedParameterJdbcOperations.query(
                "select id, first_name, surname from authors", AUTHOR_ROW_MAPPER);
        return authors;
    }

    @Override
    public Optional<Author> findByNameAndSurname(String firstName, String surname) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("first_name", firstName);
        params.addValue("surname", surname);
        try {
            Author author = namedParameterJdbcOperations.queryForObject(
                    "select id, first_name, surname from authors" +
                            " where first_name = :first_name and surname = :surname",
                    params, AUTHOR_ROW_MAPPER);
            assert author != null;
            return Optional.of(author);
        }
        catch (IncorrectResultSizeDataAccessException error) {
            log.info(String.format("Не найден автор с именем %s и фамилией %s", firstName, surname),
                    error);
            return Optional.empty();
        }
    }

    private static class AuthorRowMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet resultSet, int i) throws SQLException {
            return new Author(resultSet.getLong("id"), resultSet.getString("first_name"),
                    resultSet.getString("surname"));
        }
    }
}
