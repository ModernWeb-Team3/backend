package kr.unideal.server.backend.global.exception;


import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return errorCode.getMessage();
    }

}
