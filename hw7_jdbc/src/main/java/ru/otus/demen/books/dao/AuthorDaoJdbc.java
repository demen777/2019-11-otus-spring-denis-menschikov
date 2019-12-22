package ru.otus.demen.books.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.demen.books.model.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    private static class AuthorRowMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet resultSet, int i) throws SQLException {
            return new Author(resultSet.getLong(1), resultSet.getString(2),
                    resultSet.getString(3));
        }
    }
}
