package ru.otus.demen.books.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.demen.books.dao.AuthorDao;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.service.exception.*;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorDao authorDao;

    @Override
    @Transactional
    public Mono<Author> findById(String id) {
        try {
            return authorDao.findById(id);
        } catch (DataAccessException error) {
            throw new DataAccessServiceException(String.format("Ошибка Dao во время поиска автора по id %s", id),
                    error);
        }
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
        try {
            if (firstName == null || firstName.isEmpty()) {
                throw new IllegalParameterException("Имя не должно быть пустым");
            }
            if (surname == null || surname.isEmpty()) {
                throw new IllegalParameterException("Фамилия не должна быть пустой");
            }
            Mono<Author> alreadyExistsAuthor = findByNameAndSurname(firstName, surname);
            alreadyExistsAuthor.flatMap((author) ->
            {
                throw new AlreadyExistsException(String.format("Автор %s %s уже существует в БД", firstName,
                    surname));
            });
            Author author = new Author(firstName, surname);
            return authorDao.save(author);
        } catch (DataAccessException error) {
            throw new DataAccessServiceException(
                    String.format("Ошибка Dao во время создания автора %s %s", firstName, surname),
                    error);
        }
    }

    @Transactional
    public Mono<Author> findByNameAndSurname(String firstName, String surname) {
        try {
            return authorDao.findByFirstNameAndSurname(firstName, surname);
        } catch (DataAccessException error) {
            throw new DataAccessServiceException(String.format("Ошибка Dao во время поиска по имени %s и фамилии %s",
                    firstName, surname),
                    error);
        }
    }

    @Override
    @Transactional
    public Flux<Author> getAll() {
        try {
            return authorDao.findAll();
        } catch (DataAccessException error) {
            throw new DataAccessServiceException("Ошибка Dao во время получения списка всех авторов", error);
        }
    }
}
