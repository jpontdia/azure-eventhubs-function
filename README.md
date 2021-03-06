# azure-eventhubs
*Microsoft Azure Functions connecting to Azure Event Hubs demo with Spring Boot 2.5.4*

## Table of contents
1. [Objectives](#Objectives)
2. [Prerequisites](#prerequisites)
3. [Environment variables](#environment-variables)
4. [Build and test the application](#build-and-test-the-application)
5. [Recommended content](#recommended-content)

## Objectives
1. Receive messages from Azure Event Hubs using Azure Functions framework and Spring Boot

## Prerequisites
* An Azure subscription
* Java Development Kit (JDK) 11
* Apache Maven, version 3.8 or later.
* An Event Hubs standard instance.
* A Storage Account
* Azurite.  Emulator for local Azure Storage development [Azurite](https://docs.microsoft.com/en-us/azure/storage/common/storage-use-azurite?tabs=visual-studio)

## Environment variables
The application requires the next environment variables:
* EVENTHUBS_CONNECTION. The Event Hub Connection String contains the Namespace Name and the Shared Access Signature (SAS) authentication information.
* AZUREWEBJOBSSTORAGE. The Azure Storage Account needed to save the pointer
  to the latest message read. For local development install [Azurite](https://docs.microsoft.com/en-us/azure/storage/common/storage-use-azurite?tabs=visual-studio)

Example:
~~~bash
set EVENTHUBS_CONNECTION=Endpoint=sb://my-eventhubs.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=aabbccddeeffgghhhiii= 
set AZUREWEBJOBSSTORAGE=DefaultEndpointsProtocol=https;AccountName=my-storagea-account;AccountKey=aabbccddeeffgghhhiii;EndpointSuffix=core.windows.net
~~~

## Build and test the application
Open a windows console and set your directory to the root folder of your project

Run Azurite:
```bash
azurite --blobHost 127.0.0.1
```
Result:
```bash
....
....
Azurite Blob service is starting at http://127.0.0.1:10000
Azurite Blob service is successfully listening at http://127.0.0.1:10000
Azurite Queue service is starting at http://127.0.0.1:10001
Azurite Queue service is successfully listening at http://127.0.0.1:10001
Azurite Table service is starting at http://127.0.0.1:10002
Azurite Table service is successfully listening at http://127.0.0.1:10002
```

Open another window console and set your directory to the root folder of
your project:
Run the application:
```bash
mvn clean package -DskipTests azure-functions:run
```

The console should look like:
```bash
...
...
...
[2022-02-08T04:23:36.319Z] EventHubOptions
[2022-02-08T04:23:36.320Z] {
[2022-02-08T04:23:36.322Z]   "BatchCheckpointFrequency": 1,
[2022-02-08T04:23:36.323Z]   "EventProcessorOptions": {
[2022-02-08T04:23:36.325Z]     "EnableReceiverRuntimeMetric": false,
[2022-02-08T04:23:36.326Z]     "InvokeProcessorAfterReceiveTimeout": false,
[2022-02-08T04:23:36.328Z]     "MaxBatchSize": 10,
[2022-02-08T04:23:36.330Z]     "PrefetchCount": 300,
[2022-02-08T04:23:36.331Z]     "ReceiveTimeout": "00:01:00"
[2022-02-08T04:23:36.332Z]   },
[2022-02-08T04:23:36.334Z]   "PartitionManagerOptions": {
[2022-02-08T04:23:36.335Z]     "LeaseDuration": "00:00:30",
[2022-02-08T04:23:36.337Z]     "RenewInterval": "00:00:10"
[2022-02-08T04:23:36.339Z]   }
[2022-02-08T04:23:36.340Z] }
[2022-02-08T04:23:36.344Z] Starting JobHost
[2022-02-08T04:23:36.350Z] Starting Host (HostId=lnarpc0ntftq-1139085544, InstanceId=a0f92c8c-391f-4971-b95a-492c9b4064dd, Version=3.0.15417.0, ProcessId=9648, AppDomainId=1, InDebugMode=False, InDiagnosticMode=False, FunctionsExtensionVersion=(null))
[2022-02-08T04:23:36.371Z] Loading functions metadata
[2022-02-08T04:23:36.375Z] FUNCTIONS_WORKER_RUNTIME set to java. Skipping WorkerConfig for language:node
[2022-02-08T04:23:36.378Z] FUNCTIONS_WORKER_RUNTIME set to java. Skipping WorkerConfig for language:powershell
[2022-02-08T04:23:36.380Z] FUNCTIONS_WORKER_RUNTIME set to java. Skipping WorkerConfig for language:python
[2022-02-08T04:23:36.404Z] 1 functions loaded
[2022-02-08T04:23:36.433Z] Worker process started and initialized.
[2022-02-08T04:23:36.435Z] Generating 1 job function(s)
[2022-02-08T04:23:36.484Z] Found the following functions:
[2022-02-08T04:23:36.486Z] Host.Functions.loggingProcessor
[2022-02-08T04:23:36.487Z]
[2022-02-08T04:23:36.495Z] Initializing function HTTP routes
[2022-02-08T04:23:36.496Z] No HTTP routes mapped
[2022-02-08T04:23:36.498Z]
[2022-02-08T04:23:36.512Z] Host initialized (146ms)

Functions:

        loggingProcessor: eventHubTrigger

For detailed output, run func with --verbose flag.
[2022-02-08T04:23:38.253Z] Host started (1899ms)
[2022-02-08T04:23:38.257Z] Job host started
[2022-02-08T04:23:43.764Z] Host lock lease acquired by instance ID '000000000000000000000000348D1FE3'.
```

### Testing the application
Send messages to the event hub using the demo application: [Azure EventHubs demo](https://github.com/jpontdia/azure-eventhubs)

## Recommended content
* [Get an Event Hubs connection string](https://docs.microsoft.com/en-us/azure/event-hubs/event-hubs-get-connection-string)
* [Integrate Event Hubs with serverless functions on Azure](https://docs.microsoft.com/en-us/azure/architecture/serverless/event-hubs-functions/event-hubs-functions)
* [Explore Azure Event Hubs with GUI](https://medium.com/@sriharip316/explore-azure-event-hubs-with-gui-2501ed278d4)
