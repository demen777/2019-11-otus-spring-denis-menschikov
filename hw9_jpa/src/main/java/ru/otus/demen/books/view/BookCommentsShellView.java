package ru.otus.demen.books.view;

import org.springframework.stereotype.Service;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.BookComment;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class BookCommentsShellView implements TextView<Collection<BookComment>> {
    @Override
    public String getView(Collection<BookComment> bookComments) {
        Map<Book, List<BookComment>> commentsByBooks = bookComments.stream()
                .collect(Collectors.groupingBy(BookComment::getBook));
        StringBuilder result = new StringBuilder();
        for (Map.Entry<Book, List<BookComment>> entry : commentsByBooks.entrySet()) {
            String header = String.format("Список комментариев для книги %s %s '%s':\n",
                    entry.getKey().getAuthor().getFirstName(),
                    entry.getKey().getAuthor().getSurname(),
                    entry.getKey().getName());
            result.append(header).append(entry.getValue().stream()
                    .map((bookComment) -> String.format("(id=%d): %s", bookComment.getId(), bookComment.getText()))
                    .collect(Collectors.joining("\n")));
        }
        return result.toString();
    }
}
