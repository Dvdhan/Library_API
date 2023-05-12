package Solo_Project.Library_API.domain.memberBook.service;

import Solo_Project.Library_API.domain.memberBook.entity.MemberBook;
import Solo_Project.Library_API.domain.memberBook.repository.MemberBookRepository;
import Solo_Project.Library_API.global.advice.BusinessLogicException;
import Solo_Project.Library_API.global.advice.ExceptionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberBookService {
    @Autowired
    private MemberBookRepository memberBookRepository;

    public MemberBook findMemberBookByMemberIdBookId(Long memberId, Long bookId) {
        return memberBookRepository.findByMember_IdAndBook_IdAndReturnedAtIsNull(memberId,bookId);
    }
    public Page<MemberBook> findMemberBooksByMemberId(Long memberId, int page, int size) {
        return memberBookRepository.findMemberBookByMemberId(memberId, PageRequest.of(page,size));
    }
}
