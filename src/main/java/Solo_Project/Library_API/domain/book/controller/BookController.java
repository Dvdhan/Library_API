package Solo_Project.Library_API.domain.book.controller;

import Solo_Project.Library_API.domain.Page.MultiResponse;
import Solo_Project.Library_API.domain.Page.PageInfo;
import Solo_Project.Library_API.domain.book.entity.Book;
import Solo_Project.Library_API.domain.libraryBook.dto.LibraryBookDto;
import Solo_Project.Library_API.domain.libraryBook.entity.LibraryBook;
import Solo_Project.Library_API.domain.libraryBook.mapper.LibraryBookMapper;
import Solo_Project.Library_API.domain.libraryBook.repository.LibraryBookRepository;
import Solo_Project.Library_API.domain.libraryBook.service.LibraryBookService;
import Solo_Project.Library_API.domain.libraryMember.entity.LibraryMember;
import Solo_Project.Library_API.domain.libraryMember.repository.LibraryMemberRepository;
import Solo_Project.Library_API.domain.libraryMember.service.LibraryMemberService;
import Solo_Project.Library_API.domain.member.entity.Member;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

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
    private LibraryMemberService libraryMemberService;

    @Autowired
    private MemberBookMapper memberBookMapper;

    @Autowired
    private LibraryBookMapper libraryBookMapper;

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

            // daysSinceLastOverdue = 지난번 연체 발생한 날짜와 오늘을 DAY 기준으로 비교한 Long 변수.
            Long daysSinceLastOverdue = ChronoUnit.DAYS.between(libraryMember.getLastOverdueDate(), now);
            // libraryMember.getOverdueDays = penaltyDays
            // = 대여 날짜와 반납 날짜를 계산하여 14일이 넘을 경우 실제 값에서 14를 뺀 Long 변수. 즉 패널티 적용 기간.
            if(daysSinceLastOverdue < libraryMember.getOverdueDays()){
                LocalDate availableDate = libraryMember.getLastOverdueDate().plusDays(libraryMember.getOverdueDays());
                String message = "당신은" + libraryMember.getOverdueDays() + "일의 연체 기록이 있습니다. 이 기간동안은 대여할 수 없습니다." +
                        availableDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+"부터 대여할 수 있습니다.";
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

        MemberBookDto.Response response = memberBookMapper.memberBookToMemberBookDtoResponse(memberBook);

        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }
    @DeleteMapping("/{library-Id}/{book-Id}/{member-Id}")
    public ResponseEntity deleteBookRental(@PathVariable("library-Id")@Positive Long libraryId,
                                         @PathVariable("book-Id")@Positive Long bookId,
                                         @PathVariable("member-Id")@Positive Long memberId) throws Exception {

        LibraryBook foundLibraryBook = libraryBookService.findLibraryBookByLibraryIdBookId(libraryId,bookId);
        if (foundLibraryBook.getBookStatus() == Book.BookStatus.AVAILABLE) {
            throw new BusinessLogicException(ExceptionCode.RENTAL_HISTORY_NOT_FOUND);
        }

        MemberBook foundMemberBook = memberBookService.findMemberBookByMemberIdBookId(memberId, bookId);
        if (foundMemberBook == null) {
            throw new BusinessLogicException(ExceptionCode.RENTAL_HISTORY_NOT_FOUND);
        }
        // daysRented = 대여날짜 기준으로 현재 날을 DAY 단위로 비교한 변수.
        Long daysRented = ChronoUnit.DAYS.between(foundMemberBook.getCreatedAt(), LocalDate.now());
        Long overdueDays = 0L;
        // 만일 daysRented 가 대여 기간인 14 일보다 길다면,
        if (daysRented > 14) {
            // daysRented - 14 의 값을 연체 패널티 변수에 담음.
            overdueDays = daysRented - 14;
            foundMemberBook.setOverdueDays(overdueDays);
        }
        LibraryMember libraryMember = libraryMemberService.findByLibrary_IdAndMember_Id(libraryId,memberId);
        if (libraryMember == null) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }

        libraryMember.setOverdueDays(overdueDays);
        libraryMemberRepository.save(libraryMember);

        foundLibraryBook.setBookStatus(Book.BookStatus.AVAILABLE);
        libraryBookRepository.save(foundLibraryBook);

        foundMemberBook.setReturnedAt(LocalDate.now());
        memberBookRepository.save(foundMemberBook);

        MemberBookDto.ReturnResponse response = memberBookMapper.ReturnMemberBookToMemberBookDtoResponse(foundMemberBook);

        if(foundMemberBook.getOverdueDays() == null) {
            response.setOverdueDays(0L);
            response.setMessage("반납이 완료되었습니다.");
        }else if (foundMemberBook.getOverdueDays() != null && foundMemberBook.getOverdueDays() >0) {
            response.setOverdueDays(foundMemberBook.getOverdueDays());
            String availableDate = response.getReturnedAt().plusDays(response.getOverdueDays()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            response.setMessage("연체가 발생되어 "+response.getOverdueDays()+"일 동안 대여할 수 없습니다. " +
                   availableDate+" 일부터 대여할 수 있습니다.");
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/{library-Id}/{book-Id}")
    public ResponseEntity getBook(@PathVariable("library-Id")@Positive Long libraryId,
                                   @PathVariable("book-Id")@Positive Long bookId) {
        LibraryBook libraryBook = libraryBookService.findLibraryBookByLibraryIdBookId(libraryId, bookId);
        LibraryBookDto.Response response = libraryBookMapper.libraryBookToLibraryBookDtoResponse(libraryBook);
        response.setUrl(url+libraryId+"/"+bookId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // JUnit으로 RestDocs 하려면 이 메서드는 주석처리하고 해야함.
    @Transactional
    @GetMapping("/{bookId}")
    public ResponseEntity getABook(@PathVariable("bookId")@Positive Long bookId,
                                   @Positive @RequestParam int page,
                                   @Positive @RequestParam int size){
        Page<LibraryBook> libraryBookPage = libraryBookService.findAllLibraryBooksByBookId(bookId, page-1, size);

        PageInfo pageInfo = new PageInfo(
                libraryBookPage.getNumber(),
                libraryBookPage.getSize(),
                libraryBookPage.getTotalElements(),
                libraryBookPage.getTotalPages());

        List<LibraryBook> libraryBooks = libraryBookPage.getContent();
        List<LibraryBookDto.Response> responses = libraryBookMapper.libraryBooksToLibraryBooksDtoResponse(libraryBooks);
        responses.stream().forEach(x->x.setUrl(url+bookId));

        return new ResponseEntity(
                new MultiResponse<>(responses, pageInfo),HttpStatus.OK);
    }
}
