package Solo_Project.Library_API.global.advice;

import lombok.Getter;

@Getter
public enum ExceptionCode {
    MEMBER_ALREADY_EXIST(400,"회원이 이미 존재합니다."),
    MEMBER_NOT_FOUND(400,"회원이 존재하지 않습니다."),
    RENTAL_BOOK_EXIST(400,"현재 대여 중인 책이 있으므로 탈퇴할 수 없습니다."),
    OVERDUE_BOOK_EXIST(400, "현재 연체중인 책이 있으므로 추가 대출 불가합니다."),
    MAXIMUM_RENTAL_ALREADY(400,"대여할 수 있는 최대 치를 초과하였습니다."),
    ALL_BOOKS_ARE_ALREADY_RENTED(400, "모든 책이 대여중입니다."),
    METHOD_NOT_ALLOWED(400,"메서드가 잘못되었습니다."),
    EMAIL_IS_ALREADY_EXIST(400,"이미 존재하는 이메일 입니다."),
    FIELD_MUST_BE_FULFILLED(400,"내용이 부족합니다."),
    BOOK_IS_ALREADY_RENTED(400,"책이 이미 대여중입니다."),
    BOOK_NOT_FOUND(400,"일치하는 책이 없습니다."),
    RENTAL_HISTORY_NOT_FOUND(400,"대여하신 기록이 없습니다."),
    RENTAL_PERSON_IS_DIFFERENT(400,"대여자가 다릅니다."),
    BOOK_IS_ALREADY_RETURNED(400,"책이 이미 반납되었습니다."),
    DATA_IS_EMPTY(400, "저장된 정보가 없습니다.");

    @Getter
    int status;

    @Getter
    String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
