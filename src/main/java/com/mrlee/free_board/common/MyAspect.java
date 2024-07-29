package com.mrlee.free_board.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
public class MyAspect {

    private final MyMailSender myMailSender;

    @Around(value = "execution(* com.mrlee.free_board.post.service.PostService.*(..))")
    public Object checkExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        checkExecutionTime(executionTime, joinPoint);
        return result;
    }

    private void checkExecutionTime(long executionTime, ProceedingJoinPoint joinPoint) {
        if (executionTime > 1500) {
            log.info("실행 시간 = {}", executionTime);
            log.info("실행 메서드 = {}", joinPoint.getSignature().toShortString());
            myMailSender.sendMail(MyMailMessage.LowQualityMailMessage);
        }
    }
}
