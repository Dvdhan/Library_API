package Solo_Project.Library_API.domain.book.controller;

import Solo_Project.Library_API.domain.book.entity.Book;
import Solo_Project.Library_API.domain.libraryBook.entity.LibraryBook;
import Solo_Project.Library_API.domain.libraryBook.repository.LibraryBookRepository;
import Solo_Project.Library_API.domain.libraryBook.service.LibraryBookService;
import Solo_Project.Library_API.domain.member.entity.Member;
import Solo_Project.Library_API.domain.member.service.MemberService;
import Solo_Project.Library_API.domain.memberBook.entity.MemberBook;
import Solo_Project.Library_API.domain.memberBook.repository.MemberBookRepository;
import Solo_Project.Library_API.domain.memberBook.service.MemberBookService;
import Solo_Project.Library_API.global.advice.BusinessLogicException;
import Solo_Project.Library_API.global.advice.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@RestController
@Validated
@RequestMapping("/books")
@Slf4j
public class BookController {
    @Autowired
    private LibraryBookRepository libraryBookRepository;

    private final String url = "http://localhost:8080/books/";

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberBookRepository memberBookRepository;

    @Autowired
    private LibraryBookService libraryBookService;

    @Autowired
    private MemberBookService memberBookService;

    @PostMapping("/{library-Id}/{book-Id}/{member-Id}")
    public ResponseEntity postBookRental(@PathVariable("library-Id")@Positive Long libraryId,
                                         @PathVariable("book-Id")@Positive Long bookId,
                                         @PathVariable("member-Id")@Positive Long memberId) throws Exception {
        LibraryBook targetLibraryBook = libraryBookService.findLibraryBookByLibraryIdBookId(libraryId, bookId);

        if(targetLibraryBook == null) {
            throw new BusinessLogicException(ExceptionCode.BOOK_NOT_FOUND);
        }

        if(targetLibraryBook.getBookStatus() == Book.BookStatus.UNAVAILABLE) {
            throw new BusinessLogicException(ExceptionCode.BOOK_IS_ALREADY_RENTED);
        }
        Member targetMember = memberService.findMember(memberId);

        int rentalHistoryCount = memberBookRepository.countByMemberAndReturnedAtIsNull(targetMember);

        if (rentalHistoryCount >= 5) {
            throw new BusinessLogicException(ExceptionCode.MAXIMUM_RENTAL_ALREADY);
        }
        targetLibraryBook.setBookStatus(Book.BookStatus.UNAVAILABLE);
        libraryBookService.saveLibraryBook(targetLibraryBook);

        MemberBook memberBook = new MemberBook();
        memberBook.setLibraryId(libraryId);
        memberBook.setMember(targetMember);
        memberBook.setBook(targetLibraryBook.getBook());
        memberBook.setCreatedAt(LocalDateTime.now());
        memberBookRepository.save(memberBook);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @DeleteMapping("/{library-Id}/{book-Id}/{member-Id}")
    public ResponseEntity deleteBookRental(@PathVariable("library-Id")@Positive Long libraryId,
                                         @PathVariable("book-Id")@Positive Long bookId,
                                         @PathVariable("member-Id")@Positive Long memberId) throws Exception {
        // 1. 반납하려는 책이 해당 도서관에 현재 대여중인 책인지 확인
        // LibraryBook 객체를 주어진 LibraryId, bookId로 찾아서 대여중인지 확인. 만약 AVAILABLE 이라면 대여 중인 책이 아닙니다. 예외처리.
        LibraryBook foundLibraryBook = libraryBookService.findLibraryBookByLibraryIdBookId(libraryId,bookId);
        if (foundLibraryBook.getBookStatus() == Book.BookStatus.AVAILABLE) {
            throw new BusinessLogicException(ExceptionCode.RENTAL_HISTORY_NOT_FOUND);
        }
        // 2. 반납하려는 책의 대여자와 실제 반납하고자 하는 사람이 동일 인물인지 비교.
        // MemberBook 객체를 MemberId, bookId 으로 찾고, 주어진 memberId와 같은지 비교.
        // 3. 같지 않다면 대여자가 다릅니다 예외 처리.
        MemberBook foundMemberBook = memberBookService.findMemberBookByMemberIdBookId(memberId, bookId);

        if (foundMemberBook == null) {
            throw new BusinessLogicException(ExceptionCode.RENTAL_HISTORY_NOT_FOUND);
        }

        // 4. 찾은 LibraryBook의 상태를 AVAILABLE으로 상태 변경, returnedAt을 now로 변경 후 저장.
        foundLibraryBook.setBookStatus(Book.BookStatus.AVAILABLE);
        libraryBookRepository.save(foundLibraryBook);
        foundMemberBook.setReturnedAt(LocalDateTime.now());
        memberBookRepository.save(foundMemberBook);

        return new ResponseEntity(HttpStatus.OK);

    }
}
