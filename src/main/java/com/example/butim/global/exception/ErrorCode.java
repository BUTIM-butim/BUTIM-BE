package com.example.butim.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    SIDO_REQUIRED(HttpStatus.BAD_REQUEST, 400, "시/도 값이 필요합니다."),
    SIDO_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "존재하지 않는 시/도입니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "서버 내부 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, int code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}