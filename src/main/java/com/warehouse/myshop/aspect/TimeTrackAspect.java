package com.warehouse.myshop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Aspect
@Component
public class TimeTrackAspect {
    @Around("@annotation(com.warehouse.myshop.anotation.TimeTrack)")
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
             System.out.println("Время выполнения метода " + method + " класса " + className + ": "+ minutes + "мин "
                     + seconds + "сек");
         }
    }
}
