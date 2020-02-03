package ru.otus.demen.books.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.demen.books.dao.BookCommentDao;
import ru.otus.demen.books.dao.BookDao;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.BookComment;
import ru.otus.demen.books.service.exception.DataAccessServiceException;
import ru.otus.demen.books.service.exception.IllegalParameterException;
import ru.otus.demen.books.service.exception.NotFoundException;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookCommentServiceImpl implements BookCommentService {
    private final BookCommentDao bookCommentDao;
    private final BookDao bookDao;

    @Override
    @Transactional
    public BookComment add(long bookId, String text) {
        if (text == null || text.isEmpty()) {
            throw new IllegalParameterException("Текст комментария должен быть непустым");
        }
        try {
            Book book = getBook(bookId);
            BookComment bookComment = new BookComment(text, book);
            return bookCommentDao.save(bookComment);
        }
        catch (DataAccessException error) {
            throw new DataAccessServiceException("Ошибка Dao во время добавления комментария", error);
        }
    }

    private Book getBook(long bookId) {
        Optional<Book> bookOptional = bookDao.findById(bookId);
        return bookOptional.orElseThrow(
                () -> new NotFoundException(String.format("Не найдена книга с id=%d", bookId)));
    }

    @Override
    @Transactional
    public boolean deleteById(long bookCommentId) {
        try {
            long deletedQuantity = bookCommentDao.removeById(bookCommentId);
            return deletedQuantity > 0;
        }
        catch (DataAccessException error) {
            throw new DataAccessServiceException(
                    String.format("Ошибка Dao во время удаления комментария с id=%d", bookCommentId), error);
        }
    }

    @Override
    @Transactional
    public Collection<BookComment> getByBookId(long bookId) {
        try {
            getBook(bookId);
            return bookCommentDao.findByBookId(bookId);
        }
        catch (DataAccessException error) {
            throw new DataAccessServiceException(
                String.format("Ошибка Dao во время получения комментариев для книги с id=%d", bookId), error);
        }
    }
}
