package com.example.butim.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    INVALID_SIDO_CODE(HttpStatus.BAD_REQUEST, "시도 코드는 필수입니다."),
    REGION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 시도 코드입니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}