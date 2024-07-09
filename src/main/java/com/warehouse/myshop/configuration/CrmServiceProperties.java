package com.warehouse.myshop.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "rest.crm-service")
@Getter
@Setter
public class CrmServiceProperties {
    private String host;
    private Map<String, String> methods;
}
