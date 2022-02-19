package com.demo.logging.vo;

import lombok.Data;

@Data
public class LogMessage {
    String timestamp;
    String service;
    String message;
}
