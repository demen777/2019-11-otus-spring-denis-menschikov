package ru.otus.demen.books.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.demen.books.dao.AuthorDao;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.service.exception.*;

import static ru.otus.demen.books.service.DataAccessExceptionWrapper.wrapDataAccessException;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorDao authorDao;

    @Override
    @Transactional
    public Mono<Author> findById(String id) {
        return authorDao.findById(id).onErrorMap(
                error -> wrapDataAccessException(
                        String.format("Ошибка Dao во время поиска автора по id %s", id), error));
    }

    @Override
    @Transactional
    public Mono<Author> getById(String id) {
        Mono<Author> authorOptional = findById(id);
        return authorOptional
                .switchIfEmpty(Mono.error(new NotFoundException(String.format("Не найден автор с id=%s", id))));
    }

    @Override
    @Transactional
    public Mono<Author> add(String firstName, String surname) {
        if (firstName == null || firstName.isEmpty()) {
            return Mono.error(new IllegalParameterException("Имя не должно быть пустым"));
        }
        if (surname == null || surname.isEmpty()) {
            return Mono.error(new IllegalParameterException("Фамилия не должна быть пустой"));
        }
        return findByNameAndSurname(firstName, surname)
                .flatMap(author -> Mono.error(
                        new AlreadyExistsException(
                                String.format("Автор %s %s уже существует в БД", firstName, surname)
                        )))
                .then(authorDao.save(new Author(firstName, surname)))
                .onErrorMap(error -> wrapDataAccessException(
                        String.format("Ошибка Dao во время создания автора %s %s", firstName, surname), error));
    }

    @Override
    @Transactional
    public Mono<Author> findByNameAndSurname(String firstName, String surname) {
        return authorDao.findByFirstNameAndSurname(firstName, surname)
                .onErrorMap(error -> wrapDataAccessException(
                        String.format("Ошибка Dao во время поиска по имени %s и фамилии %s", firstName, surname),
                        error));
    }

    @Override
    @Transactional
    public Flux<Author> getAll() {
        return authorDao.findAll()
                .onErrorMap(error -> wrapDataAccessException(
                        "Ошибка Dao во время получения списка всех авторов", error));
    }
}
