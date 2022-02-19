package com.demo.logging.loganalytics;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.UUID;

@Component
@SuppressWarnings("unused")
@Slf4j
public class AzureLoggerComponent {
    public static final String SPL_CHAR_REGEX = "[-+.^:,]";
    public static final String UNDERSCORE = "_";
    public static final String AZURE_LA_URL = "https://%s.ods.opinsights.azure.com/api/logs?api-version=2016-04-01";

    public AzureLoggerComponent() {
    }

    public AzureLoggerComponent(LogStoreEnv logStoreEnv) {
        this.logStoreEnv = logStoreEnv;
    }

    @Autowired
    private LogStoreEnv logStoreEnv;

    @Value("${spring.application.name}")
    private String appName;

    @Async
    public void pushLogsToAzure(String json) {
        if (logStoreEnv == null || StringUtils.isEmpty(logStoreEnv.getAzureWid())
                || StringUtils.isEmpty(logStoreEnv.getAzureSharedKey())) {
            return;
        }

        try {
            UUID.fromString(logStoreEnv.getAzureWid());
        } catch (NullPointerException | IllegalArgumentException e) {
            log.error("The format of the Analytics Id is invalid");
            return;
        }
        logStoreEnv.setAppName(appName);
        log.debug("Configuration value: {}", logStoreEnv);
        RequestEntity<String> azureLogRqst = constructHttpEntity(json);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange(azureLogRqst, String.class);
    }

    private RequestEntity<String> constructHttpEntity(String json) {

        String nowRfc1123 = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss O").format(ZonedDateTime.now(ZoneOffset.UTC));

        var httpHeaders = new HttpHeaders();
        httpHeaders.add("Log-Type", constructAzureLogNm(logStoreEnv.getAppName()));
        httpHeaders.add("x-ms-date", nowRfc1123);
        json = StringUtils.stripAccents(json);
        httpHeaders.add("Authorization", computeAuthHdr(json, nowRfc1123));
        httpHeaders.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        var azueLogUrl = String.format(AZURE_LA_URL, logStoreEnv.getAzureWid());
        RequestEntity<String> requestEntity = new RequestEntity<>(json, httpHeaders, HttpMethod.POST,
                UriComponentsBuilder.fromHttpUrl(azueLogUrl).build().toUri());
        log.debug("RequestEntity: {}", requestEntity);
        return requestEntity;
    }

    private String computeAuthHdr(String jsonBody, String date) {
        int bodyLength = jsonBody.length();
        String signString = "POST\n" + bodyLength + "\n" + MediaType.APPLICATION_JSON_VALUE + "\n" + "x-ms-date:" + date
                + "\n" + "/api/logs";
        log.debug("Sign Signature: {}", signString);
        return createAuthorizationHeader(signString);
    }

    private String createAuthorizationHeader(String canonicalizedString) {
        String authStr = null;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(Base64.getDecoder().decode(logStoreEnv.getAzureSharedKey()), "HmacSHA256"));
            String authKey = new String(Base64.getEncoder().encode(mac.doFinal(canonicalizedString.getBytes("UTF-8"))));
            authStr = "SharedKey " + logStoreEnv.getAzureWid() + ":" + authKey;
        } catch (Exception e) {
            log.error("Error while sending message to Log Analytics", e);

        }
        return authStr;
    }

    public static String constructAzureLogNm(String appName) {
        if (StringUtils.isEmpty(appName)) {
            return "default";
        }
        return StringUtils.replaceAll(appName, SPL_CHAR_REGEX, "");
    }
}
