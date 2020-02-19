package ru.otus.demen.books.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.demen.books.dao.AuthorDao;
import ru.otus.demen.books.dao.BookDao;
import ru.otus.demen.books.dao.GenreDao;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.Genre;
import ru.otus.demen.books.service.exception.IllegalParameterException;
import ru.otus.demen.books.service.exception.NotFoundException;

import static ru.otus.demen.books.service.DataAccessExceptionWrapper.wrapDataAccessException;

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
            return Mono.error(new IllegalParameterException("Имя книги должно быть не пустым"));
        }
        Mono<Genre> genreOptional = genreDao.findByName(genreName)
                .switchIfEmpty(Mono.error(new NotFoundException(String.format("Не найден жанр с именем %s",
                        genreName))));
        return authorDao.findById(authorId)
                .switchIfEmpty(Mono.error(new NotFoundException(String.format("Не найден автор с id=%s", authorId))))
                .zipWith(genreOptional, (author, genre) -> new Book(name, author, genre))
                .flatMap(bookDao::save)
                .onErrorMap(error -> wrapDataAccessException("Ошибка Dao во время добавления книги", error));
    }

    @Override
    @Transactional
    public Flux<Book> findBySurname(String surname) {
        return bookDao.findByAuthorSurname(surname).onErrorMap(
                error -> wrapDataAccessException(
                        String.format("Ошибка Dao во время поиска книг по фамилии %s", surname), error));
    }

    @Override
    @Transactional
    public Mono<Book> getById(String id) {
        return bookDao.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(String.format("Не " +
                        "найдена книга с id=%s", id))))
                .onErrorMap(error -> wrapDataAccessException(
                        String.format("Ошибка Dao во время поиска книги по id %s", id), error));
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
        Mono<Genre> genreMono = genreDao.findById(genreId)
                .switchIfEmpty(Mono.error(new NotFoundException(String.format("Не найден жанр с id %s",
                        genreId))));
        Mono<Author> authorMono = authorDao.findById(authorId)
                .switchIfEmpty(Mono.error(new NotFoundException(String.format("Не найден автор с id=%s", authorId))));
        Mono<Book> bookMono = bookDao.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(String.format("Не найдена книга с id=%s", id))));
        return Mono.zip(bookMono, genreMono, authorMono)
                .flatMap(tuple3 -> updateBook(tuple3.getT1(), tuple3.getT2(), tuple3.getT3(), name))
                .then(Mono.empty());
    }

    private Mono<Book> updateBook(Book book, Genre genre, Author author, String name) {
        book.setAuthor(author);
        book.setGenre(genre);
        book.setName(name);
        return bookDao.save(book);
    }
}
