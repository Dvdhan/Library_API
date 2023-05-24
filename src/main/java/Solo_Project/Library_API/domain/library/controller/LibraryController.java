package Solo_Project.Library_API.domain.library.controller;

import Solo_Project.Library_API.domain.Page.MultiResponse;
import Solo_Project.Library_API.domain.Page.PageInfo;
import Solo_Project.Library_API.domain.book.entity.Book;
import Solo_Project.Library_API.domain.libraryBook.dto.LibraryBookDto;
import Solo_Project.Library_API.domain.libraryBook.entity.LibraryBook;
import Solo_Project.Library_API.domain.libraryBook.mapper.LibraryBookMapper;
import Solo_Project.Library_API.domain.libraryBook.service.LibraryBookService;
import Solo_Project.Library_API.domain.libraryMember.dto.LibraryMemberDto;
import Solo_Project.Library_API.domain.libraryMember.entity.LibraryMember;
import Solo_Project.Library_API.domain.libraryMember.mapper.LibraryMemberMapper;
import Solo_Project.Library_API.domain.libraryMember.service.LibraryMemberService;
import Solo_Project.Library_API.domain.member.dto.MemberDto;
import Solo_Project.Library_API.domain.member.entity.Member;
import Solo_Project.Library_API.domain.member.mapper.MemberMapper;
import Solo_Project.Library_API.domain.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Validated
@RequestMapping("/library")
@Slf4j
public class LibraryController {

    private MemberService memberService;
    private MemberMapper memberMapper;

    @Autowired
    private LibraryBookService libraryBookService;

    @Autowired
    private LibraryBookMapper libraryBookMapper;

    @Autowired
    private LibraryMemberService libraryMemberService;

    @Autowired
    private LibraryMemberMapper libraryMemberMapper;

    public LibraryController(MemberService memberService,
                             MemberMapper memberMapper) {
        this.memberService = memberService;
        this.memberMapper = memberMapper;
    }

    private final String memberUrl = "http://localhost:8080/members/";
    private final String bookUrl = "http://localhost:8080/books/";

    @GetMapping("/{library-Id}/members")
    public ResponseEntity getMembers(@Positive @PathVariable("library-Id") Long libraryId,
                                     @Positive @RequestParam int page,
                                     @Positive @RequestParam int size) {
        Page<LibraryMember> memberPage = libraryMemberService.findAllLibraryMembersByLibraryId(libraryId,page-1, size);
        PageInfo pageInfo = new PageInfo(memberPage.getNumber(),memberPage.getSize(),
                memberPage.getTotalElements(), memberPage.getTotalPages());

        List<LibraryMember> libraryMembers = memberPage.getContent();
        List<LibraryMemberDto.Response> responses = libraryMemberMapper.libraryMembersToLibraryMembersDtoResponse(libraryMembers);
        responses.stream().forEach(x -> {
            x.setLibraryId(libraryId);
            x.setUrl(memberUrl+x.getMemberId());
        });
        return new ResponseEntity(
                new MultiResponse<>(responses, pageInfo), HttpStatus.OK
        );
    }
//@GetMapping("/{library-Id}/members")
//public ResponseEntity getMembers(@Positive @PathVariable("library-Id") Long libraryId) {
//    List<LibraryMember> memberPage = libraryMemberService.findAllLibraryMembersByLibraryId(libraryId);
//
//    List<LibraryMemberDto.Response> responses = libraryMemberMapper.libraryMembersToLibraryMembersDtoResponse(memberPage);
//    responses.stream().forEach(x -> {
//        x.setLibraryId(libraryId);
//        x.setUrl(memberUrl+x.getMemberId());
//    });
//    return new ResponseEntity(responses, HttpStatus.OK);
//}
    @Transactional
    @GetMapping("/{library-Id}/books")
    public ResponseEntity getBooks(@Positive @PathVariable("library-Id")Long libraryId,
                                   @Positive @RequestParam int page,
                                   @Positive @RequestParam int size) {
        Page<LibraryBook> libraryBookPage = libraryBookService.findAllLibraryBooksByLibraryId(libraryId,page-1,size);

        PageInfo pageInfo = new PageInfo(libraryBookPage.getNumber(), libraryBookPage.getSize(),
                libraryBookPage.getTotalElements(),libraryBookPage.getTotalPages());

        List<LibraryBook> libraryBooks = libraryBookPage.getContent();
        List<LibraryBookDto.Response> responses = libraryBookMapper.libraryBooksToLibraryBooksDtoResponse(libraryBooks);
        responses.stream().forEach(x -> {
            x.setLibraryId(libraryId);
            x.setUrl(bookUrl+x.getLibraryBookId());
        });

        return new ResponseEntity(
                new MultiResponse<>(responses, pageInfo), HttpStatus.OK);
    }
}
