package Solo_Project.Library_API.domain.book.dto;

import Solo_Project.Library_API.domain.book.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class BookDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        long libraryId;
        long bookId;
        String bookTitle;
        String bookAuthor;
        String bookPublisher;
        Book.BookStatus bookStatus = Book.BookStatus.AVAILABLE;
        String url;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SingleResponse {
        long bookId;
        String bookTitle;
        String bookAuthor;
        String bookPublisher;
    }
}
