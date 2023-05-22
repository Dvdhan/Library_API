package Solo_Project.Library_API.domain.library.repository;

import Solo_Project.Library_API.domain.book.entity.Book;
import Solo_Project.Library_API.domain.library.entity.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryRepository extends JpaRepository<Library, Long> {
    public Library findByLibraryId(Long libraryId);
}
