package Solo_Project.Library_API.domain.libraryBook.mapper;

import Solo_Project.Library_API.domain.libraryBook.dto.LibraryBookDto;
import Solo_Project.Library_API.domain.libraryBook.entity.LibraryBook;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LibraryBookMapper {

    @Mapping(target = "libraryId", source = "library.libraryId")
    @Mapping(target = "bookId", source = "book.bookId")
    @Mapping(target = "libraryBookId", source = "libraryBookId")
    @Mapping(target = "bookTitle", source = "book.bookTitle")
    @Mapping(target = "bookAuthor", source = "book.bookAuthor")
    @Mapping(target = "bookPublisher", source = "book.bookPublisher")
    @Mapping(target = "bookStatus", source = "bookStatus")
    LibraryBookDto.Response libraryBookToLibraryBookDtoResponse(LibraryBook libraryBook);

    List<LibraryBookDto.Response> libraryBooksToLibraryBooksDtoResponse(List<LibraryBook> libraryBooks);
}
