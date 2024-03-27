package com.warehouse.myshop.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "My-shop",
                description = "My shop inventory", version = "1.0.0",
                contact = @Contact(
                        name = "Ivanov Roman",
                        email = "do0mblast@yandex.ru"
                )
        )
)
public class SwaggerConfiguration {
}
