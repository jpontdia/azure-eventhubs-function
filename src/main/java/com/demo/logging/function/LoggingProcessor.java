package com.demo.logging.function;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.function.Function;

@Component
@Slf4j
@SuppressWarnings("unused")
public class LoggingProcessor implements Function<String, Boolean> {

    public Boolean apply(String message) {
        log.debug("Message received in spring function: {}", message);
        return true;
    }
}