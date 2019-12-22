package ru.otus.demen.books.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.demen.books.dao.GenreDao;
import ru.otus.demen.books.model.Genre;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreDao genreDao;

    @Override
    public Optional<Genre> findByName(String name) throws ServiceError {
        try {
            return genreDao.findByName(name);
        }
        catch (DataAccessException error) {
            throw new ServiceError(String.format("Ошибка во время поиска жанра по имени %s", name), error);
        }
    }

    @Override
    public Genre getByName(String name) throws ServiceError {
        Optional<Genre> genreOptional = findByName(name);
        return genreOptional
                .orElseThrow(() -> new ServiceError(String.format("Не найден жанр %s", name)));
    }
}
