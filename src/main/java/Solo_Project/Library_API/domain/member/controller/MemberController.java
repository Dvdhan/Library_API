package Solo_Project.Library_API.domain.member.controller;

import Solo_Project.Library_API.domain.Page.MultiResponse;
import Solo_Project.Library_API.domain.Page.PageInfo;
import Solo_Project.Library_API.domain.library.repository.LibraryRepository;
import Solo_Project.Library_API.domain.library.service.LibraryService;
import Solo_Project.Library_API.domain.libraryMember.entity.LibraryMember;
import Solo_Project.Library_API.domain.libraryMember.repository.LibraryMemberRepository;
import Solo_Project.Library_API.domain.libraryMember.service.LibraryMemberService;
import Solo_Project.Library_API.domain.member.dto.MemberDto;
import Solo_Project.Library_API.domain.member.entity.Member;
import Solo_Project.Library_API.domain.member.mapper.MemberMapper;
import Solo_Project.Library_API.domain.member.repository.MemberRepository;
import Solo_Project.Library_API.domain.member.service.MemberService;
import Solo_Project.Library_API.domain.memberBook.dto.MemberBookDto;
import Solo_Project.Library_API.domain.memberBook.entity.MemberBook;
import Solo_Project.Library_API.domain.memberBook.mapper.MemberBookMapper;
import Solo_Project.Library_API.domain.memberBook.repository.MemberBookRepository;
import Solo_Project.Library_API.domain.memberBook.service.MemberBookService;
import Solo_Project.Library_API.global.advice.BusinessLogicException;
import Solo_Project.Library_API.global.advice.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Validated
@RequestMapping("/members")
@Slf4j
public class MemberController {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberBookRepository memberBookRepository;
    @Autowired
    private MemberBookService memberBookService;

    private final String url = "http://localhost:8080/members/";

    private MemberMapper mapper;
    private MemberService memberService;
    private LibraryService libraryService;
    private LibraryMemberRepository libraryMemberRepository;

    @Autowired
    private LibraryMemberService libraryMemberService;

    @Autowired
    private MemberBookMapper memberBookMapper;

    public MemberController(MemberMapper mapper,
                            MemberService memberService,
                            LibraryService libraryService,
                            LibraryMemberRepository libraryMemberRepository) {
        this.mapper = mapper;
        this.memberService = memberService;
        this.libraryService = libraryService;
        this.libraryMemberRepository = libraryMemberRepository;
    }

    @Transactional
    @PostMapping("/{library-Id}")
    public ResponseEntity postMember(@PathVariable("library-Id")@Positive Long libraryId,
                                     @RequestBody @Valid MemberDto.Post post) throws Exception {

        Member member = mapper.memberDtoPostToMember(post);
        member.setLibraryId(libraryId);
        Member createdMember = memberService.createMember(member);

        LibraryMember libraryMember = new LibraryMember();
        libraryMember.setMember(member);
        libraryMember.setLibrary(libraryService.findLibrary(libraryId));
        member.getLibraryMembers().add(libraryMember);

        libraryMemberRepository.save(libraryMember);

        MemberDto.Response response = mapper.memberToMemberDtoResponse(createdMember);
        response.setUrl(url + response.getMemberId());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Expose-Headers","Authorization");
        return new ResponseEntity(response, headers, HttpStatus.CREATED);
    }
    @GetMapping("/{library-Id}/{member-Id}")
    public ResponseEntity getMember(@PathVariable("member-Id")@Positive Long memberId) throws Exception {
        Member member = memberService.findMember(memberId);
        MemberDto.Response response = mapper.memberToMemberDtoResponse(member);
        response.setUrl(url+response.getMemberId());
        return new ResponseEntity(response, HttpStatus.OK);
    }
    @DeleteMapping("/{library-Id}/{member-Id}")
    public ResponseEntity deleteMember(@PathVariable("library-Id")@Positive Long libraryId,
                                       @PathVariable("member-Id")@Positive long memberId) throws Exception {

        LibraryMember libraryMember = libraryMemberService.findByLibrary_IdAndMember_Id(libraryId, memberId);
        if (libraryMember == null) {
            throw new BusinessLogicException(ExceptionCode.DATA_IS_EMPTY);
        }

        List<MemberBook> existedRental = memberBookRepository.findByMember_Id(memberId);
        boolean hasUnreturnedBooks = existedRental.stream()
                .anyMatch(memberBook -> memberBook.getReturnedAt() == null);
        if (hasUnreturnedBooks) {
            throw new BusinessLogicException(ExceptionCode.RENTAL_BOOK_EXIST);
        }

        memberBookRepository.deleteByMember_Id(memberId);
        memberService.deleteMember(memberId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/history/{member-Id}")
    public ResponseEntity getRentalHistory(@PathVariable("member-Id")@Positive long memberId,
                                           @Positive @RequestParam int page,
                                           @Positive @RequestParam int size) throws Exception {

        Page<MemberBook> memberBookPage = memberBookService.findMemberBooksByMemberId(memberId,page-1,size);

        List<MemberBook> memberBooks = memberBookPage.getContent();

        if (memberBooks.isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.RENTAL_HISTORY_NOT_FOUND);
        }

        PageInfo pageInfo = new PageInfo(memberBookPage.getNumber(), memberBookPage.getSize(),
                memberBookPage.getTotalElements(), memberBookPage.getTotalPages());

        List<MemberBookDto.Response> responses = memberBookMapper.memberBooksToMemberBooksDtoResponse(memberBooks);

        return new ResponseEntity(
                new MultiResponse<>(responses, pageInfo),HttpStatus.OK);
    }
}
