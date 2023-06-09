package Solo_Project.Library_API.domain.book.entity;

import Solo_Project.Library_API.domain.libraryBook.entity.LibraryBook;
import Solo_Project.Library_API.domain.memberBook.entity.MemberBook;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="book")
@Getter
@Setter
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bookId;
    @Column
    private String bookTitle;
    @Column
    private String bookAuthor;
    @Column
    private String bookPublisher;

    @Getter
    public enum BookStatus {
        AVAILABLE("대여 가능"),
        UNAVAILABLE("대여 불가");

        @Getter
        private String status;

        BookStatus(String status) {
            this.status = status;
        }
    }

    @OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE)
    private List<MemberBook> memberBooks = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    private List<LibraryBook> libraryBooks = new ArrayList<>();

    public Book(Long bookId, String bookTitle, String bookAuthor, String bookPublisher) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookPublisher = bookPublisher;
    }
}
