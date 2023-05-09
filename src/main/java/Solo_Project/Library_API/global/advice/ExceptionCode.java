package Solo_Project.Library_API.global.advice;

import lombok.Getter;

@Getter
public enum ExceptionCode {
    MEMBER_ALREADY_EXIST(601,"회원이 이미 존재합니다."),
    MEMBER_NOT_FOUND(602,"회원이 존재하지 않습니다."),
    RENTAL_BOOK_EXIST(603,"현재 대여 중인 책이 있으므로 탈퇴할 수 없습니다."),
    OVERDUE_BOOK_EXIST(604, "현재 연체중인 책이 있으므로 추가 대출 불가합니다."),
    RENTAL_BOOK_EXCEED(605,"대여할 수 있는 최대 치를 초과하였습니다."),
    LIBRARY_BOOK_SOLD_OUT(606, "모든 책이 대여중입니다."),
    METHOD_NOT_ALLOWED(607,"메서드가 잘못되었습니다."),
    EMAIL_IS_ALREADY_EXIST(608,"이미 존재하는 이메일 입니다."),
    FIELD_MUST_BE_FULFILLED(609,"내용이 부족합니다.");

    @Getter
    int status;

    @Getter
    String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
