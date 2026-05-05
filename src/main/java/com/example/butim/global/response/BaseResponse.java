package com.example.butim.global.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseResponse<T> {

    @JsonProperty("isSuccess")
    private final boolean isSuccess;

    private final int code;
    private final String message;
    private final T result;

    public static <T> BaseResponse<T> success(String message, T result) {
        return new BaseResponse<>(true, 200, message, result);
    }

    public static <T> BaseResponse<T> success(int code, String message, T result) {
        return new BaseResponse<>(true, code, message, result);
    }

    public static <T> BaseResponse<T> fail(int code, String message) {
        return new BaseResponse<>(false, code, message, null);
    }
}