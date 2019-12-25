package ru.otus.demen.books.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.demen.books.dao.GenreDao;
import ru.otus.demen.books.model.Genre;
import ru.otus.demen.books.service.exception.*;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreDao genreDao;

    @Override
    public Optional<Genre> findByName(String name) {
        try {
            return genreDao.findByName(name);
        } catch (DataAccessException error) {
            throw new DataAccessServiceException(
                String.format("Ошибка Dao во время поиска жанра по имени %s", name), error);
        }
    }

    @Override
    public Genre getByName(String name) {
        Optional<Genre> genreOptional = findByName(name);
        return genreOptional
                .orElseThrow(() -> new NotFoundException(String.format("Не найден жанр %s", name)));
    }

    @Override
    public Genre add(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalParameterException("Имя жанра должно быть непустым");
        }
        Optional<Genre> alreadyExistsGenre = findByName(name);
        if (alreadyExistsGenre.isPresent()) {
            throw new AlreadyExistsException(String.format("Жанр с именем %s уже есть в БД", name));
        }
        try {
            return genreDao.save(new Genre(name));
        }
        catch (DataAccessException error) {
            throw new DataAccessServiceException(
                String.format("Ошибка Dao во время добавления жанра по имени %s", name), error);
        }
    }

    @Override
    public Collection<Genre> getAll() {
        try {
            return genreDao.getAll();
        }
        catch (DataAccessException error) {
            throw new DataAccessServiceException("Ошибка Dao во время получения списка жанров", error);
        }
    }
}
