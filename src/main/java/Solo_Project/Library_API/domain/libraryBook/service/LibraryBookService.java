package Solo_Project.Library_API.domain.libraryBook.service;

import Solo_Project.Library_API.domain.libraryBook.entity.LibraryBook;
import Solo_Project.Library_API.domain.libraryBook.repository.LibraryBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class LibraryBookService {

    @Autowired
    private LibraryBookRepository libraryBookRepository;

    public LibraryBook saveLibraryBook(LibraryBook book){
        return libraryBookRepository.save(book);
    }
    public Page<LibraryBook> findAllLibraryBooksByLibraryId(Long libraryId, int page, int size) {
        return libraryBookRepository.findAllLibraryBooksByLibraryId(libraryId, PageRequest.of(page, size));
    }
    public LibraryBook findLibraryBookByLibraryIdBookId(Long libraryId, Long bookId) {
        return libraryBookRepository.findLibraryBookByLibraryIdBookId(libraryId,bookId);
    }

}
