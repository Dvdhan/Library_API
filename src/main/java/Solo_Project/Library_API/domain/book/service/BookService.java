package Solo_Project.Library_API.domain.book.service;

import Solo_Project.Library_API.domain.book.entity.Book;
import Solo_Project.Library_API.domain.book.repository.BookRepository;
import Solo_Project.Library_API.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    private BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Page<Book> findAllBook(Long libraryId, int page, int size) {
        Page<Book> bookPage = bookRepository.findAllByLibraryId(libraryId, PageRequest.of(
                page, size, Sort.by("bookTitle").ascending()
        ));
        return bookPage;
    }

}
