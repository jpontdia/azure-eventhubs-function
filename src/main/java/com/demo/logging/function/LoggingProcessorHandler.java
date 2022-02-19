package com.demo.logging.function;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.EventHubTrigger;
import com.microsoft.azure.functions.annotation.FunctionName;
import org.springframework.cloud.function.adapter.azure.FunctionInvoker;

import java.util.List;

@SuppressWarnings("unused")
public class LoggingProcessorHandler extends FunctionInvoker<String, Boolean>{

    @FunctionName("loggingProcessor")
    public void run(
            @EventHubTrigger(
                    name = "message",
                    dataType = "string",
                    eventHubName  = "appservice-logs",
                    connection = "EVENTHUBS_CONNECTION"
            )
                    List<String> message,
            final ExecutionContext executionContext) {
        //executionContext.getLogger().info("Handler message size: " + message.size());
        message.forEach(item ->
                        handleRequest(item, executionContext)
                //executionContext.getLogger().info("Message payload: " + item)
        );
    }
}
