package com.warehouse.myshop.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.myshop.handler.exceptions.CurrencyException;
import com.warehouse.myshop.handler.responce.ApiError;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Order(1)
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    final ObjectMapper objectMapper;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (CurrencyException e) {
            handleCurrencyException(response, e);
        }
    }

    private String convertObjectToJson(ApiError object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        return objectMapper.writeValueAsString(object);
    }

    private void handleCurrencyException(HttpServletResponse response, CurrencyException e) throws IOException {
        ApiError errorResponse = ApiError.builder()
                .message(e.getMessage())
                .reason("Данная валюта не поддерживается")
                .status(HttpStatus.BAD_REQUEST.toString())
                .time(LocalDateTime.now())
                .build();

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(convertObjectToJson(errorResponse));
    }
}
