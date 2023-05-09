package Solo_Project.Library_API.domain.book.repository;

import Solo_Project.Library_API.domain.book.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findAllByLibraryId (Long libraryId, Pageable pageable);
}
