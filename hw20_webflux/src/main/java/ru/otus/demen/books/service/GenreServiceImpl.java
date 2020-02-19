package ru.otus.demen.books.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.demen.books.dao.GenreDao;
import ru.otus.demen.books.model.Genre;
import ru.otus.demen.books.service.exception.*;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreDao genreDao;

    @Override
    @Transactional
    public Mono<Genre> findByName(String name) {
        return genreDao.findByName(name).onErrorMap(error -> {
            if (error instanceof DataAccessException) {
                return new DataAccessServiceException(
                    String.format("Ошибка Dao во время поиска жанра по имени %s", name), error);
            }
            return error;
        });
    }

    @Override
    @Transactional
    public Mono<Genre> getByName(String name) {
        return findByName(name).switchIfEmpty(Mono.error(new NotFoundException(String.format("Не найден жанр %s", name))));
    }

    @Override
    @Transactional
    public Mono<Genre> add(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalParameterException("Имя жанра должно быть непустым");
        }
        return findByName(name)
            .flatMap(genre -> Mono.error(new AlreadyExistsException(String.format("Жанр с именем %s уже есть в БД",
                name))))
            .then(genreDao.save(new Genre(name)))
            .onErrorMap(error -> {
                if (error instanceof DataAccessException) {
                    return new DataAccessServiceException(
                        String.format("Ошибка Dao во время добавления жанра по имени %s", name), error);
                }
                return error;
            });
    }

    @Override
    @Transactional
    public Flux<Genre> getAll() {
        try {
            return genreDao.findAll();
        } catch (DataAccessException error) {
            throw new DataAccessServiceException("Ошибка Dao во время получения списка жанров", error);
        }
    }
}
