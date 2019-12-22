package ru.otus.demen.books.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.demen.books.dao.AuthorDao;
import ru.otus.demen.books.model.Author;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorDao authorDao;

    @Override
    public Optional<Author> findById(long id) throws ServiceError {
        try {
            return authorDao.findById(id);
        }
        catch (DataAccessException error) {
            throw new ServiceError(String.format("Ошибка Dao во время поиска автора по id %d", id), error);
        }
    }

    @Override
    public Author getById(long id) throws ServiceError {
        Optional<Author> authorOptional = findById(id);
        return authorOptional
                .orElseThrow(() -> new ServiceError(String.format("Не найден автор с id=%d", id)));
    }
}
