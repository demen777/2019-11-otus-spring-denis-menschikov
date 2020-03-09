package ru.otus.demen.books.batch;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;

@Configuration
public class ClearDbStepConfig {
    public static final String CLEAR_DB_STEP_NAME = "ClearDbStep";

    @Bean
    public Tasklet clearDbTask(MongoOperations mongoOperations)
    {
        return (contribution, chunkContext) -> {
            mongoOperations.dropCollection("book");
            mongoOperations.dropCollection("bookComment");
            mongoOperations.dropCollection("genre");
            mongoOperations.dropCollection("author");
            return null;
        };
    }

    @Bean
    public Step clearDbStep(StepBuilderFactory stepBuilderFactory, Tasklet clearDbTask)
    {
        return stepBuilderFactory.get(CLEAR_DB_STEP_NAME)
                .tasklet(clearDbTask)
                .build();
    }
}
