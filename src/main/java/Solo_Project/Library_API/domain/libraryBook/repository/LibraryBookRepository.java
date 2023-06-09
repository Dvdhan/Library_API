package Solo_Project.Library_API.domain.libraryBook.repository;

import Solo_Project.Library_API.domain.book.entity.Book;
import Solo_Project.Library_API.domain.libraryBook.entity.LibraryBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibraryBookRepository extends JpaRepository<LibraryBook, Long> {
    @Query("SELECT lb FROM LibraryBook lb JOIN lb.book b WHERE lb.library.id = :libraryId ORDER BY b.bookTitle ASC")
    public Page<LibraryBook> findAllLibraryBooksByLibraryId(@Param("libraryId") Long libraryId, Pageable pageable);

    @Query("SELECT lb FROM LibraryBook lb WHERE lb.book.id = :bookId")
    public Page<LibraryBook> findAllLibraryBooksByBookId(@Param("bookId")Long bookId, Pageable pageable);

    // Spring Data JPA LibraryBook 조회
    public LibraryBook findByLibrary_LibraryIdAndBook_BookId(Long libraryId, Long bookId);

    // JPQL 버젼 LibraryBook 조회
    @Query("SELECT lb FROM LibraryBook lb JOIN lb.book b WHERE lb.library.id = :libraryId AND lb.book.id = :bookId")
    public LibraryBook findLibraryBookByLibraryIdBookId(@Param("libraryId") Long libraryId, @Param("bookId") Long bookId);

    // ===================================================================================================================
    // 회원 이름, 이메일을 지닌 회원이 대여한 아직 반납안된 libraryBook 정보가져오기



}
