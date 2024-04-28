package com.warehouse.myshop.product.filter;


import com.warehouse.myshop.currency.session.CurrencyProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class OncePreRequestFilter implements Filter {
    private final CurrencyProvider currencyProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String currency = httpRequest.getHeader("currency");

        if (currency != null) {
            currencyProvider.setCurrency(currency);
        }

        filterChain.doFilter(request, response);
    }
}
