package Solo_Project.Library_API.domain.member.service;

import Solo_Project.Library_API.domain.member.entity.Member;
import Solo_Project.Library_API.domain.member.repository.MemberRepository;
import Solo_Project.Library_API.global.BookAuthorityUtils;
import Solo_Project.Library_API.global.MemberRegistrationApplicationEvent;
import Solo_Project.Library_API.global.advice.BusinessLogicException;
import Solo_Project.Library_API.global.advice.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MemberService {

    private PasswordEncoder passwordEncoder;
    private BookAuthorityUtils authorityUtils;
    private MemberRepository memberRepository;
    private ApplicationEventPublisher publisher;

    public MemberService(PasswordEncoder passwordEncoder,
                         BookAuthorityUtils authorityUtils,
                         MemberRepository memberRepository,
                         ApplicationEventPublisher publisher) {
        this.passwordEncoder = passwordEncoder;
        this.authorityUtils = authorityUtils;
        this.memberRepository = memberRepository;
        this.publisher = publisher;
    }

    @Transactional
    public Member createMember(Member member) throws Exception{
        verifyExistedEmail(member.getEmail());

        String encryptedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encryptedPassword);

        List<String> roles = authorityUtils.createRoles(member.getEmail());
        member.setRoles(roles);

        Member createdMember = memberRepository.save(member);
        publisher.publishEvent(new MemberRegistrationApplicationEvent(this, createdMember));
        return createdMember;
    }

    public Member findMember(Long memberId) throws Exception {
        Optional<Member> findMember = memberRepository.findByMemberId(memberId);
        Member foundMember = findMember.orElseThrow(()->new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        return foundMember;
    }

    public Page<Member> findAllMember(Long libraryId, int page, int size) {
        Page<Member> memberPage = memberRepository.findAllByLibraryId(libraryId,PageRequest.of(
            page, size, Sort.by("member.memberId").descending()
        ));
        if(memberPage.isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }else {
            return memberPage;
        }
    }
    public void verifyExistedEmail(String email) throws Exception{
        Optional<Member> foundEmail = memberRepository.findByEmail(email);
        if (foundEmail.isPresent())
            throw new BusinessLogicException(ExceptionCode.EMAIL_IS_ALREADY_EXIST);
    }

}
