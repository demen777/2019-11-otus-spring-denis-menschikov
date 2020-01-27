package ru.otus.demen.books.bee.changelog;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.Genre;

@ChangeLog
public class DatabaseChangelog {
    private static final Genre GENRE_NOVEL = new Genre("1", "Роман");
    private static final Author AUTHOR_TOLSTOY = new Author("1", "Лев", "Толстой");

    @ChangeSet(order = "001", id = "addGenres", author = "denis-menschikov")
    public void addGenres(MongoTemplate mongoTemplate) {
        mongoTemplate.save(GENRE_NOVEL);
        mongoTemplate.save(new Genre("asd"));
        mongoTemplate.save(new Genre("asd123"));
    }

    @ChangeSet(order = "002", id = "addAuthors", author = "denis-menschikov")
    public void addAuthors(MongoTemplate mongoTemplate) {
        mongoTemplate.save(AUTHOR_TOLSTOY);
    }

    @ChangeSet(order = "003", id = "addBooks", author = "denis-menschikov")
    public void addBooks(MongoTemplate mongoTemplate) {
        mongoTemplate.save(new Book("1", "Война и мир", AUTHOR_TOLSTOY, GENRE_NOVEL));
    }
}
