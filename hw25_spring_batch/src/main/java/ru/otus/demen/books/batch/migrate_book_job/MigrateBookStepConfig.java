package ru.otus.demen.books.batch.migrate_book_job;

import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.demen.books.mapper.BookMapper;
import ru.otus.demen.books.model.Book;

import javax.persistence.EntityManagerFactory;

@Configuration
public class MigrateBookStepConfig {
    private static final int CHUNK_SIZE = 15;
    private static final String MIGRATE_BOOK_STEP_NAME = "MigrateBookStep";
    private static final String BOOK_ITEM_READER_NAME = "BookItemReader";

    @Bean
    JpaPagingItemReader<Book> bookItemReader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<Book>().name(BOOK_ITEM_READER_NAME)
                .entityManagerFactory(entityManagerFactory)
                .queryString("select b from Book b join fetch b.author join fetch b.genre left join fetch b.comments")
                .build();
    }

    @Bean
    ItemProcessor<Book, ru.otus.demen.books.mongo_model.Book> bookProcessor(BookMapper bookMapper) {
        return bookMapper::toMongo;
    }

    @Bean
    public Step migrateBookStep(
            StepBuilderFactory stepBuilderFactory,
            JpaPagingItemReader<Book> bookItemReader,
            ItemWriter<ru.otus.demen.books.mongo_model.Book> bookItemWriter,
            ItemProcessor<Book, ru.otus.demen.books.mongo_model.Book> bookProcessor,
            ItemWriteListener<Object> itemWriteListener,
            ItemReadListener<Object> itemReadListener)
    {
        return stepBuilderFactory.get(MIGRATE_BOOK_STEP_NAME)
                .<Book, ru.otus.demen.books.mongo_model.Book>chunk(CHUNK_SIZE)
                .reader(bookItemReader)
                .processor(bookProcessor)
                .writer(bookItemWriter)
                .listener(itemReadListener)
                .listener(itemWriteListener)
                .build();
    }
}
