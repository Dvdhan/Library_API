package Solo_Project.Library_API.domain.member.repository;

import Solo_Project.Library_API.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    public Optional<Member> findByEmail(String email);
    public Optional<Member> findByMemberId(Long memberId);

    // Spring Data JPA 방식
    public Member findByNameAndEmail(String name, String email);

    // JPQL 방식
    @Query("SELECT m FROM Member m WHERE m.name =:name AND m.email =:email")
    public Member findMember (@Param("name")String name, @Param("email")String email);

}
