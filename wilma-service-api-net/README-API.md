<a name='contents'></a>
# Contents [#](#contents 'Go To Here')

- [ILogger](#T-epam-wilma_service_api-ILogger 'epam.wilma_service_api.ILogger')
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
