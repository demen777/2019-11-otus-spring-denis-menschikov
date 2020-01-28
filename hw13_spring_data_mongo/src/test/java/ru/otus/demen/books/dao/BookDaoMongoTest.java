package ru.otus.demen.books.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.BookComment;
import ru.otus.demen.books.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


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
        Book book = bookDao.save(new Book(ANNA_KARENINA_NAME, tolstoyAuthor, novelGenre));
        Book bookFromDb = mongoTemplate.findById(book.getId(), Book.class);
        assertThat(bookFromDb).isNotNull();
        assertThat(bookFromDb).isEqualTo(book);
    }

    @Test
    @DisplayName("Поиск по фамилии возвращает список книг")
    void findBySurname_ok() {
        Collection<Book> books = bookDao.findByAuthorSurname(TOLSTOY_SURNAME);
        assertThat(books).containsExactlyInAnyOrderElementsOf(List.of(warAndPeace));
    }

    @Test
    @DisplayName("Поиск по id возратил книгу")
    void findById_ok() {
        Optional<Book> book = bookDao.findById(warAndPeace.getId());
        assertThat(book.isPresent()).isTrue();
        assertThat(book.get()).isEqualTo(warAndPeace);
    }

    @Test
    @DisplayName("Поиск по id не нашел книгу")
    void findById_notFound() {
        Optional<Book> book = bookDao.findById(NO_EXIST_OBJECT_ID);
        assertThat(book).isEmpty();
    }

    @Test
    @DisplayName("Успешное удаление комментария по id")
    void deleteById_ok() {
        assertThat(bookDao.removeCommentById(warAndPeaceComment.getId())).isEqualTo(1);
        assertThat(mongoTemplate.findById(warAndPeaceComment.getId(), BookComment.class)).isNull();
        Book bookFromDb = mongoTemplate.findById(warAndPeace.getId(), Book.class);
        assertThat(bookFromDb).isNotNull();
        assertThat(bookFromDb.getComments()).hasSize(0);
    }

    @Test
    @DisplayName("Комментарий по id не найден")
    void deleteById_notFound() {
        assertThat(bookDao.removeCommentById(NO_EXIST_OBJECT_ID)).isEqualTo(0);
    }

    @Test
    @DisplayName("Успешное добавление комментария")
    public void addComment_ok() {
        BookComment newComment = new BookComment("Новый комментарий");
        bookDao.addComment(warAndPeace.getId(), newComment);
        assertThat(mongoTemplate.findAll(BookComment.class)).hasSize(2);
        Book dbBook = mongoTemplate.findById(warAndPeace.getId(), Book.class);
        assertThat(dbBook).isNotNull();
        assertThat(dbBook.getComments()).containsExactlyInAnyOrder(warAndPeaceComment, newComment);
    }

    @Test
    @DisplayName("Добавление комментария в несуществующую книгу")
    public void addComment_book_no_exists() {
        BookComment newComment = new BookComment("Новый комментарий");
        assertThatThrownBy(() -> bookDao.addComment(NO_EXIST_OBJECT_ID, newComment))
                .isInstanceOf(IdNotFoundException.class);
        assertThat(mongoTemplate.findAll(BookComment.class)).hasSize(1);
    }
}