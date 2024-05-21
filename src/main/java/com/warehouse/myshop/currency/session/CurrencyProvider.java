package com.warehouse.myshop.currency.session;

import com.warehouse.myshop.currency.enums.Currency;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Getter
@Setter
@Component
public class CurrencyProvider {
    private Currency currency = Currency.RUB;
}
