package ru.otus.demen.books.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.Genre;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = AclSecurityTestConfiguration.class)
@WithMockUser(roles = "ADMIN")
class BookAclCreatorImplTest {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    BookAclCreator bookAclCreator;

    private final static Author TOLSTOY = new Author(1L, "Лев", "Толстой");
    private final static Genre NOVEL = new Genre(1L, "Роман");

    @Test
    @DisplayName("Успешное добавление в таблицу acl_object_identity записи для новой книги")
    @Transactional
    void createAcl_ok() {
        final String SELECT_COUNT_ACL_OBJECT_IDENTITY_FOR_NEW_BOOK
            = "select count(*) from acl_object_identity where object_id_identity = 1000";
        Book book = new Book(1000L, "New book", TOLSTOY, NOVEL);
        Long countBefore = jdbcTemplate.queryForObject(SELECT_COUNT_ACL_OBJECT_IDENTITY_FOR_NEW_BOOK, Long.class);
        assertThat(countBefore).isEqualTo(0L);
        bookAclCreator.createAcl(book);
        Long countAfter = jdbcTemplate.queryForObject(SELECT_COUNT_ACL_OBJECT_IDENTITY_FOR_NEW_BOOK, Long.class);
        assertThat(countAfter).isEqualTo(1L);
    }
}