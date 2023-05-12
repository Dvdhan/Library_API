package Solo_Project.Library_API.domain.libraryMember.mapper;

import Solo_Project.Library_API.domain.libraryMember.dto.LibraryMemberDto;
import Solo_Project.Library_API.domain.libraryMember.entity.LibraryMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LibraryMemberMapper {
    @Mapping(target = "libraryId", source = "library.libraryId")
    @Mapping(target = "memberId", source = "member.memberId")
    @Mapping(target = "libraryMemberId", source = "libraryMemberId")
    @Mapping(target = "name", source = "member.name")
    @Mapping(target = "phone", source = "member.phone")
    @Mapping(target = "email", source = "member.email")
    LibraryMemberDto.Response libraryMemberToLibraryMemberDtoResponse (LibraryMember libraryMember);
    List<LibraryMemberDto.Response> libraryMembersToLibraryMembersDtoResponse (List<LibraryMember> libraryMembers);
}
