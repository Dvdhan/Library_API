package Solo_Project.Library_API.domain.memberBook.repository;

import Solo_Project.Library_API.domain.member.entity.Member;
import Solo_Project.Library_API.domain.memberBook.entity.MemberBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MemberBookRepository extends JpaRepository<MemberBook, Long> {
    int countByMemberAndReturnedAtIsNull(Member member);

    @Query("SELECT COUNT(mb) FROM MemberBook mb WHERE mb.member.memberId = :memberId AND mb.returnedAt IS NULL")
    int countUnreturnedBooksByMember(@Param("memberId") Long memberId);

    @Query("SELECT mb FROM MemberBook mb WHERE mb.member.id =:memberId AND mb.book.id =:bookId AND mb.returnedAt IS NULL")
    MemberBook findByMember_IdAndBook_IdAndReturnedAtIsNull(Long memberId, Long bookId);

    @Query("SELECT mb FROM MemberBook mb WHERE mb.member.id =:memberId")
    List<MemberBook> findByMember_Id(@Param("memberId") Long memberId);

    @Query("SELECT mb FROM MemberBook mb JOIN mb.book b WHERE mb.member.id = :memberId")
    Page<MemberBook> findMemberBookByMemberId (@Param("memberId") Long memberId, Pageable pageable);

    @Transactional
    @Modifying
    @Query("DELETE FROM MemberBook mb WHERE mb.member.id =:memberId")
    void deleteByMember_Id(@Param("memberId") Long memberId);

    //=================================================================================================================
    // Spring Data JPA 버젼
    List<MemberBook> findByMember_MemberIdAndReturnedAtIsNull(Long memberId);
    // JPQL 버젼
    @Query("SELECT mb FROM MemberBook mb WHERE mb.member.id =:memberId AND mb.returnedAt IS NULL")
    List<MemberBook> findByMemberIdAndReturnedAtIsNull(@Param("memberId") Long memberId);

}
