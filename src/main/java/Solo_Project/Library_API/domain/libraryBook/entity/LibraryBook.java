package Solo_Project.Library_API.domain.libraryBook.entity;

import Solo_Project.Library_API.domain.book.entity.Book;
import Solo_Project.Library_API.domain.library.entity.Library;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "library_book",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"libraryId", "bookId"})})
public class LibraryBook {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long libraryBookId;

    @ManyToOne
    @JoinColumn(name = "libraryId")
    private Library library;

    @ManyToOne
    @JoinColumn(name = "bookId")
    private Book book;
}
