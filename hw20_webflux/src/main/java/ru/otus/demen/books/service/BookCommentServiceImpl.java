package ru.otus.demen.books.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
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
    public Mono<BookComment> add(String bookId, String text) {
        if (text == null || text.isEmpty()) {
            throw new IllegalParameterException("Текст комментария должен быть непустым");
        }
        try {
            Mono<Long> count = bookDao.countById(bookId);
            count.filter(c -> c == 0).flatMap(c -> {
                throw new NotFoundException(String.format("Не найдена книга с id=%s", bookId));
            });
            BookComment bookComment = new BookComment(text);
            bookDao.addComment(bookId, bookComment);
            return Mono.just(bookComment);
        } catch (DataAccessException error) {
            throw new DataAccessServiceException("Ошибка Dao во время добавления комментария", error);
        }
    }

    private Mono<Book> getBook(String bookId) {
        return bookDao.findById(bookId).switchIfEmpty(Mono.error(new NotFoundException(String.format("Не найдена " +
            "книга с id=%s", bookId))));
    }

    @Override
    @Transactional
    public Mono<Boolean> deleteById(String bookCommentId) {
        try {
            return bookDao.removeCommentById(bookCommentId).map(deletedQuantity -> deletedQuantity > 0);
        } catch (DataAccessException error) {
            throw new DataAccessServiceException(
                String.format("Ошибка Dao во время удаления комментария с id=%s", bookCommentId), error);
        }
    }
}
