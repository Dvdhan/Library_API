package Solo_Project.Library_API.domain.libraryBook.mapper;

import Solo_Project.Library_API.domain.libraryBook.dto.LibraryBookDto;
import Solo_Project.Library_API.domain.libraryBook.entity.LibraryBook;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LibraryBookMapper {

    //MemberBookMapper와는 다르게 source 에 . 을 사용한 이유는
    //해당 필드들은 LibraryBook 엔티티에서 직접적인 field 로 가지고 있지 않고
    //library 객체, book 객체를 통해 가져오는 것이기 때문.

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
