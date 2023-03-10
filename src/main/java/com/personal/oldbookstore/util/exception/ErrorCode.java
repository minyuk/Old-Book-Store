package com.personal.oldbookstore.util.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    //common
    ID_NOT_FOUND("id", "id.notfound", "해당 id를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    ONLY_USER("access", "access.denied", "회원만 기능을 이용할 수 있습니다.", HttpStatus.BAD_REQUEST),

    //User
    EMAIL_DUPLICATED("email", "email.duplicated", "이메일이 중복됩니다.", HttpStatus.BAD_REQUEST),
    NICKNAME_DUPLICATED("nickname", "nickname.duplicated", "닉네임이 중복됩니다.", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_EQUAL("password", "password.notEqual", "비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    NICKNAME_EQUAL_PREVIOUS("nickname", "nickname.equal.previous", "닉네임이 이전과 같습니다.", HttpStatus.BAD_REQUEST),
    PASSWORD_EQUAL_PREVIOUS("newPassword", "password.equal.previous", "기존 비밀번호와 새 비밀번호가 일치합니다.", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_FOUND("email", "email.notFound", "해당 이메일을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    NICKNAME_NOT_EQUAL("nickname", "nickname.notEqual", "닉네임이 일치하지 않습니다.", HttpStatus.BAD_REQUEST),

    //item
    EDIT_ACCESS_DENIED("edit", "edit.access.denied", "수정 권한이 없습니다.", HttpStatus.BAD_REQUEST),
    DELETE_ACCESS_DENIED("delete", "delete.access.denied", "삭제 권한이 없습니다.", HttpStatus.BAD_REQUEST),
    DELETE_EXIST_ORDER("delete", "delete.exist.order", "주문 내역이 있는 상품입니다.", HttpStatus.BAD_REQUEST),

    //order
    STOCK_FAIL("stock", "stock.fail", "재고가 부족합니다.", HttpStatus.BAD_REQUEST),

    //basket
    ALREADY_SAVED_BASKET("save", "already.saved.basket", "이미 장바구니에 담긴 상품입니다.", HttpStatus.BAD_REQUEST)
    ;

    private String cause;
    private String code;
    private String message;
    private HttpStatus httpStatus;

    private ErrorCode(String cause, String code, String message, HttpStatus httpStatus) {
        this.cause = cause;
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getCause() {
        return cause;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
