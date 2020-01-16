package ru.otus.demen.books.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.demen.books.dao.AuthorDao;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.service.exception.*;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorDao authorDao;

    @Override
    @Transactional
    public Optional<Author> findById(long id) {
        try {
            return authorDao.findById(id);
        } catch (DataAccessException error) {
            throw new DataAccessServiceException(String.format("Ошибка Dao во время поиска автора по id %d", id),
                    error);
        }
    }

    @Override
    @Transactional
    public Author getById(long id) {
        Optional<Author> authorOptional = findById(id);
        return authorOptional
                .orElseThrow(() -> new NotFoundException(String.format("Не найден автор с id=%d", id)));
    }

    @Override
    @Transactional
    public Author add(String firstName, String surname) {
        try {
            if (firstName == null || firstName.isEmpty()) {
                throw new IllegalParameterException("Имя не должно быть пустым");
            }
            if (surname == null || surname.isEmpty()) {
                throw new IllegalParameterException("Фамилия не должна быть пустой");
            }
            Optional<Author> alreadyExistsAuthor = findByNameAndSurname(firstName, surname);
            alreadyExistsAuthor.ifPresent(author -> {
                throw new AlreadyExistsException(String.format("Автор %s %s уже существует в БД",
                        firstName, surname));
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
    public Optional<Author> findByNameAndSurname(String firstName, String surname) {
        try {
            return authorDao.findByNameAndSurname(firstName, surname);
        } catch (DataAccessException error) {
            throw new DataAccessServiceException(String.format("Ошибка Dao во время поиска по имени %s и фамилии %s",
                    firstName, surname),
                    error);
        }
    }

    @Override
    @Transactional
    public Collection<Author> getAll() {
        try {
            return authorDao.getAll();
        } catch (DataAccessException error) {
            throw new DataAccessServiceException("Ошибка Dao во время получения списка всех авторов", error);
        }
    }
}
