package ru.otus.demen.books.batch.migrate_book_job;

import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import ru.otus.demen.books.mapper.AuthorMapper;
import ru.otus.demen.books.model.Author;

import javax.persistence.EntityManagerFactory;

@Configuration
public class MigrateAuthorStepConfig {
    private static final int CHUNK_SIZE = 5;
    private static final String MIGRATE_AUTHOR_STEP_NAME = "MigrateAuthorStep";
    private static final String AUTHOR_ITEM_READER_NAME = "AuthorItemReader";

    @Bean
    JpaPagingItemReader<Author> authorItemReader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<Author>().name(AUTHOR_ITEM_READER_NAME)
                .entityManagerFactory(entityManagerFactory)
                .queryString("select a from Author a")
                .build();
    }

    @Bean
    MongoItemWriter<ru.otus.demen.books.mongo_model.Author> authorItemWriter(MongoOperations mongoOperations) {
        return new MongoItemWriterBuilder<ru.otus.demen.books.mongo_model.Author>()
                .template(mongoOperations)
                .build();
    }

    @Bean
    ItemProcessor<Author, ru.otus.demen.books.mongo_model.Author> authorProcessor(AuthorMapper authorMapper) {
        return authorMapper::toMongo;
    }

    @Bean
    public Step migrateAuthorStep(
            StepBuilderFactory stepBuilderFactory,
            JpaPagingItemReader<Author> authorItemReader,
            MongoItemWriter<ru.otus.demen.books.mongo_model.Author> authorItemWriter,
            ItemProcessor<Author, ru.otus.demen.books.mongo_model.Author> authorProcessor,
            ItemWriteListener<Object> itemWriteListener)
    {
        return stepBuilderFactory.get(MIGRATE_AUTHOR_STEP_NAME)
                .<Author, ru.otus.demen.books.mongo_model.Author>chunk(CHUNK_SIZE)
                .reader(authorItemReader)
                .processor(authorProcessor)
                .writer(authorItemWriter)
                .listener(itemWriteListener)
                .build();
    }
}
