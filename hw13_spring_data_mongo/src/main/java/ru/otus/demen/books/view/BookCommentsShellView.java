package ru.otus.demen.books.view;

import org.springframework.stereotype.Service;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.BookComment;

import java.util.Collection;
import java.util.stream.Collectors;


@Service
public class BookCommentsShellView implements TextView<Collection<BookComment>> {
    @Override
    public String getView(Collection<BookComment> bookComments) {
        if (bookComments.isEmpty()) {
            return "Комментариев к книге нет";
        }
        Book book = bookComments.iterator().next().getBook();
        StringBuilder result = new StringBuilder();
        String header = String.format("Список комментариев для книги %s %s '%s':\n",
                book.getAuthor().getFirstName(), book.getAuthor().getSurname(), book.getName());
        result.append(header).append(bookComments.stream()
                .map((bookComment) -> String.format("(id=%d): %s", bookComment.getId(), bookComment.getText()))
                .collect(Collectors.joining("\n")));
        return result.toString();
    }
}
