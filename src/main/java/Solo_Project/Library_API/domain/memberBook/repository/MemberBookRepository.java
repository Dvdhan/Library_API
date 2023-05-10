package Solo_Project.Library_API.domain.memberBook.repository;

import Solo_Project.Library_API.domain.member.entity.Member;
import Solo_Project.Library_API.domain.memberBook.entity.MemberBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberBookRepository extends JpaRepository<MemberBook, Long> {
    int countByMember(Member member);
}
