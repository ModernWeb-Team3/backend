package kr.unideal.server.backend.global.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    // [1] 포인트컷: kr.unideal.server.backend 하위의 모든 메서드
    @Pointcut("execution(* kr.unideal.server.backend..*(..))")
    public void logPointcut() {}

    // [2] 메서드 실행 전 로그
    @Before("logPointcut()")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("📌 BEFORE: " + joinPoint.getSignature().toShortString());
        System.out.println("📦 Arguments: " + Arrays.toString(joinPoint.getArgs()));
    }

    // [3] 정상 반환 후 로그
    @AfterReturning(pointcut = "logPointcut()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        System.out.println("✅ AFTER RETURN: " + joinPoint.getSignature().toShortString());
        System.out.println("🎁 Returned: " + result);
    }

    // [4] 예외 발생 시 로그
    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        System.out.println("❌ EXCEPTION in: " + joinPoint.getSignature().toShortString());
        System.out.println("⚠️ Exception: " + e.getMessage());
    }
}
