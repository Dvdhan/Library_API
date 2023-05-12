package Solo_Project.Library_API.domain.memberBook.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.procedure.spi.ParameterRegistrationImplementor;

import java.time.LocalDateTime;

@NoArgsConstructor
public class MemberBookDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response{
        private long memberId;
        private Long libraryId;
        private Long bookId;
        private LocalDateTime createdAt;
        private LocalDateTime returnedAt;
    }
}
