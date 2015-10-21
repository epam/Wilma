<?xml version="1.0"?>
<doc>
    <assembly>
        <name>wilma-service-api-net</name>
    </assembly>
    <members>
        <member name="T:epam.wilma_service_api.ILogger">
            <summary>
            ILogger interface. Implement this to support WilmaService logging.
            </summary>
        </member>
        <member name="T:epam.wilma_service_api.WilmaService">
            <summary>
            WilmaService class to acccess and manipulate programmatically WilmaApp.
            </summary>
        </member>
        <member name="M:epam.wilma_service_api.WilmaService.#ctor(epam.wilma_service_api.WilmaServiceConfig,epam.wilma_service_api.ILogger,System.Net.Http.HttpClient)">
            <summary>
            WilmaService constructor.
            </summary>
            <param name="config">WilmaServiceConfig stores host and port.</param>
            <param name="logger">Object implements ILogger interface, this methods are called for logging.</param>
            <param name="httpClient">HttpClient.</param>
            <exception cref="T:System.ArgumentNullException">Thrown when config or logger is null.</exception>
        </member>
        <member name="M:epam.wilma_service_api.WilmaService.#ctor(epam.wilma_service_api.WilmaServiceConfig,epam.wilma_service_api.ILogger)">
            <summary>
            WilmaService constructor.
            </summary>
            <param name="config">WilmaServiceConfig stores host and port.</param>
            <param name="logger">Object implements ILogger interface, this methods are called for logging.</param>
            <exception cref="T:System.ArgumentNullException">Thrown when config or logger is null.</exception>
        </member>
        <member name="M:epam.wilma_service_api.WilmaService.GetVersionInformationAsync">
            <summary>
            Gets the version of Wilma.
            </summary>
            <returns>The version information, or null in case of communication problem.</returns>
        </member>
        <member name="M:epam.wilma_service_api.WilmaService.GetActualLoadInformationAsync">
            <summary>
            Gets the actual load information of the application.
            </summary>
            <returns>Actual load information, or null in case of communication problem.</returns>
        </member>
        <member name="M:epam.wilma_service_api.WilmaService.ShutdownApplicationAsync">
            <summary>
            Shutdown the Wilma application.
            </summary>
            <returns>True if the request is successful, otherwise returns false</returns>
        </member>
        <member name="M:epam.wilma_service_api.WilmaService.GetMessageLoggingStatusAsync">
            <summary>
            Gets the message logging status.
            </summary>
            <returns>Message logging status, or MessageLoggingControlStatus.Error in case of communication problem.</returns>
        </member>
        <member name="M:epam.wilma_service_api.WilmaService.SetMessageLoggingStatusAsync(epam.wilma_service_api.WilmaService.MessageLoggingControlStatus)">
            <summary>
            Turns on/off the message logging status.
            </summary>
            <param name="control">Control on/off</param>
            <returns>True if the request is successful, otherwise returns false.</returns>
        </member>
        <member name="M:epam.wilma_service_api.WilmaService.GetOperationModeAsync">
            <summary>
            Gets the operation mode status.
            </summary>
            <returns>Operation mode status, or OperationModes.ERROR in case of communication problem.</returns>
        </member>
        <member name="M:epam.wilma_service_api.WilmaService.SetOperationModeAsync(epam.wilma_service_api.WilmaService.OperationModes)">
            <summary>
            Switch the operation mode.
            </summary>
            <param name="modes">Mode: wilma/stub/proxy</param>
            <returns>True if the request is successful, otherwise returns false.</returns>
        </member>
        <member name="M:epam.wilma_service_api.WilmaService.GetLocalhostBlockingStatusAsync">
            <summary>
            Gets the localhost blocking status.
            </summary>
            <returns>Localhost blocking status if successful, otherwise LocalhostControlStatuses.Error.</returns>
        </member>
        <member name="M:epam.wilma_service_api.WilmaService.SetLocalhostBlockingStatusAsync(epam.wilma_service_api.WilmaService.LocalhostControlStatuses)">
            <summary>
            Turns on/off the localhost blocking.
            </summary>
            <param name="control">Control on/off</param>
            <returns>True if the request is successful, otherwise returns false.</returns>
        </member>
        <member name="M:epam.wilma_service_api.WilmaService.GetStubConfigInformationAsync">
            <summary>
            Gets the stub configuration information.
            </summary>
            <returns>Stub configuration as JsonObject.</returns>
        </member>
        <member name="M:epam.wilma_service_api.WilmaService.ChangeStubConfigStatusAsync(System.String,epam.wilma_service_api.WilmaService.StubConfigStatus)">
            <summary>
            Enable/disable the given group.
            </summary>
            <param name="groupName">Name of the Group.</param>
            <param name="status">Status to set: Enabled/Disabled</param>
            <returns>True if succesful, otherwise returns false.</returns>
        </member>
        <member name="M:epam.wilma_service_api.WilmaService.ChangeStubConfigOrderAsync(System.String,epam.wilma_service_api.WilmaService.StubConfigOrder)">
            <summary>
             Sets new order for the given group, move up or down in the list.
            </summary>
            <param name="groupName">Name of Group to move.</param>
            <param name="order">Move direction: Up/Down</param>
            <returns>True if success, otherwise returns false.</returns>
        </member>
        <member name="M:epam.wilma_service_api.WilmaService.DropStubConfigAsync(System.String)">
            <summary>
            Drops the given stub configuration.
            </summary>
            <param name="groupName">Name of Group to drop.</param>
            <returns>True if succes, otherwise returns false.</returns>
        </member>
        <member name="M:epam.wilma_service_api.WilmaService.PersistActualStubConfigAsync">
            <summary>
            Persists the actual stub configuration.
            </summary>
            <returns>True if success, otherwise returns false.</returns>
        </member>
        <member name="M:epam.wilma_service_api.WilmaService.UploadConditionCheckerAsync(System.String,System.IO.Stream)">
            <summary>
            Uploads condition checker configuration.
            </summary>
            <param name="fileName">Name of file.</param>
            <param name="stream">FileStream to upload.</param>
            <returns>True if success, otherwise returns false.</returns>
        </member>
        <member name="M:epam.wilma_service_api.WilmaService.UploadTemplateAsync(System.String,System.IO.Stream)">
            <summary>
            Uploads template.
            </summary>
            <param name="fileName">Name of file.</param>
            <param name="stream">FileStream to upload.</param>
            <returns>True if success, otherwise returns false.</returns>
        </member>
        <member name="M:epam.wilma_service_api.WilmaService.UploadTemplateFormatterAsync(System.String,System.IO.Stream)">
            <summary>
            Uploads template formatter.
            </summary>
            <param name="fileName">Name of file.</param>
            <param name="stream">FileStream to upload.</param>
            <returns>True if success, otherwise returns false.</returns>
        </member>
        <member name="M:epam.wilma_service_api.WilmaService.UploadStubConfigurationAsync(System.String,System.IO.Stream)">
            <summary>
            Uploads Stub Configuration.
            </summary>
            <param name="fileName">Name of file.</param>
            <param name="stream">FileStream to upload.</param>
            <returns>True if success, otherwise returns false.</returns>
        </member>
        <member name="T:epam.wilma_service_api.WilmaServiceConfig">
            <summary>
            Configuraation class to store WilmaService config.
            </summary>
        </member>
        <member name="M:epam.wilma_service_api.WilmaServiceConfig.#ctor(System.String,System.UInt16)">
            <summary>
            Constructor.
            </summary>
            <param name="host">WilmaApp host.</param>
            <param name="port">WilmaApp port.</param>
        </member>
        <member name="P:epam.wilma_service_api.WilmaServiceConfig.Host">
            <summary>
            WilmaApp host.
            </summary>
        </member>
        <member name="P:epam.wilma_service_api.WilmaServiceConfig.Port">
            <summary>
            WilmaApp port.
            </summary>
        </member>
    </members>
</doc>
