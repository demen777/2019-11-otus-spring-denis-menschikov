package ru.otus.demen.books.batch.migrate_book_job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MigrateBookJobConfig {
    private static final String MIGRATE_BOOK_JOB_NAME = "MigrateBookJob";

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public Job migrateBookJob(@Qualifier("clearDbStep") Step clearDbStep,
                              @Qualifier("migrateAuthorStep") Step migrateAuthorStep,
                              @Qualifier("migrateGenreStep") Step migrateGenreStep,
                              @Qualifier("migrateBookStep") Step migrateBookStep) {
        return jobBuilderFactory.get(MIGRATE_BOOK_JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(clearDbStep)
                .next(migrateAuthorStep)
                .next(migrateGenreStep)
                .next(migrateBookStep)
                .build();
    }
}
