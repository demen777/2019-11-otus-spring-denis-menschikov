package ru.otus.demen.books.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.demen.books.dao.AuthorDao;
import ru.otus.demen.books.dao.BookDao;
import ru.otus.demen.books.dao.GenreDao;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.Genre;
import ru.otus.demen.books.service.exception.DataAccessServiceException;
import ru.otus.demen.books.service.exception.IllegalParameterException;
import ru.otus.demen.books.service.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final BookDao bookDao;

    @Override
    @Transactional
    public Book add(String name, String authorId, String genreName) {
        if (name == null || name.isEmpty()) {
            throw new IllegalParameterException("Имя книги должно быть не пустым");
        }
        try {
            Optional<Author> authorOptional = authorDao.findById(authorId);
            Author author = authorOptional.orElseThrow(
                    () -> new NotFoundException(String.format("Не найден автор с id=%s", authorId)));
            Optional<Genre> genreOptional = genreDao.findByName(genreName);
            Genre genre = genreOptional.orElseThrow(
                    () -> new NotFoundException(String.format("Не найден жанр с именем %s", genreName)));
            Book book = new Book(name, author, genre);
            return bookDao.save(book);
        } catch (DataAccessException error) {
            throw new DataAccessServiceException("Ошибка Dao во время добавления книги", error);
        }
    }

    @Override
    @Transactional
    public List<Book> findBySurname(String surname) {
        try {
            return bookDao.findByAuthorSurname(surname);
        } catch (DataAccessException error) {
            throw new DataAccessServiceException(
                    String.format("Ошибка Dao во время поиска книг по фамилии %s", surname), error);
        }
    }

    @Override
    @Transactional
    public Book getById(String id) {
        try {
            Optional<Book> book = bookDao.findById(id);
            return book.orElseThrow(() -> new NotFoundException(String.format("Не найдена книга с id=%s", id)));
        } catch (DataAccessException error) {
            throw new DataAccessServiceException(String.format("Ошибка Dao во время поиска книги по id %s", id), error);
        }
    }
}
