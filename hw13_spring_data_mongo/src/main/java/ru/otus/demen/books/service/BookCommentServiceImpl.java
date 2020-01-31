package ru.otus.demen.books.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.demen.books.dao.BookDao;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.BookComment;
import ru.otus.demen.books.service.exception.DataAccessServiceException;
import ru.otus.demen.books.service.exception.IllegalParameterException;
import ru.otus.demen.books.service.exception.NotFoundException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookCommentServiceImpl implements BookCommentService {
    private final BookDao bookDao;

    @Override
    @Transactional
    public BookComment add(String bookId, String text) {
        if (text == null || text.isEmpty()) {
            throw new IllegalParameterException("Текст комментария должен быть непустым");
        }
        try {
            long count = bookDao.countById(bookId);
            if (count == 0) {
                throw new NotFoundException(String.format("Не найдена книга с id=%s", bookId));
            }
            BookComment bookComment = new BookComment(text);
            bookDao.addComment(bookId, bookComment);
            return bookComment;
        }
        catch (DataAccessException error) {
            throw new DataAccessServiceException("Ошибка Dao во время добавления комментария", error);
        }
    }

    private Book getBook(String bookId) {
        Optional<Book> bookOptional = bookDao.findById(bookId);
        return bookOptional.orElseThrow(
                () -> new NotFoundException(String.format("Не найдена книга с id=%s", bookId)));
    }

    @Override
    @Transactional
    public boolean deleteById(String bookCommentId) {
        try {
            long deletedQuantity = bookDao.removeCommentById(bookCommentId);
            return deletedQuantity > 0;
        }
        catch (DataAccessException error) {
            throw new DataAccessServiceException(
                    String.format("Ошибка Dao во время удаления комментария с id=%s", bookCommentId), error);
        }
    }
}
