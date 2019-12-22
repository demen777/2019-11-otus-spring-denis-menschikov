package ru.otus.demen.books.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.demen.books.dao.BookDao;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.Genre;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final AuthorService authorService;
    private final GenreService genreService;
    private final BookDao bookDao;

    @Override
    public Book addBook(String name, long authorId, String genreName) throws ServiceError {
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
}
