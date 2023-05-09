package Solo_Project.Library_API.domain.member.mapper;

import Solo_Project.Library_API.domain.member.dto.MemberDto;
import Solo_Project.Library_API.domain.member.entity.Member;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member memberDtoPostToMember (MemberDto.Post post);
    MemberDto.Response memberToMemberDtoResponse (Member member);
    List<MemberDto.Response> membersToMemberDtoResponse (List<Member> members);
}
