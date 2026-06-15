package com.example.butim.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // Region
    INVALID_SIDO_CODE(HttpStatus.BAD_REQUEST, 400, "시도 코드는 필수입니다."),
    REGION_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "존재하지 않는 시도 코드입니다."),

    // Industry
    INDUSTRY_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "해당 업종을 찾을 수 없습니다."),
    JOB_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "해당 직종 목록을 찾을 수 없습니다."),

    // AccidentInfo
    ACCIDENT_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "산재정보를 찾을 수 없습니다."),
    ACCIDENT_INFO_ALREADY_EXISTS(HttpStatus.CONFLICT, 409, "이미 산재정보가 존재합니다."),
    DIAGNOSIS_CODE_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "해당 주상병코드를 찾을 수 없습니다."),

    // Prediction
    PREDICTION_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "예측 결과를 찾을 수 없습니다."),

    // Financial
    FINANCIAL_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "재정정보를 찾을 수 없습니다."),
    FINANCIAL_INFO_ALREADY_EXISTS(HttpStatus.CONFLICT, 409, "이미 재정정보가 등록되어 있습니다."),

    // Strategy
    STRATEGY_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "추천 전략 결과를 찾을 수 없습니다."),
    STRATEGY_NOT_CONFIRMED(HttpStatus.BAD_REQUEST, 400, "확정된 전략이 없습니다."),
    INVALID_STRATEGY_TYPE(HttpStatus.BAD_REQUEST, 400, "올바르지 않은 전략 유형입니다."),
    STRATEGY_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "전략 항목을 찾을 수 없습니다."),
    INVALID_CASHFLOW_INPUT(HttpStatus.BAD_REQUEST, 400, "입력된 항목이 없습니다. 값을 입력한 후 다시 계산해주세요."),
    AI_RESPONSE_PARSE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, 500, "AI 추천 결과를 해석하지 못했습니다."),
    AI_REQUEST_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, 500, "AI 전략 추천 요청에 실패했습니다."),
    PUBLIC_DATA_REQUEST_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, 500, "공공데이터 조회에 실패했습니다."),

    // Common
    INVALID_INPUT(HttpStatus.BAD_REQUEST, 400, "입력값이 올바르지 않습니다."),

    // Auth
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, 409, "이미 사용 중인 이메일입니다."),
    DUPLICATE_PHONE(HttpStatus.CONFLICT, 409, "이미 사용 중인 전화번호입니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, 400, "비밀번호가 일치하지 않습니다."),
    PHONE_NOT_VERIFIED(HttpStatus.BAD_REQUEST, 400, "전화번호 인증이 완료되지 않았습니다."),
    INVALID_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, 400, "인증번호가 올바르지 않습니다."),
    EXPIRED_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, 400, "인증번호가 만료되었습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "존재하지 않는 사용자입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, 401, "비밀번호가 올바르지 않습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, 401, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, 401, "만료된 토큰입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, 401, "유효하지 않은 리프레시 토큰입니다."),
    WITHDRAWN_USER(HttpStatus.UNAUTHORIZED, 401, "탈퇴한 사용자입니다."),

    // Server
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