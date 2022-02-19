package com.demo.logging.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Record {
    String time;
    String resourceId;
    String operationName;
    String category;
    String resultDescription;
    String level;

    @JsonProperty("EventStampType")
    String eventStampType;

    @JsonProperty("EventPrimaryStampName")
    String eventPrimaryStampName;

    @JsonProperty("EventStampName")
    String eventStampName;

    @JsonProperty("Host")
    String host;

    @JsonProperty("EventIpAddress")
    String eventIpAddress;
}
