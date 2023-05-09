package Solo_Project.Library_API.domain.member.repository;

import Solo_Project.Library_API.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    public Optional<Member> findByEmail(String email);
    public Optional<Member> findByMemberId(Long memberId);
    public Page<Member> findAllByLibraryId(Long libraryId, Pageable pageable);
}
