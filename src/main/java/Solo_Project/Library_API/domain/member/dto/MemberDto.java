package Solo_Project.Library_API.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
public class MemberDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Post{
        @NotBlank
        String name;

        @NotBlank
        String phone;

        @Email
        String email;

        @NotNull(message = "Password cannot be null")
        @NotEmpty(message = "Password cannot be empty")
        String password;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response{
        long libraryId;
        long memberId;
        String name;
        String email;
        String password;
        String url;
    }
}
