package ru.otus.demen.books.dao;

import org.hibernate.boot.model.naming.Identifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CustomPhysicalNamingStrategyTest {
    private final CustomPhysicalNamingStrategy namingStrategy = new CustomPhysicalNamingStrategy();

    @Test
    @DisplayName("Проверка преобразования к snake case и добавления s в конец имени таблицы")
    void toPhysicalTableName() {
        assertThat(namingStrategy.toPhysicalTableName(Identifier.toIdentifier("BookComment"), null))
                .isEqualTo(Identifier.toIdentifier("book_comments"));
    }

    @Test
    @DisplayName("Проверка преобразования к snake case для поля таблицы")
    void toPhysicalColumnName() {
        assertThat(namingStrategy.toPhysicalColumnName(Identifier.toIdentifier("bookAuthor"), null))
                .isEqualTo(Identifier.toIdentifier("book_author"));
    }
}