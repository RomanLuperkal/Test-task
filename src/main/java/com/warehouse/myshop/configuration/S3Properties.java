package com.warehouse.myshop.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "aws")
@Getter
@Setter
public class S3Properties {
    private String accessKeyId;
    private String secretAccessKey;
    private String region;
    private String bucket;
}
