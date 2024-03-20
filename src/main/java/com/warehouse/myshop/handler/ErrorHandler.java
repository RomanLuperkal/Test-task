package com.warehouse.myshop.handler;

import com.warehouse.myshop.handler.exceptions.NotFoundException;
import com.warehouse.myshop.handler.responce.ApiError;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<ApiError> handleException(MethodArgumentNotValidException e) {
        ApiError errorResponse = ApiError.builder()
                .message(e.getFieldError().getDefaultMessage())
                .reason("Неправильно сделанный запрос.")
                .status(HttpStatus.BAD_REQUEST.toString())
                .time(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    private ResponseEntity<ApiError> handleException(ConstraintViolationException e) {
        ApiError errorResponse = ApiError.builder()
                .message(e.getMessage())
                .reason("Неправильно сделанный запрос.")
                .status(HttpStatus.BAD_REQUEST.toString())
                .time(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    private ResponseEntity<ApiError> handleException(DataIntegrityViolationException e) {
        ApiError errorResponse = ApiError.builder()
                .message(e.getMessage())
                .reason("Ограничение целостности базы данных была нарушена.")
                .status(String.valueOf(HttpStatus.CONFLICT))
                .time(LocalDateTime.now())
                .build();
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    private ResponseEntity<ApiError> handleException(NotFoundException e) {
        ApiError errorResponse = ApiError.builder()
                .message(e.getMessage())
                .reason("Требуемый объект не был найден.")
                .status(HttpStatus.NOT_FOUND.toString())
                .time(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    private ResponseEntity<ApiError> handleException(Exception e) {
        ApiError errorResponse = ApiError.builder()
                .message(e.getMessage())
                .reason("Неправильно сделанный запрос.")
                .status(HttpStatus.BAD_REQUEST.toString())
                .time(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
