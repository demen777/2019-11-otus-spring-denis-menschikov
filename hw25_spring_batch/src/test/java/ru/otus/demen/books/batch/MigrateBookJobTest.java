package ru.otus.demen.books.batch;

import com.jupiter.tools.spring.test.mongo.annotation.ExpectedMongoDataSet;
import com.jupiter.tools.spring.test.mongo.junit5.MongoDbExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.demen.books.mongo_model.Book;
import ru.otus.demen.books.mongo_model.BookComment;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@SpringBatchTest
@ExtendWith({SpringExtension.class, MongoDbExtension.class})
class MigrateBookJobTest {
    private static final String MIGRATE_BOOK_JOB_NAME = "MigrateBookJob";

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    MongoOperations mongoOperations;

    @Test
    @ExpectedMongoDataSet("dataset/migrate_book_result.json")
    void testMigrateBookJob() throws Exception {
        Job job = jobLauncherTestUtils.getJob();

        assertThat(job).isNotNull()
                .extracting(Job::getName)
                .isEqualTo(MIGRATE_BOOK_JOB_NAME);

        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
        assertThat(Objects.requireNonNull(mongoOperations.findById("1", Book.class)).getComments()).containsExactly(
                new BookComment("1", "Большая книга"), new BookComment("2", "Толстой рулит"));
    }
}