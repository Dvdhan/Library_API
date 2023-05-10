package Solo_Project.Library_API.domain.libraryMember.repository;

import Solo_Project.Library_API.domain.libraryMember.entity.LibraryMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryMemberRepository extends JpaRepository<LibraryMember, Long> {
}
