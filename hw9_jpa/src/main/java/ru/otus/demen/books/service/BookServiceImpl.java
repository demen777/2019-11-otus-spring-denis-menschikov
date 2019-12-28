package ru.otus.demen.books.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.demen.books.dao.BookDao;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.Genre;
import ru.otus.demen.books.service.exception.DataAccessServiceException;
import ru.otus.demen.books.service.exception.IllegalParameterException;
import ru.otus.demen.books.service.exception.NotFoundException;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final AuthorService authorService;
    private final GenreService genreService;
    private final BookDao bookDao;

    @Override
    public Book add(String name, long authorId, String genreName) {
        if(name == null || name.isEmpty()) {
            throw new IllegalParameterException("Имя книги должно быть не пустым");
        }
        Author author = authorService.getById(authorId);
        Genre genre = genreService.getByName(genreName);
        Book book = new Book(name, author, genre);
        try {
            return bookDao.save(book);
        }
        catch (DataAccessException error) {
            throw new DataAccessServiceException(String.format("Ошибка Dao во время добавления книги %s", book), error);
        }
    }

    @Override
    public Collection<Book> findBySurname(String surname) {
        try {
            return bookDao.findBySurname(surname);
        }
        catch (DataAccessException error) {
            throw new DataAccessServiceException(
                String.format("Ошибка Dao во время поиска книг по фамилии %s", surname), error);
        }
    }

    @Override
    public Book getById(long id) {
        try {
            Optional<Book> book = bookDao.findById(id);
            return book.orElseThrow(() -> new NotFoundException(String.format("Не найдена книга с id=%d", id)));
        }
        catch (DataAccessException error) {
            throw new DataAccessServiceException(String.format("Ошибка Dao во время поиска книги по id %d", id), error);
        }
    }
}
