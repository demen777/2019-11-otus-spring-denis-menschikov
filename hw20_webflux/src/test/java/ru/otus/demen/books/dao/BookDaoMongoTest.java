package ru.otus.demen.books.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.BookComment;
import ru.otus.demen.books.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;


class BookDaoMongoTest extends BaseDaoMongoTest {
    private static final String TOLSTOY_FIRST_NAME = "Лев";
    private static final String TOLSTOY_SURNAME = "Толстой";
    private static final String NOVEL_GENRE_NAME = "Роман";
    private static final String WAR_AND_PEACE_NAME = "Война и мир";
    private static final String ANNA_KARENINA_NAME = "Анна Каренина";
    private static final String COMMENT_TEXT = "Хорошая книга";
    private static final String NO_EXIST_OBJECT_ID = "000000000000000000000000";

    private Author tolstoyAuthor;
    private Genre novelGenre;
    private Book warAndPeace;
    private BookComment warAndPeaceComment;

    @Autowired
    BookDao bookDao;

    @BeforeEach
    void setUp() {
        tolstoyAuthor = new Author(TOLSTOY_FIRST_NAME, TOLSTOY_SURNAME);
        novelGenre = new Genre(NOVEL_GENRE_NAME);
        warAndPeace =
                new Book(WAR_AND_PEACE_NAME, tolstoyAuthor, novelGenre);
        warAndPeaceComment = new BookComment(COMMENT_TEXT);
        mongoTemplate.dropCollection(Book.class);
        mongoTemplate.dropCollection(BookComment.class);
        mongoTemplate.dropCollection(Genre.class);
        mongoTemplate.dropCollection(Author.class);
        mongoTemplate.save(tolstoyAuthor);
        mongoTemplate.save(novelGenre);
        mongoTemplate.save(warAndPeaceComment);
        warAndPeace.getComments().add(warAndPeaceComment);
        mongoTemplate.save(warAndPeace);
    }

    @Test
    @DisplayName("Успешное добавление новой книги")
    void save_ok() {
        Book book = bookDao.save(new Book(ANNA_KARENINA_NAME, tolstoyAuthor, novelGenre)).blockOptional().orElseThrow();
        Book bookFromDb = mongoTemplate.findById(book.getId(), Book.class);
        assertThat(bookFromDb).isNotNull();
        assertThat(bookFromDb).isEqualTo(book);
    }

    @Test
    @DisplayName("Поиск по фамилии возвращает список книг")
    void findBySurname_ok() {
        Collection<Book> books = bookDao.findByAuthorSurname(TOLSTOY_SURNAME).collectList().block();
        assertThat(books).containsExactlyInAnyOrderElementsOf(List.of(warAndPeace));
    }

    @Test
    @DisplayName("Поиск по id возратил книгу")
    void findById_ok() {
        Optional<Book> book = bookDao.findById(warAndPeace.getId()).blockOptional();
        assertThat(book.isPresent()).isTrue();
        assertThat(book.get()).isEqualTo(warAndPeace);
    }

    @Test
    @DisplayName("Поиск по id не нашел книгу")
    void findById_notFound() {
        Optional<Book> book = bookDao.findById(NO_EXIST_OBJECT_ID).blockOptional();
        assertThat(book).isEmpty();
    }

    @Test
    @DisplayName("Успешное удаление комментария по id")
    void deleteById_ok() {
        assertThat(getMongoArraySize("book", "comments", warAndPeace.getId())).isEqualTo(1);
        assertThat(bookDao.removeCommentById(warAndPeaceComment.getId()).block()).isEqualTo(1);
        assertThat(mongoTemplate.count(new Query(), BookComment.class)).isEqualTo(0);
        Book bookFromDb = mongoTemplate.findById(warAndPeace.getId(), Book.class);
        assertThat(bookFromDb).isNotNull();
        assertThat(bookFromDb.getComments()).hasSize(0);
        assertThat(getMongoArraySize("book", "comments", warAndPeace.getId())).isEqualTo(0);
    }

    static class IntegerCount { int count; }

    @SuppressWarnings("SameParameterValue")
    private int getMongoArraySize(String collectionName, String arrayName, String id) {
        AggregationResults<IntegerCount> aggregationResults = mongoTemplate.aggregate(newAggregation(
                match(Criteria.where("_id").is(id)),
                project().and(arrayName).size().as("count")
                        .andExclude("_id")
        ), collectionName, IntegerCount.class);
        List<IntegerCount> results = aggregationResults.getMappedResults();
        return results.get(0).count;
    }

    @Test
    @DisplayName("Комментарий по id не найден")
    void deleteById_notFound() {
        assertThat(bookDao.removeCommentById(NO_EXIST_OBJECT_ID).block()).isEqualTo(0);
    }

    @Test
    @DisplayName("Успешное добавление комментария")
    public void addComment_ok() {
        BookComment newComment = new BookComment("Новый комментарий");
        bookDao.addComment(warAndPeace.getId(), newComment).block();
        assertThat(mongoTemplate.findAll(BookComment.class)).hasSize(2);
        Book dbBook = mongoTemplate.findById(warAndPeace.getId(), Book.class);
        assertThat(dbBook).isNotNull();
        assertThat(dbBook.getComments()).containsExactlyInAnyOrder(warAndPeaceComment, newComment);
    }

    @Test
    @DisplayName("Добавление комментария в несуществующую книгу")
    public void addComment_book_no_exists() {
        BookComment newComment = new BookComment("Новый комментарий");
        assertThatThrownBy(() -> bookDao.addComment(NO_EXIST_OBJECT_ID, newComment).block())
                .isInstanceOf(IdNotFoundException.class);
        assertThat(mongoTemplate.findAll(BookComment.class)).hasSize(1);
    }
}