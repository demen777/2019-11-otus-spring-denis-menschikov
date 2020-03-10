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
import ru.otus.demen.books.mapper.GenreMapper;
import ru.otus.demen.books.model.Genre;

import javax.persistence.EntityManagerFactory;

@Configuration
public class MigrateGenreStepConfig {
    private static final int CHUNK_SIZE = 5;
    private static final String MIGRATE_GENRE_STEP_NAME = "MigrateGenreStep";
    private static final String GENRE_ITEM_READER_NAME = "GenreItemReader";

    @Bean
    JpaPagingItemReader<Genre> genreItemReader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<Genre>().name(GENRE_ITEM_READER_NAME)
                .entityManagerFactory(entityManagerFactory)
                .queryString("select g from Genre g")
                .build();
    }

    @Bean
    MongoItemWriter<ru.otus.demen.books.mongo_model.Genre> genreItemWriter(MongoOperations mongoOperations) {
        return new MongoItemWriterBuilder<ru.otus.demen.books.mongo_model.Genre>()
                .template(mongoOperations)
                .build();
    }

    @Bean
    ItemProcessor<Genre, ru.otus.demen.books.mongo_model.Genre> genreProcessor(GenreMapper genreMapper) {
        return genreMapper::toMongo;
    }

    @Bean
    public Step migrateGenreStep(
            StepBuilderFactory stepBuilderFactory,
            JpaPagingItemReader<Genre> genreItemReader,
            MongoItemWriter<ru.otus.demen.books.mongo_model.Genre> genreItemWriter,
            ItemProcessor<Genre, ru.otus.demen.books.mongo_model.Genre> genreProcessor,
            ItemWriteListener<Object> itemWriteListener)
    {
        return stepBuilderFactory.get(MIGRATE_GENRE_STEP_NAME)
                .<Genre, ru.otus.demen.books.mongo_model.Genre>chunk(CHUNK_SIZE)
                .reader(genreItemReader)
                .processor(genreProcessor)
                .writer(genreItemWriter)
                .listener(itemWriteListener)
                .build();
    }
}
