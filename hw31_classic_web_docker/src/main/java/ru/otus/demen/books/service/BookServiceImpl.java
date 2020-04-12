package ru.otus.demen.books.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.demen.books.dao.AuthorDao;
import ru.otus.demen.books.dao.BookCommentDao;
import ru.otus.demen.books.dao.BookDao;
import ru.otus.demen.books.dao.GenreDao;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.Genre;
import ru.otus.demen.books.service.exception.DataAccessServiceException;
import ru.otus.demen.books.service.exception.IllegalParameterException;
import ru.otus.demen.books.service.exception.NotFoundException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final BookDao bookDao;
    private final BookCommentDao bookCommentDao;

    @Override
    @Transactional
    public Book add(String name, long authorId, long genreId) {
        if (name == null || name.isEmpty()) {
            throw new IllegalParameterException("Имя книги должно быть не пустым");
        }
        try {
            Optional<Author> authorOptional = authorDao.findById(authorId);
            Author author = authorOptional.orElseThrow(
                () -> new NotFoundException(String.format("Не найден автор с id=%d", authorId)));
            Optional<Genre> genreOptional = genreDao.findById(genreId);
            Genre genre = genreOptional.orElseThrow(
                () -> new NotFoundException(String.format("Не найден жанр с id=%d", genreId)));
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
    public Book getById(long id) {
        try {
            Optional<Book> book = bookDao.findById(id);
            return book.orElseThrow(() -> new NotFoundException(String.format("Не найдена книга с id=%d", id)));
        } catch (DataAccessException error) {
            throw new DataAccessServiceException(String.format("Ошибка Dao во время поиска книги по id %d", id), error);
        }
    }

    @Override
    @Transactional
    public List<Book> findAll() {
        try {
            return bookDao.findAll();
        } catch (DataAccessException error) {
            throw new DataAccessServiceException("Ошибка Dao во время получения списка всех книг", error);
        }
    }

    @Override
    @Transactional
    public void update(long id, String name, long authorId, long genreId) {
        try {
            Book book = bookDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Не найдена книга с id=%d", id)));
            Optional<Author> author = authorDao.findById(authorId);
            book.setAuthor(author.orElseThrow(
                () -> new NotFoundException(String.format("Не найден автор с id=%d", authorId))));
            Optional<Genre> genre = genreDao.findById(genreId);
            book.setGenre(genre.orElseThrow(
                () -> new NotFoundException(String.format("Не найден жанр с id=%d", genreId))));
            book.setName(name);
            bookDao.save(book);
        } catch (DataAccessException error) {
            throw new DataAccessServiceException(
                String.format("Ошибка Dao во время изменения книги с id=%d", id), error);
        }
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        try {
            bookCommentDao.deleteByBookId(id);
            bookDao.deleteById(id);
        }
        catch (DataAccessException error) {
            throw new DataAccessServiceException(
                    String.format("Ошибка Dao во время удаления книги с id=%d", id), error);
        }
    }
}
