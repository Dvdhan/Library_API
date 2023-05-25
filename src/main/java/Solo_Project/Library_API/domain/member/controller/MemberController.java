package Solo_Project.Library_API.domain.member.controller;

import Solo_Project.Library_API.domain.Page.MultiResponse;
import Solo_Project.Library_API.domain.Page.PageInfo;
import Solo_Project.Library_API.domain.library.service.LibraryService;
import Solo_Project.Library_API.domain.libraryBook.repository.LibraryBookRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/members")
@Slf4j
public class MemberController {
    @Autowired
    private LibraryBookRepository libraryBookRepository;
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
        if(!existedRental.isEmpty()) {
            List<String> unreturnedBookTitles = existedRental.stream()
                    .map(x -> x.getBook().getBookTitle())
                    .collect(Collectors.toList());
            String message = "현재 {"+String.join(", ",unreturnedBookTitles)+"} 도서가 반납되지 않아 탈퇴 처리가 불가능합니다.";
            return new ResponseEntity(message,HttpStatus.FORBIDDEN);
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

        List<MemberBookDto.RentalHistoryResponse> responses = memberBookMapper.HistoryMemberBooksToMemberBooksDtoResponse(memberBooks);

        return new ResponseEntity(
                new MultiResponse<>(responses, pageInfo),HttpStatus.OK);
    }
    //================================================================================================================================
    // 회원 이름, 이메일을 지닌 회원이 대여한 아직 반납안된 libraryBook 정보가져오기
    // 1. 주어진 회원 이름, 이메일을 지닌 회원 찾기.
    // 2. 찾은 회원이 가지고 있는 libraryBook 찾고, 그 중에 returnedAt 필드가 null 인 libraryBook List 찾기.
    @GetMapping("/SpringDataJPA/history/{name}/{email}")
    public ResponseEntity SpringDataJPAgetHistory(@PathVariable("name")String name,
                                                  @PathVariable("email")String email) {
        Member member = memberRepository.findByNameAndEmail(name,email);
        if(member == null) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }

        LibraryMember libraryMember = libraryMemberRepository.findByMember_MemberId(member.getMemberId());
        if(libraryMember == null || libraryMember.getMember() == null) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }

        List<MemberBook> memberBooks = memberBookRepository.findByMember_MemberIdAndReturnedAtIsNull(libraryMember.getMember().getMemberId());
        if (memberBooks.isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.RENTAL_HISTORY_NOT_FOUND);
        }
        List<MemberBookDto.RentalHistoryResponse> responses = memberBookMapper.HistoryMemberBooksToMemberBooksDtoResponse(memberBooks);
        return new ResponseEntity(responses,HttpStatus.OK);
    }
    @GetMapping("/JPQL/history/{name}/{email}")
    public ResponseEntity JPQLgetHistory(@PathVariable("name")String name,
                                         @PathVariable("email")String email) {
        Member member = memberRepository.findByNameAndEmail(name, email);
        if (member == null) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }
        LibraryMember libraryMember = libraryMemberRepository.findByMember_MemberId(member.getMemberId());
        if(libraryMember == null || libraryMember.getMember() == null) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }

        List<MemberBook> memberBooks = memberBookRepository.findByMemberIdAndReturnedAtIsNull(libraryMember.getMember().getMemberId());
        List<MemberBookDto.RentalHistoryResponse> responses = memberBookMapper.HistoryMemberBooksToMemberBooksDtoResponse(memberBooks);
        return new ResponseEntity(responses, HttpStatus.OK);
    }




}
