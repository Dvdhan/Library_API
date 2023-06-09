package Solo_Project.Library_API.domain.libraryMember.repository;

import Solo_Project.Library_API.domain.libraryMember.entity.LibraryMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryMemberRepository extends JpaRepository<LibraryMember, Long>, LibraryMemberRepositoryCustom {

    @Query("SELECT lm FROM LibraryMember lm WHERE lm.library.id = :libraryId")
    public Page<LibraryMember> findAllLibraryMembersByLibraryId(@Param("libraryId")Long libraryId, Pageable pageable);
    @Query("SELECT lm FROM LibraryMember lm WHERE lm.library.id = :libraryId AND lm.member.id =:memberId")
    LibraryMember findByLibrary_IdAndMember_Id(@Param("libraryId") Long libraryId, @Param("memberId") Long memberId);

    //=================================================================================================================
    // Spring Data JPA 방식
    LibraryMember findByMember_MemberId(Long memberId);

    // JPQL 방식
    @Query("SELECT lm FROM LibraryMember lm WHERE lm.member.id =:memberId")
    LibraryMember findLibraryMember(@Param("memberId")Long memberId);

    // QueryDsl 방식
//    LibraryMember findOne (Long memberId);
}
