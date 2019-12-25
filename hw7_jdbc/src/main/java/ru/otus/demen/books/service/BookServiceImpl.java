package ru.otus.demen.books.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.demen.books.dao.BookDao;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.Genre;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final AuthorService authorService;
    private final GenreService genreService;
    private final BookDao bookDao;

    @Override
    public Book add(String name, long authorId, String genreName) throws ServiceError {
        if(name == null || name.isEmpty()) {
            throw new ServiceError("Имя книги должно быть не пустым");
        }
        Author author = authorService.getById(authorId);
        Genre genre = genreService.getByName(genreName);
        Book book = new Book(name, author, genre);
        try {
            return bookDao.save(book);
        }
        catch (DataAccessException error) {
            throw new ServiceError(String.format("Ошибка Dao во время добавления книги %s", book), error);
        }
    }

    @Override
    public Collection<Book> findBySurname(String surname) throws ServiceError {
        try {
            return bookDao.findBySurname(surname);
        }
        catch (DataAccessException error) {
            throw new ServiceError(String.format("Ошибка Dao во время поиска книг по фамилии %s", surname), error);
        }
    }

    @Override
    public Book getById(long id) throws ServiceError {
        try {
            Optional<Book> book = bookDao.findById(id);
            return book.orElseThrow(() -> new ServiceError(String.format("Не найдена книга с id=%d", id)));
        }
        catch (DataAccessException error) {
            throw new ServiceError(String.format("Ошибка Dao во время поиска книги по id %d", id), error);
        }
    }
}
