package Solo_Project.Library_API.domain.book.controller;

import Solo_Project.Library_API.domain.book.entity.Book;
import Solo_Project.Library_API.domain.book.service.BookService;
import Solo_Project.Library_API.domain.library.service.LibraryService;
import Solo_Project.Library_API.domain.libraryBook.entity.LibraryBook;
import Solo_Project.Library_API.domain.libraryBook.service.LibraryBookService;
import Solo_Project.Library_API.domain.member.entity.Member;
import Solo_Project.Library_API.domain.member.service.MemberService;
import Solo_Project.Library_API.domain.memberBook.entity.MemberBook;
import Solo_Project.Library_API.domain.memberBook.repository.MemberBookRepository;
import Solo_Project.Library_API.global.advice.BusinessLogicException;
import Solo_Project.Library_API.global.advice.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/books")
@Slf4j
public class BookController {

    private final String url = "http://localhost:8080/books/";

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private BookService bookService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberBookRepository memberBookRepository;

    @Autowired
    private LibraryBookService libraryBookService;

    @PostMapping("/{library-Id}/{book-Id}/{member-Id}")
    public ResponseEntity postBookRental(@PathVariable("library-Id")@Positive Long libraryId,
                                         @PathVariable("book-Id")@Positive Long bookId,
                                         @PathVariable("member-Id")@Positive Long memberId) throws Exception {
        Page<LibraryBook> libraryBookPage = libraryBookService.findAllLibraryBooksByLibraryId(libraryId, 0, 10);

        List<LibraryBook> availableBooks = libraryBookPage.getContent().stream()
                .filter(libraryBook -> libraryBook.getBookStatus() == Book.BookStatus.AVAILABLE)
                .collect(Collectors.toList());

        boolean isUnavailable = libraryBookPage.getContent().stream()
                .filter(libraryBook -> libraryBook.getBookStatus() == Book.BookStatus.UNAVAILABLE)
                .anyMatch(libraryBook -> libraryBook.getBook().getBookId().equals(bookId));

        if(isUnavailable) {
            throw new BusinessLogicException(ExceptionCode.BOOK_IS_ALREADY_RENTED);
        }

        LibraryBook targetLibraryBook = availableBooks.stream()
                .filter(libraryBook -> libraryBook.getBook().getBookId().equals(bookId))
                .findFirst()
                .orElseThrow(()->new BusinessLogicException(ExceptionCode.BOOK_NOT_FOUND));

        Member targetMember = memberService.findMember(memberId);

        int rentalHistoryCount = memberBookRepository.countByMember(targetMember);

        if (rentalHistoryCount >= 5) {
            throw new BusinessLogicException(ExceptionCode.MAXIMUM_RENTAL_ALREADY);
        }
        targetLibraryBook.setBookStatus(Book.BookStatus.UNAVAILABLE);
        libraryBookService.saveLibraryBook(targetLibraryBook);

        MemberBook memberBook = new MemberBook();
        memberBook.setLibraryId(libraryId);
        memberBook.setBook(targetLibraryBook.getBook());
        memberBook.setMember(targetMember);
        memberBook.setCreatedAt(LocalDateTime.now());
        memberBookRepository.save(memberBook);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
