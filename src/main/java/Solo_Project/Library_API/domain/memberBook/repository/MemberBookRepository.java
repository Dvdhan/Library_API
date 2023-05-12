package Solo_Project.Library_API.domain.memberBook.repository;

import Solo_Project.Library_API.domain.member.entity.Member;
import Solo_Project.Library_API.domain.memberBook.entity.MemberBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberBookRepository extends JpaRepository<MemberBook, Long> {
//    int countByMember(Member member);
    int countByMemberAndReturnedAtIsNull(Member member);

    @Query("SELECT mb FROM MemberBook mb WHERE mb.member.id =:memberId AND mb.book.id =:bookId AND mb.returnedAt IS NULL")
    MemberBook findByMember_IdAndBook_IdAndReturnedAtIsNull(Long memberId, Long bookId);
}
