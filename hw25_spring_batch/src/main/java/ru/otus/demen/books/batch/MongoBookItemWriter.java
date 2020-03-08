package ru.otus.demen.books.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.otus.demen.books.mongo_model.Book;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MongoBookItemWriter implements ItemWriter<Book> {
    private final MongoOperations mongoOperations;

    @Override
    public void write(@NonNull List<? extends Book> books) {
        books.forEach(this::writeOne);
    }

    private void writeOne(Book book) {
        book.getComments().forEach(mongoOperations::insert);
        mongoOperations.insert(book);
    }
}
