package Solo_Project.Library_API.domain.book.repository;

import Solo_Project.Library_API.domain.book.entity.Book;
import Solo_Project.Library_API.domain.libraryBook.entity.LibraryBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT lb.book FROM LibraryBook lb JOIN lb.book b WHERE lb.library.id = :libraryId ORDER BY b.bookTitle ASC")
    Page<Book> findAllBooksByLibraryId (@Param("libraryId") Long libraryId, Pageable pageable);
    @Query("SELECT lb FROM LibraryBook lb JOIN lb.book b WHERE lb.library.id = :libraryId ORDER BY b.bookTitle ASC")
    public Page<LibraryBook> findAllLibraryBooksByLibraryId(@Param("libraryId") Long libraryId, Pageable pageable);


}
