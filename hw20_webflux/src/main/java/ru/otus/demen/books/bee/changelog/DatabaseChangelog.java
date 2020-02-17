package ru.otus.demen.books.bee.changelog;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
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

    @ChangeSet(order = "002", id = "addIndex", author = "denis-menschikov")
    public void addIndex(MongoTemplate mongoTemplate) {
        mongoTemplate.indexOps(Author.class).ensureIndex(new Index("surname", Direction.ASC));
        mongoTemplate.indexOps(Genre.class).ensureIndex(new Index("name", Direction.ASC));
    }
}
