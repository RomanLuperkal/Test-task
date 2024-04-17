package com.warehouse.myshop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Aspect
@Component
@Order(1)
@Slf4j
public class TimeTrackTransactionAspect {
    @Around("@annotation(com.warehouse.myshop.anotation.TimeTrack) && @annotation(org.springframework.transaction.annotation.Transactional)")
    public Object timeTrack(ProceedingJoinPoint joinPoint) throws Throwable {
        final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        final String className = methodSignature.getDeclaringType().getSimpleName();
        final String method = methodSignature.getName();
        Instant start = Instant.now();
        try {
            return joinPoint.proceed();
        } finally {
            Instant end = Instant.now();
            Duration duration = Duration.between(start, end);
            long minutes = duration.toMinutes();
            long seconds = duration.getSeconds() % 60;
            log.info("Время выполнения метода " + method + " класса " + className + "c учетом transactional: "
                    + minutes + "мин " + seconds + "сек");
        }
    }
}
