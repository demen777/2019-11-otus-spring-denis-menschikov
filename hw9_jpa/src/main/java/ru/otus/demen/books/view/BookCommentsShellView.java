package ru.otus.demen.books.view;

import org.springframework.stereotype.Service;
import ru.otus.demen.books.model.Book;

import java.util.stream.Collectors;


@Service
public class BookCommentsShellView implements TextView<Book> {
    @Override
    public String getView(Book book) {
        StringBuilder result = new StringBuilder();
        String header = String.format("Список комментариев для книги %s %s '%s':\n",
                book.getAuthor().getFirstName(), book.getAuthor().getSurname(), book.getName());
        result.append(header).append(book.getBookComments().stream()
                .map((bookComment) -> String.format("(id=%d): %s", bookComment.getId(), bookComment.getText()))
                .collect(Collectors.joining("\n")));
        return result.toString();
    }
}
