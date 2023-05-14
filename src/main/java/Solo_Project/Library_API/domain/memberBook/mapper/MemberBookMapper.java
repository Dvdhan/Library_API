package Solo_Project.Library_API.domain.memberBook.mapper;

import Solo_Project.Library_API.domain.memberBook.dto.MemberBookDto;
import Solo_Project.Library_API.domain.memberBook.entity.MemberBook;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberBookMapper {

    // LibraryBookMapper와는 다르게 source에서
    // libraryId, bookId, memberBookId 를 . 없이 바로 사용한 이유는
    // MemberBook 엔티티에서 해당 필드들을 바로 가지고 있기 때문이고
    // bookId에서 . 을 사용한 이유는 bookId를 바로가진게 아니라 book 객체를 통해 가져오기 떄문.

    @Mapping(target = "memberId", source = "member.memberId")
    @Mapping(target = "libraryId", source = "libraryId")
    @Mapping(target = "bookId", source = "book.bookId")
    @Mapping(target = "memberBookId", source = "memberBookId")
    @Mapping(target = "bookTitle", source = "book.bookTitle")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "returnedAt", source = "returnedAt")
    @Mapping(target = "dueReturnDate", source = "dueReturn")
    MemberBookDto.Response memberBookToMemberBookDtoResponse(MemberBook memberBook);
    List<MemberBookDto.Response> memberBooksToMemberBooksDtoResponse (List<MemberBook> memberBooks);
}
