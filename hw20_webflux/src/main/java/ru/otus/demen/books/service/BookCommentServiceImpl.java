package ru.otus.demen.books.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.otus.demen.books.dao.BookDao;
import ru.otus.demen.books.model.BookComment;
import ru.otus.demen.books.service.exception.IllegalParameterException;
import ru.otus.demen.books.service.exception.NotFoundException;

import static ru.otus.demen.books.service.DataAccessExceptionWrapper.wrapDataAccessException;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookCommentServiceImpl implements BookCommentService {
    private final BookDao bookDao;

    @Override
    @Transactional
    public Mono<BookComment> add(String bookId, String text) {
        if (text == null || text.isEmpty()) {
            return Mono.error(new IllegalParameterException("Текст комментария должен быть непустым"));
        }
        Mono<Long> count = bookDao.countById(bookId)
                .filter(c -> c > 0)
                .switchIfEmpty(Mono.error(new NotFoundException(String.format("Не найдена книга с id=%s", bookId))));
        return count
                .then(bookDao.addComment(bookId, new BookComment(text)))
                .onErrorMap(error -> wrapDataAccessException("Ошибка Dao во время добавления комментария", error));
    }

    @Override
    @Transactional
    public Mono<Boolean> deleteById(String bookCommentId) {
        return bookDao.removeCommentById(bookCommentId)
                .map(deletedQuantity -> deletedQuantity > 0)
                .onErrorMap(error -> wrapDataAccessException(
                        String.format("Ошибка Dao во время удаления комментария с id=%s", bookCommentId), error));
    }
}
