package ru.otus.demen.books.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.demen.books.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GenreDaoJdbc implements GenreDao {
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private final static GenreRowMapper GENRE_ROW_MAPPER = new GenreRowMapper();

    @Override
    public Optional<Genre> findByName(String name) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", name);
        try {
            Genre genre = namedParameterJdbcOperations.queryForObject(
                    "select id, name from genres where upper(name) = upper(:name)", params, GENRE_ROW_MAPPER);
            assert genre != null;
            return Optional.of(genre);
        }
        catch (IncorrectResultSizeDataAccessException error) {
            log.info(String.format("Не найден единственный жанр по имени %s", name), error);
            return Optional.empty();
        }
    }

    private static class GenreRowMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
            return new Genre(resultSet.getLong(1), resultSet.getString(2));
        }
    }
}
