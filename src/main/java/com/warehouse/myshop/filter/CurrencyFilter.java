package com.warehouse.myshop.filter;


import com.warehouse.myshop.currency.session.CurrencyProvider;
import com.warehouse.myshop.enums.Currency;
import com.warehouse.myshop.handler.exceptions.CurrencyException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class CurrencyFilter extends OncePerRequestFilter {
    private final CurrencyProvider currencyProvider;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        if (uri.startsWith("/product") && method.equals("GET")) {
            String currency = request.getHeader("currency");
            try {
                if (currency != null) {
                    currencyProvider.setCurrency(Currency.valueOf(currency));
                }
            } catch (Exception e) {
                throw new CurrencyException(e.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }
}
