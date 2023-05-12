package Solo_Project.Library_API.domain.book.service;

import Solo_Project.Library_API.domain.book.entity.Book;
import Solo_Project.Library_API.domain.book.repository.BookRepository;
import Solo_Project.Library_API.domain.libraryBook.entity.LibraryBook;
import Solo_Project.Library_API.domain.member.entity.Member;
import Solo_Project.Library_API.global.advice.BusinessLogicException;
import Solo_Project.Library_API.global.advice.ExceptionCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookService {
    private BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    public Page<Book> findAllBook(Long libraryId, int page, int size) {
        Page<Book> bookPage = bookRepository.findAllBooksByLibraryId(libraryId, PageRequest.of(
                page, size
        ));
        if(bookPage.isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.BOOK_NOT_FOUND);
        }else {
            return bookPage;
        }
    }
    public Page<LibraryBook> findAllLibraryBooksByLibraryId(Long libraryId, int page, int size) {
        return bookRepository.findAllLibraryBooksByLibraryId(libraryId, PageRequest.of(page, size));
    }
    public Book saveBook(Book book){
        return bookRepository.save(book);
    }

    public Book findBookByBookId(Long bookId) {
        Optional<Book> findBook = bookRepository.findById(bookId);
        Book foundBook = findBook.orElseThrow(()-> new BusinessLogicException(ExceptionCode.BOOK_NOT_FOUND));
        return foundBook;
    }
}
