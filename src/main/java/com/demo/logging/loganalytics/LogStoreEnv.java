package com.demo.logging.loganalytics;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "azure.log.store")
@Component
@Data
public class LogStoreEnv {
    private String azureSharedKey;
    private String azureWid;
    private String appName;
}
