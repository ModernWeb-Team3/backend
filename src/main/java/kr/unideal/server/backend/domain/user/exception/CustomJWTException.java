package kr.unideal.server.backend.domain.user.exception;


public class CustomJWTException extends RuntimeException {

    // 기본 생성자
    public CustomJWTException() {
        super("JWT 처리 중 오류가 발생했습니다.");
    }

    // 사용자 정의 메시지를 전달할 수 있는 생성자
    public CustomJWTException(String message) {
        super(message);  // 부모 클래스의 생성자를 호출하여 메시지 전달
    }

    // 예외 원인을 함께 전달할 수 있는 생성자
    public CustomJWTException(String message, Throwable cause) {
        super(message, cause);  // 부모 클래스의 생성자를 호출하여 메시지와 원인 전달
    }
}
