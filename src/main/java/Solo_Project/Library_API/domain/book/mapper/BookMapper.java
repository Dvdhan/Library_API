package Solo_Project.Library_API.domain.book.mapper;

import Solo_Project.Library_API.domain.book.dto.BookDto;
import Solo_Project.Library_API.domain.book.entity.Book;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDto.SingleResponse bookToBookDtoResponse (Book book);
    List<BookDto.Response> booksToBookDtoResponse (List<Book> books);
}
