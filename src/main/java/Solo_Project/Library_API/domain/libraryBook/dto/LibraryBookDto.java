package Solo_Project.Library_API.domain.libraryBook.dto;

import Solo_Project.Library_API.domain.book.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class LibraryBookDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response{
        long libraryId;
        long bookId;
        long libraryBookId;
        String bookTitle;
        String bookAuthor;
        String bookPublisher;
        Book.BookStatus bookStatus;
        String url;
    }
}
