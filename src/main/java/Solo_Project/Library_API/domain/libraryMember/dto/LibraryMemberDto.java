package Solo_Project.Library_API.domain.libraryMember.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class LibraryMemberDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response{
        long libraryId;
        long memberId;
        long libraryMemberId;
        String name;
        String phone;
        String email;
        String url;
    }
}
