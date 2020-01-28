package ru.otus.demen.books.bee.changelog;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.Genre;

@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "addData", author = "denis-menschikov")
    public void addData(MongoTemplate mongoTemplate) {
        Genre genreNovel = mongoTemplate.save(new Genre("Роман"));
        Author authorTolstoy = mongoTemplate.save(new Author("Лев", "Толстой"));
        mongoTemplate.save(new Book("Война и мир", authorTolstoy, genreNovel));
    }

    // todo add indexes
}
