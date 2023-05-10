package Solo_Project.Library_API.domain.library.controller;

import Solo_Project.Library_API.domain.Page.MultiResponse;
import Solo_Project.Library_API.domain.Page.PageInfo;
import Solo_Project.Library_API.domain.book.dto.BookDto;
import Solo_Project.Library_API.domain.book.entity.Book;
import Solo_Project.Library_API.domain.book.mapper.BookMapper;
import Solo_Project.Library_API.domain.book.repository.BookRepository;
import Solo_Project.Library_API.domain.book.service.BookService;
import Solo_Project.Library_API.domain.member.dto.MemberDto;
import Solo_Project.Library_API.domain.member.entity.Member;
import Solo_Project.Library_API.domain.member.mapper.MemberMapper;
import Solo_Project.Library_API.domain.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
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
    private BookService bookService;
    private BookMapper bookMapper;

    public LibraryController(MemberService memberService,
                             MemberMapper memberMapper,
                             BookService bookService,
                             BookMapper bookMapper) {
        this.memberService = memberService;
        this.memberMapper = memberMapper;
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    private final String memberUrl = "http://localhost:8080/members/";
    private final String bookUrl = "http://localhost:8080/books/";

    @GetMapping("/{library-Id}/members")
    public ResponseEntity getMembers(@Positive @PathVariable("library-Id") Long libraryId,
                                     @Positive @RequestParam int page,
                                     @Positive @RequestParam int size) {
        Page<Member> memberPage = memberService.findAllMember(libraryId,page-1, size);
        PageInfo pageInfo = new PageInfo(memberPage.getNumber(),memberPage.getSize(),
                memberPage.getTotalElements(), memberPage.getTotalPages());

        List<Member> members = memberPage.getContent();
        List<MemberDto.Response> responses = memberMapper.membersToMemberDtoResponse(members);
        responses.stream().forEach(x -> {
            x.setLibraryId(libraryId);
            x.setUrl(memberUrl+x.getMemberId());
        });

        return new ResponseEntity(
                new MultiResponse<>(responses, pageInfo), HttpStatus.OK
        );
    }
    @GetMapping("/{library-Id}/books")
    public ResponseEntity getBooks(@Positive @PathVariable("library-Id")Long libraryId,
                                   @Positive @RequestParam int page,
                                   @Positive @RequestParam int size) {
        Page<Book> bookPage = bookService.findAllBook(libraryId,page-1,size);
        PageInfo pageInfo = new PageInfo(bookPage.getNumber(), bookPage.getSize(),
                bookPage.getTotalElements(),bookPage.getTotalPages());

        List<Book> books = bookPage.getContent();
        List<BookDto.Response> responses = bookMapper.booksToBookDtoResponse(books);
        responses.stream().forEach(x -> {
            x.setLibraryId(libraryId);
            x.setUrl(bookUrl+x.getBookId());
        });

        return new ResponseEntity(
                new MultiResponse<>(responses, pageInfo), HttpStatus.OK
        );
    }

}
