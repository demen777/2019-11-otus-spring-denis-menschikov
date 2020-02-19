package ru.otus.demen.books.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.demen.books.dao.AuthorDao;
import ru.otus.demen.books.dao.BookDao;
import ru.otus.demen.books.dao.GenreDao;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.Genre;
import ru.otus.demen.books.service.exception.DataAccessServiceException;
import ru.otus.demen.books.service.exception.IllegalParameterException;
import ru.otus.demen.books.service.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final BookDao bookDao;

    @Override
    @Transactional
    public Mono<Book> add(String name, String authorId, String genreName) {
        if (name == null || name.isEmpty()) {
            throw new IllegalParameterException("Имя книги должно быть не пустым");
        }
        try {
            Mono<Genre> genreOptional = genreDao.findByName(genreName)
                .switchIfEmpty(Mono.error(new NotFoundException(String.format("Не найден жанр с именем %s",
                    genreName))));
            return authorDao.findById(authorId)
                .switchIfEmpty(Mono.error(new NotFoundException(String.format("Не найден автор с id=%s", authorId))))
                .zipWith(genreOptional, (author, genre) -> new Book(name, author, genre))
                .flatMap(bookDao::save);
        } catch (DataAccessException error) {
            throw new DataAccessServiceException("Ошибка Dao во время добавления книги", error);
        }
    }

    @Override
    @Transactional
    public Flux<Book> findBySurname(String surname) {
        try {
            return bookDao.findByAuthorSurname(surname);
        } catch (DataAccessException error) {
            throw new DataAccessServiceException(
                    String.format("Ошибка Dao во время поиска книг по фамилии %s", surname), error);
        }
    }

    @Override
    @Transactional
    public Mono<Book> getById(String id) {
        try {
            return bookDao.findById(id).switchIfEmpty(Mono.error(new NotFoundException(String.format("Не " +
                "найдена книга с id=%s", id))));
        } catch (DataAccessException error) {
            throw new DataAccessServiceException(String.format("Ошибка Dao во время поиска книги по id %s", id), error);
        }
    }

    @Override
    @Transactional
    public Flux<Book> findAll() {
        return bookDao.findAll();
    }

    @Override
    @Transactional
    public Mono<Void> deleteById(String id) {
        return bookDao.deleteById(id);
    }

    @Override
    @Transactional
    public Mono<Void> update(String id, String name, String authorId, String genreId) {
        // TODO
        return Mono.empty();
    }
}
