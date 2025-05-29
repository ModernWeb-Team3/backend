package kr.unideal.server.backend.global.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExceptionDto {

    @JsonProperty("error_code")  // JSON 키 이름 매핑
    private final Integer code;

    @JsonProperty("error_message")  // JSON 키 이름 매핑
    private final String message;

    public ExceptionDto(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


    // constructor
    public ExceptionDto(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }


    public static ExceptionDto of(ErrorCode errorCode) {
        return new ExceptionDto(errorCode);
    }
}
