package Solo_Project.Library_API.member.repository;

import static org.junit.jupiter.api.Assertions.*;

import Solo_Project.Library_API.domain.libraryMember.entity.LibraryMember;
import Solo_Project.Library_API.domain.member.entity.Member;
import Solo_Project.Library_API.domain.member.repository.MemberRepository;
import Solo_Project.Library_API.domain.memberBook.entity.MemberBook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void saveMemberTest() {
        Member member = new Member();
        List<LibraryMember> libraryMembers = new ArrayList<>();
        List<MemberBook> memberBooks = new ArrayList<>();
        member.setName("David");
        member.setEmail("han@han.com");
        member.setPhone("010-1111-1111");
        member.setLibraryId(4L);
        member.setPassword("1234");
        member.setLibraryMembers(libraryMembers);
        member.setMemberBooks(memberBooks);
        Member savedMember = memberRepository.save(member);

        assertNotNull(savedMember);
        assertTrue(member.getName().equals(savedMember.getName()));
        assertTrue(member.getEmail().equals(savedMember.getEmail()));
        assertTrue(member.getPhone().equals(savedMember.getPhone()));

        Member foundMemberByMemberId = memberRepository.findByMemberId(savedMember.getMemberId()).orElse(null);
        assertNotNull(foundMemberByMemberId);
        assertEquals(savedMember.getMemberId(), foundMemberByMemberId.getMemberId());

        Member foundMemberByEmail = memberRepository.findByEmail("han@han.com").orElse(null);
        assertNotNull(foundMemberByEmail);
        assertEquals(savedMember.getEmail(), foundMemberByEmail.getEmail());
    }
}
