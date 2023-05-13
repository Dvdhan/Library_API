package Solo_Project.Library_API.domain.book.controller;

import Solo_Project.Library_API.domain.book.dto.BookDto;
import Solo_Project.Library_API.domain.book.entity.Book;
import Solo_Project.Library_API.domain.book.mapper.BookMapper;
import Solo_Project.Library_API.domain.book.service.BookService;
import Solo_Project.Library_API.domain.libraryBook.entity.LibraryBook;
import Solo_Project.Library_API.domain.libraryBook.repository.LibraryBookRepository;
import Solo_Project.Library_API.domain.libraryBook.service.LibraryBookService;
import Solo_Project.Library_API.domain.libraryMember.entity.LibraryMember;
import Solo_Project.Library_API.domain.libraryMember.repository.LibraryMemberRepository;
import Solo_Project.Library_API.domain.libraryMember.service.LibraryMemberService;
import Solo_Project.Library_API.domain.member.entity.Member;
import Solo_Project.Library_API.domain.member.service.MemberService;
import Solo_Project.Library_API.domain.memberBook.dto.MemberBookDto;
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
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@RestController
@Validated
@RequestMapping("/books")
@Slf4j
public class BookController {
    @Autowired
    private LibraryMemberRepository libraryMemberRepository;
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

    @Autowired
    private BookService bookService;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private LibraryMemberService libraryMemberService;

    @PostMapping("/{library-Id}/{book-Id}/{member-Id}")
    public ResponseEntity postBookRental(@PathVariable("library-Id")@Positive Long libraryId,
                                         @PathVariable("book-Id")@Positive Long bookId,
                                         @PathVariable("member-Id")@Positive Long memberId) throws Exception {

        LibraryMember libraryMember = libraryMemberService.findByLibrary_IdAndMember_Id(libraryId, memberId);
        if(libraryMember == null) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }

        if (libraryMember.getOverdueDays() != null && libraryMember.getOverdueDays() > 0) {
            LocalDate now = LocalDate.now();
            Long daysSinceLastOverdue = ChronoUnit.DAYS.between(libraryMember.getLastOverdueDate(), now);
            if(daysSinceLastOverdue < libraryMember.getOverdueDays()){
                String message = "당신은" + libraryMember.getOverdueDays() + "일의 연체 기록이 있습니다. 이 기간동안은 대여할 수 없습니다.";
                return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
            }
        }
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
        memberBook.setCreatedAt(LocalDate.now());
        memberBook.setDueReturn(memberBook.getCreatedAt().plusDays(14));
        memberBookRepository.save(memberBook);

        MemberBookDto.RentalResponse response = new MemberBookDto.RentalResponse();
        response.setMemberId(memberBook.getMember().getMemberId());
        response.setLibraryId(memberBook.getLibraryId());
        response.setBookId(memberBook.getBook().getBookId());
        response.setCreatedAt(memberBook.getCreatedAt());
        response.setDueReturnDate(memberBook.getDueReturn());

        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }
    @GetMapping("/{book-Id}")
    public ResponseEntity findBook(@PathVariable("book-Id")@Positive Long bookId) {
        Book foundBook = bookService.findBookByBookId(bookId);
        BookDto.SingleResponse response = bookMapper.bookToBookDtoResponse(foundBook);
        return new ResponseEntity<>(response, HttpStatus.OK);
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

        Long daysRented = ChronoUnit.DAYS.between(foundMemberBook.getCreatedAt(), LocalDate.now());
        Long overdueDays = 0L;
        if (daysRented > 14) {
            overdueDays = daysRented - 14;
            foundMemberBook.setOverdueDays(overdueDays);
        }
        LibraryMember libraryMember = libraryMemberService.findByLibrary_IdAndMember_Id(libraryId,memberId);
        if (libraryMember == null) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }
        libraryMember.setOverdueDays(overdueDays);
        libraryMemberRepository.save(libraryMember);

        // 4. 찾은 LibraryBook의 상태를 AVAILABLE으로 상태 변경, returnedAt을 now로 변경 후 저장.
        foundLibraryBook.setBookStatus(Book.BookStatus.AVAILABLE);
        libraryBookRepository.save(foundLibraryBook);

        foundMemberBook.setReturnedAt(LocalDate.now());
        memberBookRepository.save(foundMemberBook);

        MemberBookDto.ReturnResponse response = new MemberBookDto.ReturnResponse();
        response.setMemberId(foundMemberBook.getMember().getMemberId());
        response.setLibraryId(foundMemberBook.getLibraryId());
        response.setBookId(foundMemberBook.getBook().getBookId());
        response.setCreatedAt(foundMemberBook.getCreatedAt());
        response.setReturnedAt(foundMemberBook.getReturnedAt());
        response.setMessage("반납이 완료되었습니다.");
        if(foundMemberBook.getOverdueDays() == null) {
            response.setOverdueDays(0L);
        }
        if (foundMemberBook.getOverdueDays() != null && foundMemberBook.getOverdueDays() >0) {
            response.setOverdueDays(foundMemberBook.getOverdueDays());
            response.setMessage("연체가 발생되어 "+response.getOverdueDays()+"일 동안 대여할 수 없습니다." +
                    response.getReturnedAt().plusDays(response.getOverdueDays())+"일부터 대여할 수 있습니다.");
        }
        return new ResponseEntity(response, HttpStatus.OK);

    }
}
