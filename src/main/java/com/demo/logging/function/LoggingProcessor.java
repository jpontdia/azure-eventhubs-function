package com.demo.logging.function;

import com.demo.logging.loganalytics.AzureLoggerComponent;
import com.demo.logging.vo.LogMessage;
import com.demo.logging.vo.Record;
import com.demo.logging.vo.ServiceConsoleLog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@Slf4j
@SuppressWarnings("unused")
public class LoggingProcessor implements Function<String, Boolean> {

    @Autowired
    private AzureLoggerComponent azureLoggerComponent;

    @Value("${azure.log.store.azure-wid}")
    private String workspaceid;

    @Value("${azure.log.store.azure-shared-key}")
    private String azureSharedKey;

    public Boolean apply(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            var serviceConsoleLog = objectMapper.readValue(message, ServiceConsoleLog.class);
            for (Record record: serviceConsoleLog.getRecords()){
                var logMessage = new LogMessage();

                //Get the name of the service
                String[] resourceArray = record.getResourceId().split("/");
                if (resourceArray.length == 9){
                    logMessage.setService(resourceArray[8]);
                }
                else{
                    logMessage.setService(record.getResourceId());
                }
                logMessage.setTimestamp(record.getTime());
                logMessage.setMessage(record.getResultDescription());

                try {
                    var logMessageString = objectMapper.writeValueAsString(logMessage);
                    azureLoggerComponent.pushLogsToAzure(logMessageString);
                    log.info("From: {}", logMessage.getService());
                } catch (JsonProcessingException jsonProcessingException) {
                    log.error("(ServiceConsoleLog branch) The logMessage object couldn't be transformed to json string");
                }
            }
        } catch (JsonProcessingException e) {
            var logMessage = new LogMessage();

            //Tokenize sensitive data
            logMessage.setMessage(message);

            //Get the timestamp from the message string
            //logMessage.setTimestamp( ... )

            //Get the service from the message string
            //logMessage.setService( ... )

            try {
                var logMessageString = objectMapper.writeValueAsString(logMessage);
                azureLoggerComponent.pushLogsToAzure(message);
                log.info("Message sent (no ServiceConsoleLog type)");
            } catch (JsonProcessingException jsonProcessingException) {
                log.error("(No ServiceConsoleLog type branch) The logMessage object couldn't be transformed to json string");
            }
        }
        return true;
    }
}