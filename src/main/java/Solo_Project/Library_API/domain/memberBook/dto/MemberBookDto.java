package Solo_Project.Library_API.domain.memberBook.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
public class MemberBookDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response{
        private Long libraryId;
        private Long memberId;
        private Long bookId;
        private Long memberBookId;
        private String bookTitle;
        private LocalDate createdAt;
        private LocalDate dueReturnDate;
        private LocalDate returnedAt;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RentalResponse {
        private Long memberId;
        private Long libraryId;
        private Long bookId;
        private LocalDate createdAt;
        private LocalDate dueReturnDate;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReturnResponse {
        private Long memberId;
        private Long libraryId;
        private Long bookId;
        private LocalDate createdAt;
        private LocalDate returnedAt;
        private Long overdueDays;
        private String message;
    }
}
