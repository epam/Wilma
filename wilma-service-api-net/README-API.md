<a name='contents'></a>
# Contents [#](#contents 'Go To Here')

- [ILogger](#T-epam-wilma_service_api-ILogger 'epam.wilma_service_api.ILogger')
  - [Debug(format,prs)](#M-epam-wilma_service_api-ILogger-Debug-System-String,System-Object[]- 'epam.wilma_service_api.ILogger.Debug(System.String,System.Object[])')
  - [Error(format,prs)](#M-epam-wilma_service_api-ILogger-Error-System-String,System-Object[]- 'epam.wilma_service_api.ILogger.Error(System.String,System.Object[])')
  - [Info(format,prs)](#M-epam-wilma_service_api-ILogger-Info-System-String,System-Object[]- 'epam.wilma_service_api.ILogger.Info(System.String,System.Object[])')
  - [Warning(format,prs)](#M-epam-wilma_service_api-ILogger-Warning-System-String,System-Object[]- 'epam.wilma_service_api.ILogger.Warning(System.String,System.Object[])')
- [LoadInformation](#T-epam-wilma_service_api-ServiceCommClasses-LoadInformation 'epam.wilma_service_api.ServiceCommClasses.LoadInformation')
  - [CountOfMessages](#P-epam-wilma_service_api-ServiceCommClasses-LoadInformation-CountOfMessages 'epam.wilma_service_api.ServiceCommClasses.LoadInformation.CountOfMessages')
  - [DeletedFilesCount](#P-epam-wilma_service_api-ServiceCommClasses-LoadInformation-DeletedFilesCount 'epam.wilma_service_api.ServiceCommClasses.LoadInformation.DeletedFilesCount')
  - [LoggerQueueSize](#P-epam-wilma_service_api-ServiceCommClasses-LoadInformation-LoggerQueueSize 'epam.wilma_service_api.ServiceCommClasses.LoadInformation.LoggerQueueSize')
  - [ResponseQueueSize](#P-epam-wilma_service_api-ServiceCommClasses-LoadInformation-ResponseQueueSize 'epam.wilma_service_api.ServiceCommClasses.LoadInformation.ResponseQueueSize')
- [LocalhostControlStatus](#T-epam-wilma_service_api-ServiceCommClasses-LocalhostControlStatus 'epam.wilma_service_api.ServiceCommClasses.LocalhostControlStatus')
  - [LocalhostMode](#P-epam-wilma_service_api-ServiceCommClasses-LocalhostControlStatus-LocalhostMode 'epam.wilma_service_api.ServiceCommClasses.LocalhostControlStatus.LocalhostMode')
- [LocalhostControlStatuses](#T-epam-wilma_service_api-WilmaService-LocalhostControlStatuses 'epam.wilma_service_api.WilmaService.LocalhostControlStatuses')
  - [Error](#F-epam-wilma_service_api-WilmaService-LocalhostControlStatuses-Error 'epam.wilma_service_api.WilmaService.LocalhostControlStatuses.Error')
  - [Off](#F-epam-wilma_service_api-WilmaService-LocalhostControlStatuses-Off 'epam.wilma_service_api.WilmaService.LocalhostControlStatuses.Off')
  - [On](#F-epam-wilma_service_api-WilmaService-LocalhostControlStatuses-On 'epam.wilma_service_api.WilmaService.LocalhostControlStatuses.On')
- [LoggingStatus](#T-epam-wilma_service_api-ServiceCommClasses-LoggingStatus 'epam.wilma_service_api.ServiceCommClasses.LoggingStatus')
  - [RequestLogging](#P-epam-wilma_service_api-ServiceCommClasses-LoggingStatus-RequestLogging 'epam.wilma_service_api.ServiceCommClasses.LoggingStatus.RequestLogging')
  - [ResponseLogging](#P-epam-wilma_service_api-ServiceCommClasses-LoggingStatus-ResponseLogging 'epam.wilma_service_api.ServiceCommClasses.LoggingStatus.ResponseLogging')
- [MessageLoggingControlStatus](#T-epam-wilma_service_api-WilmaService-MessageLoggingControlStatus 'epam.wilma_service_api.WilmaService.MessageLoggingControlStatus')
  - [Error](#F-epam-wilma_service_api-WilmaService-MessageLoggingControlStatus-Error 'epam.wilma_service_api.WilmaService.MessageLoggingControlStatus.Error')
  - [Off](#F-epam-wilma_service_api-WilmaService-MessageLoggingControlStatus-Off 'epam.wilma_service_api.WilmaService.MessageLoggingControlStatus.Off')
  - [On](#F-epam-wilma_service_api-WilmaService-MessageLoggingControlStatus-On 'epam.wilma_service_api.WilmaService.MessageLoggingControlStatus.On')
- [OperationMode](#T-epam-wilma_service_api-ServiceCommClasses-OperationMode 'epam.wilma_service_api.ServiceCommClasses.OperationMode')
  - [ProxyMode](#P-epam-wilma_service_api-ServiceCommClasses-OperationMode-ProxyMode 'epam.wilma_service_api.ServiceCommClasses.OperationMode.ProxyMode')
  - [StubMode](#P-epam-wilma_service_api-ServiceCommClasses-OperationMode-StubMode 'epam.wilma_service_api.ServiceCommClasses.OperationMode.StubMode')
  - [WilmaMode](#P-epam-wilma_service_api-ServiceCommClasses-OperationMode-WilmaMode 'epam.wilma_service_api.ServiceCommClasses.OperationMode.WilmaMode')
- [OperationModes](#T-epam-wilma_service_api-WilmaService-OperationModes 'epam.wilma_service_api.WilmaService.OperationModes')
  - [ERROR](#F-epam-wilma_service_api-WilmaService-OperationModes-ERROR 'epam.wilma_service_api.WilmaService.OperationModes.ERROR')
  - [PROXY](#F-epam-wilma_service_api-WilmaService-OperationModes-PROXY 'epam.wilma_service_api.WilmaService.OperationModes.PROXY')
  - [STUB](#F-epam-wilma_service_api-WilmaService-OperationModes-STUB 'epam.wilma_service_api.WilmaService.OperationModes.STUB')
  - [WILMA](#F-epam-wilma_service_api-WilmaService-OperationModes-WILMA 'epam.wilma_service_api.WilmaService.OperationModes.WILMA')
- [StubConfigOrder](#T-epam-wilma_service_api-WilmaService-StubConfigOrder 'epam.wilma_service_api.WilmaService.StubConfigOrder')
  - [Down](#F-epam-wilma_service_api-WilmaService-StubConfigOrder-Down 'epam.wilma_service_api.WilmaService.StubConfigOrder.Down')
  - [Up](#F-epam-wilma_service_api-WilmaService-StubConfigOrder-Up 'epam.wilma_service_api.WilmaService.StubConfigOrder.Up')
- [StubConfigStatus](#T-epam-wilma_service_api-WilmaService-StubConfigStatus 'epam.wilma_service_api.WilmaService.StubConfigStatus')
  - [Disabled](#F-epam-wilma_service_api-WilmaService-StubConfigStatus-Disabled 'epam.wilma_service_api.WilmaService.StubConfigStatus.Disabled')
  - [Enabled](#F-epam-wilma_service_api-WilmaService-StubConfigStatus-Enabled 'epam.wilma_service_api.WilmaService.StubConfigStatus.Enabled')
- [WilmaService](#T-epam-wilma_service_api-WilmaService 'epam.wilma_service_api.WilmaService')
  - [#ctor(config,logger,httpClient)](#M-epam-wilma_service_api-WilmaService-#ctor-epam-wilma_service_api-WilmaServiceConfig,epam-wilma_service_api-ILogger,System-Net-Http-HttpClient- 'epam.wilma_service_api.WilmaService.#ctor(epam.wilma_service_api.WilmaServiceConfig,epam.wilma_service_api.ILogger,System.Net.Http.HttpClient)')
  - [#ctor(config,logger)](#M-epam-wilma_service_api-WilmaService-#ctor-epam-wilma_service_api-WilmaServiceConfig,epam-wilma_service_api-ILogger- 'epam.wilma_service_api.WilmaService.#ctor(epam.wilma_service_api.WilmaServiceConfig,epam.wilma_service_api.ILogger)')
  - [ChangeStubConfigOrderAsync(groupName,order)](#M-epam-wilma_service_api-WilmaService-ChangeStubConfigOrderAsync-System-String,epam-wilma_service_api-WilmaService-StubConfigOrder- 'epam.wilma_service_api.WilmaService.ChangeStubConfigOrderAsync(System.String,epam.wilma_service_api.WilmaService.StubConfigOrder)')
  - [ChangeStubConfigStatusAsync(groupName,status)](#M-epam-wilma_service_api-WilmaService-ChangeStubConfigStatusAsync-System-String,epam-wilma_service_api-WilmaService-StubConfigStatus- 'epam.wilma_service_api.WilmaService.ChangeStubConfigStatusAsync(System.String,epam.wilma_service_api.WilmaService.StubConfigStatus)')
  - [DropStubConfigAsync(groupName)](#M-epam-wilma_service_api-WilmaService-DropStubConfigAsync-System-String- 'epam.wilma_service_api.WilmaService.DropStubConfigAsync(System.String)')
  - [GetActualLoadInformationAsync()](#M-epam-wilma_service_api-WilmaService-GetActualLoadInformationAsync 'epam.wilma_service_api.WilmaService.GetActualLoadInformationAsync')
  - [GetLocalhostBlockingStatusAsync()](#M-epam-wilma_service_api-WilmaService-GetLocalhostBlockingStatusAsync 'epam.wilma_service_api.WilmaService.GetLocalhostBlockingStatusAsync')
  - [GetMessageLoggingStatusAsync()](#M-epam-wilma_service_api-WilmaService-GetMessageLoggingStatusAsync 'epam.wilma_service_api.WilmaService.GetMessageLoggingStatusAsync')
  - [GetOperationModeAsync()](#M-epam-wilma_service_api-WilmaService-GetOperationModeAsync 'epam.wilma_service_api.WilmaService.GetOperationModeAsync')
  - [GetStubConfigInformationAsync()](#M-epam-wilma_service_api-WilmaService-GetStubConfigInformationAsync 'epam.wilma_service_api.WilmaService.GetStubConfigInformationAsync')
  - [GetVersionInformationAsync()](#M-epam-wilma_service_api-WilmaService-GetVersionInformationAsync 'epam.wilma_service_api.WilmaService.GetVersionInformationAsync')
  - [PersistActualStubConfigAsync()](#M-epam-wilma_service_api-WilmaService-PersistActualStubConfigAsync 'epam.wilma_service_api.WilmaService.PersistActualStubConfigAsync')
  - [SetLocalhostBlockingStatusAsync(control)](#M-epam-wilma_service_api-WilmaService-SetLocalhostBlockingStatusAsync-epam-wilma_service_api-WilmaService-LocalhostControlStatuses- 'epam.wilma_service_api.WilmaService.SetLocalhostBlockingStatusAsync(epam.wilma_service_api.WilmaService.LocalhostControlStatuses)')
  - [SetMessageLoggingStatusAsync(control)](#M-epam-wilma_service_api-WilmaService-SetMessageLoggingStatusAsync-epam-wilma_service_api-WilmaService-MessageLoggingControlStatus- 'epam.wilma_service_api.WilmaService.SetMessageLoggingStatusAsync(epam.wilma_service_api.WilmaService.MessageLoggingControlStatus)')
  - [SetOperationModeAsync(modes)](#M-epam-wilma_service_api-WilmaService-SetOperationModeAsync-epam-wilma_service_api-WilmaService-OperationModes- 'epam.wilma_service_api.WilmaService.SetOperationModeAsync(epam.wilma_service_api.WilmaService.OperationModes)')
  - [ShutdownApplicationAsync()](#M-epam-wilma_service_api-WilmaService-ShutdownApplicationAsync 'epam.wilma_service_api.WilmaService.ShutdownApplicationAsync')
  - [UploadConditionCheckerAsync(fileName,stream)](#M-epam-wilma_service_api-WilmaService-UploadConditionCheckerAsync-System-String,System-IO-Stream- 'epam.wilma_service_api.WilmaService.UploadConditionCheckerAsync(System.String,System.IO.Stream)')
  - [UploadStubConfigurationAsync(fileName,stream)](#M-epam-wilma_service_api-WilmaService-UploadStubConfigurationAsync-System-String,System-IO-Stream- 'epam.wilma_service_api.WilmaService.UploadStubConfigurationAsync(System.String,System.IO.Stream)')
  - [UploadTemplateAsync(fileName,stream)](#M-epam-wilma_service_api-WilmaService-UploadTemplateAsync-System-String,System-IO-Stream- 'epam.wilma_service_api.WilmaService.UploadTemplateAsync(System.String,System.IO.Stream)')
  - [UploadTemplateFormatterAsync(fileName,stream)](#M-epam-wilma_service_api-WilmaService-UploadTemplateFormatterAsync-System-String,System-IO-Stream- 'epam.wilma_service_api.WilmaService.UploadTemplateFormatterAsync(System.String,System.IO.Stream)')
- [WilmaServiceConfig](#T-epam-wilma_service_api-WilmaServiceConfig 'epam.wilma_service_api.WilmaServiceConfig')
  - [#ctor(host,port)](#M-epam-wilma_service_api-WilmaServiceConfig-#ctor-System-String,System-UInt16- 'epam.wilma_service_api.WilmaServiceConfig.#ctor(System.String,System.UInt16)')
  - [Host](#P-epam-wilma_service_api-WilmaServiceConfig-Host 'epam.wilma_service_api.WilmaServiceConfig.Host')
  - [Port](#P-epam-wilma_service_api-WilmaServiceConfig-Port 'epam.wilma_service_api.WilmaServiceConfig.Port')

<a name='assembly'></a>
# wilma-service-api-net [#](#assembly 'Go To Here') [=](#contents 'Back To Contents')

<a name='T-epam-wilma_service_api-ILogger'></a>
## ILogger [#](#T-epam-wilma_service_api-ILogger 'Go To Here') [=](#contents 'Back To Contents')

##### Namespace

epam.wilma_service_api

##### Summary

ILogger interface. Implement this to support WilmaService logging.

<a name='M-epam-wilma_service_api-ILogger-Debug-System-String,System-Object[]-'></a>
### Debug(format,prs) `method` [#](#M-epam-wilma_service_api-ILogger-Debug-System-String,System-Object[]- 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Debug

##### Parameters

| Name | Type | Description |
| ---- | ---- | ----------- |
| format | [System.String](http://msdn.microsoft.com/query/dev14.query?appId=Dev14IDEF1&l=EN-US&k=k:System.String 'System.String') | Format string. |
| prs | [System.Object[]](http://msdn.microsoft.com/query/dev14.query?appId=Dev14IDEF1&l=EN-US&k=k:System.Object[] 'System.Object[]') | Parameters |

<a name='M-epam-wilma_service_api-ILogger-Error-System-String,System-Object[]-'></a>
### Error(format,prs) `method` [#](#M-epam-wilma_service_api-ILogger-Error-System-String,System-Object[]- 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Error

##### Parameters

| Name | Type | Description |
| ---- | ---- | ----------- |
| format | [System.String](http://msdn.microsoft.com/query/dev14.query?appId=Dev14IDEF1&l=EN-US&k=k:System.String 'System.String') | Format string. |
| prs | [System.Object[]](http://msdn.microsoft.com/query/dev14.query?appId=Dev14IDEF1&l=EN-US&k=k:System.Object[] 'System.Object[]') | Parameters |

<a name='M-epam-wilma_service_api-ILogger-Info-System-String,System-Object[]-'></a>
### Info(format,prs) `method` [#](#M-epam-wilma_service_api-ILogger-Info-System-String,System-Object[]- 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Info

##### Parameters

| Name | Type | Description |
| ---- | ---- | ----------- |
| format | [System.String](http://msdn.microsoft.com/query/dev14.query?appId=Dev14IDEF1&l=EN-US&k=k:System.String 'System.String') | Format string. |
| prs | [System.Object[]](http://msdn.microsoft.com/query/dev14.query?appId=Dev14IDEF1&l=EN-US&k=k:System.Object[] 'System.Object[]') | Parameters |

<a name='M-epam-wilma_service_api-ILogger-Warning-System-String,System-Object[]-'></a>
### Warning(format,prs) `method` [#](#M-epam-wilma_service_api-ILogger-Warning-System-String,System-Object[]- 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Warning

##### Parameters

| Name | Type | Description |
| ---- | ---- | ----------- |
| format | [System.String](http://msdn.microsoft.com/query/dev14.query?appId=Dev14IDEF1&l=EN-US&k=k:System.String 'System.String') | Format string. |
| prs | [System.Object[]](http://msdn.microsoft.com/query/dev14.query?appId=Dev14IDEF1&l=EN-US&k=k:System.Object[] 'System.Object[]') | Parameters |

<a name='T-epam-wilma_service_api-ServiceCommClasses-LoadInformation'></a>
## LoadInformation [#](#T-epam-wilma_service_api-ServiceCommClasses-LoadInformation 'Go To Here') [=](#contents 'Back To Contents')

##### Namespace

epam.wilma_service_api.ServiceCommClasses

##### Summary

Wilma Service Load Information.

<a name='P-epam-wilma_service_api-ServiceCommClasses-LoadInformation-CountOfMessages'></a>
### CountOfMessages `property` [#](#P-epam-wilma_service_api-ServiceCommClasses-LoadInformation-CountOfMessages 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Messages count.

<a name='P-epam-wilma_service_api-ServiceCommClasses-LoadInformation-DeletedFilesCount'></a>
### DeletedFilesCount `property` [#](#P-epam-wilma_service_api-ServiceCommClasses-LoadInformation-DeletedFilesCount 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Deleted files count.

<a name='P-epam-wilma_service_api-ServiceCommClasses-LoadInformation-LoggerQueueSize'></a>
### LoggerQueueSize `property` [#](#P-epam-wilma_service_api-ServiceCommClasses-LoadInformation-LoggerQueueSize 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Logger queue size.

<a name='P-epam-wilma_service_api-ServiceCommClasses-LoadInformation-ResponseQueueSize'></a>
### ResponseQueueSize `property` [#](#P-epam-wilma_service_api-ServiceCommClasses-LoadInformation-ResponseQueueSize 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Response queue size.

<a name='T-epam-wilma_service_api-ServiceCommClasses-LocalhostControlStatus'></a>
## LocalhostControlStatus [#](#T-epam-wilma_service_api-ServiceCommClasses-LocalhostControlStatus 'Go To Here') [=](#contents 'Back To Contents')

##### Namespace

epam.wilma_service_api.ServiceCommClasses

##### Summary

LocalhostControlStatus class.

<a name='P-epam-wilma_service_api-ServiceCommClasses-LocalhostControlStatus-LocalhostMode'></a>
### LocalhostMode `property` [#](#P-epam-wilma_service_api-ServiceCommClasses-LocalhostControlStatus-LocalhostMode 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

LocalhostMode

<a name='T-epam-wilma_service_api-WilmaService-LocalhostControlStatuses'></a>
## LocalhostControlStatuses [#](#T-epam-wilma_service_api-WilmaService-LocalhostControlStatuses 'Go To Here') [=](#contents 'Back To Contents')

##### Namespace

epam.wilma_service_api.WilmaService

##### Summary

LocalhostControlStatuses

<a name='F-epam-wilma_service_api-WilmaService-LocalhostControlStatuses-Error'></a>
### Error `constants` [#](#F-epam-wilma_service_api-WilmaService-LocalhostControlStatuses-Error 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Communication error.

<a name='F-epam-wilma_service_api-WilmaService-LocalhostControlStatuses-Off'></a>
### Off `constants` [#](#F-epam-wilma_service_api-WilmaService-LocalhostControlStatuses-Off 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Off.

<a name='F-epam-wilma_service_api-WilmaService-LocalhostControlStatuses-On'></a>
### On `constants` [#](#F-epam-wilma_service_api-WilmaService-LocalhostControlStatuses-On 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

On.

<a name='T-epam-wilma_service_api-ServiceCommClasses-LoggingStatus'></a>
## LoggingStatus [#](#T-epam-wilma_service_api-ServiceCommClasses-LoggingStatus 'Go To Here') [=](#contents 'Back To Contents')

##### Namespace

epam.wilma_service_api.ServiceCommClasses

##### Summary

LoggingStatus class.

<a name='P-epam-wilma_service_api-ServiceCommClasses-LoggingStatus-RequestLogging'></a>
### RequestLogging `property` [#](#P-epam-wilma_service_api-ServiceCommClasses-LoggingStatus-RequestLogging 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Request logging

<a name='P-epam-wilma_service_api-ServiceCommClasses-LoggingStatus-ResponseLogging'></a>
### ResponseLogging `property` [#](#P-epam-wilma_service_api-ServiceCommClasses-LoggingStatus-ResponseLogging 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Response logging

<a name='T-epam-wilma_service_api-WilmaService-MessageLoggingControlStatus'></a>
## MessageLoggingControlStatus [#](#T-epam-wilma_service_api-WilmaService-MessageLoggingControlStatus 'Go To Here') [=](#contents 'Back To Contents')

##### Namespace

epam.wilma_service_api.WilmaService

##### Summary

MessageLoggingControlStatus

<a name='F-epam-wilma_service_api-WilmaService-MessageLoggingControlStatus-Error'></a>
### Error `constants` [#](#F-epam-wilma_service_api-WilmaService-MessageLoggingControlStatus-Error 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Communication error.

<a name='F-epam-wilma_service_api-WilmaService-MessageLoggingControlStatus-Off'></a>
### Off `constants` [#](#F-epam-wilma_service_api-WilmaService-MessageLoggingControlStatus-Off 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Off.

<a name='F-epam-wilma_service_api-WilmaService-MessageLoggingControlStatus-On'></a>
### On `constants` [#](#F-epam-wilma_service_api-WilmaService-MessageLoggingControlStatus-On 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

On.

<a name='T-epam-wilma_service_api-ServiceCommClasses-OperationMode'></a>
## OperationMode [#](#T-epam-wilma_service_api-ServiceCommClasses-OperationMode 'Go To Here') [=](#contents 'Back To Contents')

##### Namespace

epam.wilma_service_api.ServiceCommClasses

##### Summary

OperationMode class.

<a name='P-epam-wilma_service_api-ServiceCommClasses-OperationMode-ProxyMode'></a>
### ProxyMode `property` [#](#P-epam-wilma_service_api-ServiceCommClasses-OperationMode-ProxyMode 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Proxy

<a name='P-epam-wilma_service_api-ServiceCommClasses-OperationMode-StubMode'></a>
### StubMode `property` [#](#P-epam-wilma_service_api-ServiceCommClasses-OperationMode-StubMode 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Stub

<a name='P-epam-wilma_service_api-ServiceCommClasses-OperationMode-WilmaMode'></a>
### WilmaMode `property` [#](#P-epam-wilma_service_api-ServiceCommClasses-OperationMode-WilmaMode 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Wilma

<a name='T-epam-wilma_service_api-WilmaService-OperationModes'></a>
## OperationModes [#](#T-epam-wilma_service_api-WilmaService-OperationModes 'Go To Here') [=](#contents 'Back To Contents')

##### Namespace

epam.wilma_service_api.WilmaService

##### Summary

OperationModes

<a name='F-epam-wilma_service_api-WilmaService-OperationModes-ERROR'></a>
### ERROR `constants` [#](#F-epam-wilma_service_api-WilmaService-OperationModes-ERROR 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Communication error.

<a name='F-epam-wilma_service_api-WilmaService-OperationModes-PROXY'></a>
### PROXY `constants` [#](#F-epam-wilma_service_api-WilmaService-OperationModes-PROXY 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Proxy.

<a name='F-epam-wilma_service_api-WilmaService-OperationModes-STUB'></a>
### STUB `constants` [#](#F-epam-wilma_service_api-WilmaService-OperationModes-STUB 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Stub.

<a name='F-epam-wilma_service_api-WilmaService-OperationModes-WILMA'></a>
### WILMA `constants` [#](#F-epam-wilma_service_api-WilmaService-OperationModes-WILMA 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Wilma.

<a name='T-epam-wilma_service_api-WilmaService-StubConfigOrder'></a>
## StubConfigOrder [#](#T-epam-wilma_service_api-WilmaService-StubConfigOrder 'Go To Here') [=](#contents 'Back To Contents')

##### Namespace

epam.wilma_service_api.WilmaService

##### Summary

StubConfigOrder

<a name='F-epam-wilma_service_api-WilmaService-StubConfigOrder-Down'></a>
### Down `constants` [#](#F-epam-wilma_service_api-WilmaService-StubConfigOrder-Down 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Down.

<a name='F-epam-wilma_service_api-WilmaService-StubConfigOrder-Up'></a>
### Up `constants` [#](#F-epam-wilma_service_api-WilmaService-StubConfigOrder-Up 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Up.

<a name='T-epam-wilma_service_api-WilmaService-StubConfigStatus'></a>
## StubConfigStatus [#](#T-epam-wilma_service_api-WilmaService-StubConfigStatus 'Go To Here') [=](#contents 'Back To Contents')

##### Namespace

epam.wilma_service_api.WilmaService

##### Summary

StubConfigStatus

<a name='F-epam-wilma_service_api-WilmaService-StubConfigStatus-Disabled'></a>
### Disabled `constants` [#](#F-epam-wilma_service_api-WilmaService-StubConfigStatus-Disabled 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Disabled.

<a name='F-epam-wilma_service_api-WilmaService-StubConfigStatus-Enabled'></a>
### Enabled `constants` [#](#F-epam-wilma_service_api-WilmaService-StubConfigStatus-Enabled 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Enabled.

<a name='T-epam-wilma_service_api-WilmaService'></a>
## WilmaService [#](#T-epam-wilma_service_api-WilmaService 'Go To Here') [=](#contents 'Back To Contents')

##### Namespace

epam.wilma_service_api

##### Summary

WilmaService class to acccess and manipulate programmatically WilmaApp.

<a name='M-epam-wilma_service_api-WilmaService-#ctor-epam-wilma_service_api-WilmaServiceConfig,epam-wilma_service_api-ILogger,System-Net-Http-HttpClient-'></a>
### #ctor(config,logger,httpClient) `constructor` [#](#M-epam-wilma_service_api-WilmaService-#ctor-epam-wilma_service_api-WilmaServiceConfig,epam-wilma_service_api-ILogger,System-Net-Http-HttpClient- 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

WilmaService constructor.

##### Parameters

| Name | Type | Description |
| ---- | ---- | ----------- |
| config | [epam.wilma_service_api.WilmaServiceConfig](#T-epam-wilma_service_api-WilmaServiceConfig 'epam.wilma_service_api.WilmaServiceConfig') | WilmaServiceConfig stores host and port. |
| logger | [epam.wilma_service_api.ILogger](#T-epam-wilma_service_api-ILogger 'epam.wilma_service_api.ILogger') | Object implements ILogger interface, this methods are called for logging. |
| httpClient | [System.Net.Http.HttpClient](http://msdn.microsoft.com/query/dev14.query?appId=Dev14IDEF1&l=EN-US&k=k:System.Net.Http.HttpClient 'System.Net.Http.HttpClient') | HttpClient. |

##### Exceptions

| Name | Description |
| ---- | ----------- |
| [System.ArgumentNullException](http://msdn.microsoft.com/query/dev14.query?appId=Dev14IDEF1&l=EN-US&k=k:System.ArgumentNullException 'System.ArgumentNullException') | Thrown when config or logger is null. |

<a name='M-epam-wilma_service_api-WilmaService-#ctor-epam-wilma_service_api-WilmaServiceConfig,epam-wilma_service_api-ILogger-'></a>
### #ctor(config,logger) `constructor` [#](#M-epam-wilma_service_api-WilmaService-#ctor-epam-wilma_service_api-WilmaServiceConfig,epam-wilma_service_api-ILogger- 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

WilmaService constructor.

##### Parameters

| Name | Type | Description |
| ---- | ---- | ----------- |
| config | [epam.wilma_service_api.WilmaServiceConfig](#T-epam-wilma_service_api-WilmaServiceConfig 'epam.wilma_service_api.WilmaServiceConfig') | WilmaServiceConfig stores host and port. |
| logger | [epam.wilma_service_api.ILogger](#T-epam-wilma_service_api-ILogger 'epam.wilma_service_api.ILogger') | Object implements ILogger interface, this methods are called for logging. |

##### Exceptions

| Name | Description |
| ---- | ----------- |
| [System.ArgumentNullException](http://msdn.microsoft.com/query/dev14.query?appId=Dev14IDEF1&l=EN-US&k=k:System.ArgumentNullException 'System.ArgumentNullException') | Thrown when config or logger is null. |

<a name='M-epam-wilma_service_api-WilmaService-ChangeStubConfigOrderAsync-System-String,epam-wilma_service_api-WilmaService-StubConfigOrder-'></a>
### ChangeStubConfigOrderAsync(groupName,order) `method` [#](#M-epam-wilma_service_api-WilmaService-ChangeStubConfigOrderAsync-System-String,epam-wilma_service_api-WilmaService-StubConfigOrder- 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Sets new order for the given group, move up or down in the list.

##### Returns

True if success, otherwise returns false.

##### Parameters

| Name | Type | Description |
| ---- | ---- | ----------- |
| groupName | [System.String](http://msdn.microsoft.com/query/dev14.query?appId=Dev14IDEF1&l=EN-US&k=k:System.String 'System.String') | Name of Group to move. |
| order | [epam.wilma_service_api.WilmaService.StubConfigOrder](#T-epam-wilma_service_api-WilmaService-StubConfigOrder 'epam.wilma_service_api.WilmaService.StubConfigOrder') | Move direction: Up/Down |

<a name='M-epam-wilma_service_api-WilmaService-ChangeStubConfigStatusAsync-System-String,epam-wilma_service_api-WilmaService-StubConfigStatus-'></a>
### ChangeStubConfigStatusAsync(groupName,status) `method` [#](#M-epam-wilma_service_api-WilmaService-ChangeStubConfigStatusAsync-System-String,epam-wilma_service_api-WilmaService-StubConfigStatus- 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Enable/disable the given group.

##### Returns

True if succesful, otherwise returns false.

##### Parameters

| Name | Type | Description |
| ---- | ---- | ----------- |
| groupName | [System.String](http://msdn.microsoft.com/query/dev14.query?appId=Dev14IDEF1&l=EN-US&k=k:System.String 'System.String') | Name of the Group. |
| status | [epam.wilma_service_api.WilmaService.StubConfigStatus](#T-epam-wilma_service_api-WilmaService-StubConfigStatus 'epam.wilma_service_api.WilmaService.StubConfigStatus') | Status to set: Enabled/Disabled |

<a name='M-epam-wilma_service_api-WilmaService-DropStubConfigAsync-System-String-'></a>
### DropStubConfigAsync(groupName) `method` [#](#M-epam-wilma_service_api-WilmaService-DropStubConfigAsync-System-String- 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Drops the given stub configuration.

##### Returns

True if succes, otherwise returns false.

##### Parameters

| Name | Type | Description |
| ---- | ---- | ----------- |
| groupName | [System.String](http://msdn.microsoft.com/query/dev14.query?appId=Dev14IDEF1&l=EN-US&k=k:System.String 'System.String') | Name of Group to drop. |

<a name='M-epam-wilma_service_api-WilmaService-GetActualLoadInformationAsync'></a>
### GetActualLoadInformationAsync() `method` [#](#M-epam-wilma_service_api-WilmaService-GetActualLoadInformationAsync 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Gets the actual load information of the application.

##### Returns

Actual load information, or null in case of communication problem.

##### Parameters

This method has no parameters.

<a name='M-epam-wilma_service_api-WilmaService-GetLocalhostBlockingStatusAsync'></a>
### GetLocalhostBlockingStatusAsync() `method` [#](#M-epam-wilma_service_api-WilmaService-GetLocalhostBlockingStatusAsync 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Gets the localhost blocking status.

##### Returns

Localhost blocking status if successful, otherwise LocalhostControlStatuses.Error.

##### Parameters

This method has no parameters.

<a name='M-epam-wilma_service_api-WilmaService-GetMessageLoggingStatusAsync'></a>
### GetMessageLoggingStatusAsync() `method` [#](#M-epam-wilma_service_api-WilmaService-GetMessageLoggingStatusAsync 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Gets the message logging status.

##### Returns

Message logging status, or MessageLoggingControlStatus.Error in case of communication problem.

##### Parameters

This method has no parameters.

<a name='M-epam-wilma_service_api-WilmaService-GetOperationModeAsync'></a>
### GetOperationModeAsync() `method` [#](#M-epam-wilma_service_api-WilmaService-GetOperationModeAsync 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Gets the operation mode status.

##### Returns

Operation mode status, or OperationModes.ERROR in case of communication problem.

##### Parameters

This method has no parameters.

<a name='M-epam-wilma_service_api-WilmaService-GetStubConfigInformationAsync'></a>
### GetStubConfigInformationAsync() `method` [#](#M-epam-wilma_service_api-WilmaService-GetStubConfigInformationAsync 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Gets the stub configuration information.

##### Returns

Stub configuration as JsonObject.

##### Parameters

This method has no parameters.

<a name='M-epam-wilma_service_api-WilmaService-GetVersionInformationAsync'></a>
### GetVersionInformationAsync() `method` [#](#M-epam-wilma_service_api-WilmaService-GetVersionInformationAsync 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Gets the version of Wilma.

##### Returns

The version information, or null in case of communication problem.

##### Parameters

This method has no parameters.

<a name='M-epam-wilma_service_api-WilmaService-PersistActualStubConfigAsync'></a>
### PersistActualStubConfigAsync() `method` [#](#M-epam-wilma_service_api-WilmaService-PersistActualStubConfigAsync 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Persists the actual stub configuration.

##### Returns

True if success, otherwise returns false.

##### Parameters

This method has no parameters.

<a name='M-epam-wilma_service_api-WilmaService-SetLocalhostBlockingStatusAsync-epam-wilma_service_api-WilmaService-LocalhostControlStatuses-'></a>
### SetLocalhostBlockingStatusAsync(control) `method` [#](#M-epam-wilma_service_api-WilmaService-SetLocalhostBlockingStatusAsync-epam-wilma_service_api-WilmaService-LocalhostControlStatuses- 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Turns on/off the localhost blocking.

##### Returns

True if the request is successful, otherwise returns false.

##### Parameters

| Name | Type | Description |
| ---- | ---- | ----------- |
| control | [epam.wilma_service_api.WilmaService.LocalhostControlStatuses](#T-epam-wilma_service_api-WilmaService-LocalhostControlStatuses 'epam.wilma_service_api.WilmaService.LocalhostControlStatuses') | Control on/off |

<a name='M-epam-wilma_service_api-WilmaService-SetMessageLoggingStatusAsync-epam-wilma_service_api-WilmaService-MessageLoggingControlStatus-'></a>
### SetMessageLoggingStatusAsync(control) `method` [#](#M-epam-wilma_service_api-WilmaService-SetMessageLoggingStatusAsync-epam-wilma_service_api-WilmaService-MessageLoggingControlStatus- 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Turns on/off the message logging status.

##### Returns

True if the request is successful, otherwise returns false.

##### Parameters

| Name | Type | Description |
| ---- | ---- | ----------- |
| control | [epam.wilma_service_api.WilmaService.MessageLoggingControlStatus](#T-epam-wilma_service_api-WilmaService-MessageLoggingControlStatus 'epam.wilma_service_api.WilmaService.MessageLoggingControlStatus') | Control on/off |

<a name='M-epam-wilma_service_api-WilmaService-SetOperationModeAsync-epam-wilma_service_api-WilmaService-OperationModes-'></a>
### SetOperationModeAsync(modes) `method` [#](#M-epam-wilma_service_api-WilmaService-SetOperationModeAsync-epam-wilma_service_api-WilmaService-OperationModes- 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Switch the operation mode.

##### Returns

True if the request is successful, otherwise returns false.

##### Parameters

| Name | Type | Description |
| ---- | ---- | ----------- |
| modes | [epam.wilma_service_api.WilmaService.OperationModes](#T-epam-wilma_service_api-WilmaService-OperationModes 'epam.wilma_service_api.WilmaService.OperationModes') | Mode: wilma/stub/proxy |

<a name='M-epam-wilma_service_api-WilmaService-ShutdownApplicationAsync'></a>
### ShutdownApplicationAsync() `method` [#](#M-epam-wilma_service_api-WilmaService-ShutdownApplicationAsync 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Shutdown the Wilma application.

##### Returns

True if the request is successful, otherwise returns false

##### Parameters

This method has no parameters.

<a name='M-epam-wilma_service_api-WilmaService-UploadConditionCheckerAsync-System-String,System-IO-Stream-'></a>
### UploadConditionCheckerAsync(fileName,stream) `method` [#](#M-epam-wilma_service_api-WilmaService-UploadConditionCheckerAsync-System-String,System-IO-Stream- 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Uploads condition checker configuration.

##### Returns

True if success, otherwise returns false.

##### Parameters

| Name | Type | Description |
| ---- | ---- | ----------- |
| fileName | [System.String](http://msdn.microsoft.com/query/dev14.query?appId=Dev14IDEF1&l=EN-US&k=k:System.String 'System.String') | Name of file. |
| stream | [System.IO.Stream](http://msdn.microsoft.com/query/dev14.query?appId=Dev14IDEF1&l=EN-US&k=k:System.IO.Stream 'System.IO.Stream') | FileStream to upload. |

<a name='M-epam-wilma_service_api-WilmaService-UploadStubConfigurationAsync-System-String,System-IO-Stream-'></a>
### UploadStubConfigurationAsync(fileName,stream) `method` [#](#M-epam-wilma_service_api-WilmaService-UploadStubConfigurationAsync-System-String,System-IO-Stream- 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Uploads Stub Configuration.

##### Returns

True if success, otherwise returns false.

##### Parameters

| Name | Type | Description |
| ---- | ---- | ----------- |
| fileName | [System.String](http://msdn.microsoft.com/query/dev14.query?appId=Dev14IDEF1&l=EN-US&k=k:System.String 'System.String') | Name of file. |
| stream | [System.IO.Stream](http://msdn.microsoft.com/query/dev14.query?appId=Dev14IDEF1&l=EN-US&k=k:System.IO.Stream 'System.IO.Stream') | FileStream to upload. |

<a name='M-epam-wilma_service_api-WilmaService-UploadTemplateAsync-System-String,System-IO-Stream-'></a>
### UploadTemplateAsync(fileName,stream) `method` [#](#M-epam-wilma_service_api-WilmaService-UploadTemplateAsync-System-String,System-IO-Stream- 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Uploads template.

##### Returns

True if success, otherwise returns false.

##### Parameters

| Name | Type | Description |
| ---- | ---- | ----------- |
| fileName | [System.String](http://msdn.microsoft.com/query/dev14.query?appId=Dev14IDEF1&l=EN-US&k=k:System.String 'System.String') | Name of file. |
| stream | [System.IO.Stream](http://msdn.microsoft.com/query/dev14.query?appId=Dev14IDEF1&l=EN-US&k=k:System.IO.Stream 'System.IO.Stream') | FileStream to upload. |

<a name='M-epam-wilma_service_api-WilmaService-UploadTemplateFormatterAsync-System-String,System-IO-Stream-'></a>
### UploadTemplateFormatterAsync(fileName,stream) `method` [#](#M-epam-wilma_service_api-WilmaService-UploadTemplateFormatterAsync-System-String,System-IO-Stream- 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Uploads template formatter.

##### Returns

True if success, otherwise returns false.

##### Parameters

| Name | Type | Description |
| ---- | ---- | ----------- |
| fileName | [System.String](http://msdn.microsoft.com/query/dev14.query?appId=Dev14IDEF1&l=EN-US&k=k:System.String 'System.String') | Name of file. |
| stream | [System.IO.Stream](http://msdn.microsoft.com/query/dev14.query?appId=Dev14IDEF1&l=EN-US&k=k:System.IO.Stream 'System.IO.Stream') | FileStream to upload. |

<a name='T-epam-wilma_service_api-WilmaServiceConfig'></a>
## WilmaServiceConfig [#](#T-epam-wilma_service_api-WilmaServiceConfig 'Go To Here') [=](#contents 'Back To Contents')

##### Namespace

epam.wilma_service_api

##### Summary

Configuraation class to store WilmaService config.

<a name='M-epam-wilma_service_api-WilmaServiceConfig-#ctor-System-String,System-UInt16-'></a>
### #ctor(host,port) `constructor` [#](#M-epam-wilma_service_api-WilmaServiceConfig-#ctor-System-String,System-UInt16- 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

Constructor.

##### Parameters

| Name | Type | Description |
| ---- | ---- | ----------- |
| host | [System.String](http://msdn.microsoft.com/query/dev14.query?appId=Dev14IDEF1&l=EN-US&k=k:System.String 'System.String') | WilmaApp host. |
| port | [System.UInt16](http://msdn.microsoft.com/query/dev14.query?appId=Dev14IDEF1&l=EN-US&k=k:System.UInt16 'System.UInt16') | WilmaApp port. |

<a name='P-epam-wilma_service_api-WilmaServiceConfig-Host'></a>
### Host `property` [#](#P-epam-wilma_service_api-WilmaServiceConfig-Host 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

WilmaApp host.

<a name='P-epam-wilma_service_api-WilmaServiceConfig-Port'></a>
### Port `property` [#](#P-epam-wilma_service_api-WilmaServiceConfig-Port 'Go To Here') [=](#contents 'Back To Contents')

##### Summary

WilmaApp port.
