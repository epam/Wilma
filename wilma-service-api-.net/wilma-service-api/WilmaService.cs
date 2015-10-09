using System;
using System.Diagnostics;
using System.Threading.Tasks;
using System.Net.Http;
using Newtonsoft.Json;
using System.Collections.Generic;
using System.Diagnostics.Contracts;
using System.IO;
using System.Net.NetworkInformation;
using epam.wilma_service_api.ServiceCommClasses;

namespace epam.wilma_service_api
{
    /// <summary>
    /// WilmaService class to acccess and manipulate programmatically WilmaApp.
    /// </summary>
    public class WilmaService
    {
        /// <summary>
        /// ILogger interface. Implement this to support WilmaService logging.
        /// </summary>
        public interface ILogger
        {
            void Debug(string format, params object[] prs);
            void Warning(string format, params object[] prs);
            void Error(string format, params object[] prs);
            void Info(string format, params object[] prs);
        }


        #region ENUMS

        public enum MessageLoggingControlStatus
        {
            Error,
            On,
            Off,
        }

        public enum OperationModes
        {
            ERROR,
            WILMA,
            STUB,
            PROXY
        }

        public enum LocalhostControlStatuses
        {
            Error,
            On,
            Off
        }

        public enum StubConfigStatus
        {
            Enabled,
            Disabled
        }

        public enum StubConfigOrder
        {
            Up = 1,
            Down = -1
        }


        #endregion ENUMS

        #region PRIVATES

        private readonly WilmaServiceConfig _config;
        private string GetUrl(string postfix)
        {
            return string.Format("http://{0}:{1}/{2}", _config.Host, _config.Port, postfix);
        }
        private string GetUrl(string postfixFormat, params object[] prms)
        {
            return string.Format("http://{0}:{1}/{2}", _config.Host, _config.Port, string.Format(postfixFormat, prms));
        }

        private ILogger _logger;

        #endregion PRIVATES

        /// <summary>
        /// WilmaService constructor.
        /// </summary>
        /// <param name="config">WilmaServiceConfig stores host and port.</param>
        /// <param name="logger">Object implements ILogger interface, this methods are called for logging.</param>
        /// <exception cref="System.ArgumentNullException">Thrown when config or logger is null.</exception>
        public WilmaService(WilmaServiceConfig config, ILogger logger)
        {
            _logger = logger;
            if (_logger == null)
            {
                throw new ArgumentNullException("ILogger is null.");
            }

            _logger.Info("WilmaService created.");

            if (config == null)
            {
                _logger.Error("WilmaServiceConfig is NULL.");

                throw new ArgumentNullException("WilmaServiceConfig is NULL.");
            }

            _config = config;
        }

        #region APP RELATED

        private const string VERSION_INFO_URL_POSTFIX = "config/public/version";
        private const string ACTUAL_LOAD_INFO_URL_POSTFIX = "config/public/actualload";
        private const string SHUTDOWN_URL_POSTFIX = "config/admin/shutdown";

        /// <summary>
        /// Gets the version of Wilma.
        /// </summary>
        /// <returns>The version information, or null in case of communication problem.</returns>
        public async Task<string> GetVersionInformationAsync()
        {
            _logger.Debug("WilmaService GetVersionInformationAsync enter...");

            using (var client = new HttpClient())
            {
                var resp = await client.GetAsync(GetUrl(VERSION_INFO_URL_POSTFIX));

                if (resp.IsSuccessStatusCode)
                {
                    _logger.Debug("WilmaService GetVersionInformationAsync success.");

                    return await resp.Content.ReadAsStringAsync();
                }

                _logger.Debug("WilmaService GetVersionInformationAsync failed: {0}", resp.StatusCode);
                return null;
            }
        }

        /// <summary>
        /// Gets the actual load information of the application.
        /// </summary>
        /// <returns>Actual load information, or null in case of communication problem.</returns>
        public async Task<LoadInformation> GetActualLoadInformationAsync()
        {
            _logger.Debug("WilmaService GetActualLoadInformation enter...");

            using (var client = new HttpClient())
            {
                var resp = await client.GetAsync(GetUrl(ACTUAL_LOAD_INFO_URL_POSTFIX));

                if (resp.IsSuccessStatusCode)
                {
                    var jsonStr = await resp.Content.ReadAsStringAsync();
                    _logger.Debug("WilmaService GetActualLoadInformation success, with result: {0}", jsonStr);

                    return JsonConvert.DeserializeObject<LoadInformation>(jsonStr);
                }

                _logger.Debug("WilmaService GetActualLoadInformation failed: {0}", resp.StatusCode);
                return null;
            }
        }

        /// <summary>
        /// Shutdown the Wilma application.
        /// </summary>
        /// <returns>True if the request is successful, otherwise returns false</returns>
        public async Task<bool> ShutdownApplicationAsync()
        {
            _logger.Debug("WilmaService ShutdownApplication enter...");

            using (var client = new HttpClient())
            {
                var resp = await client.GetAsync(GetUrl(SHUTDOWN_URL_POSTFIX));

                if (resp.IsSuccessStatusCode)
                {
                    _logger.Debug("WilmaService ShutdownApplication success.");
                    return true;
                }

                _logger.Debug("WilmaService ShutdownApplication failed: {0}", resp.StatusCode);
                return false;
            }
        }

        #endregion APP RELATED

        #region LOGGING STATUS

        private const string STATUS_GETLOGGING_URL_POSTFIX = "config/public/logging/status";
        private const string STATUS_SETLOGGING_URL_POSTFIX_FORMAT = "config/admin/logging/{0}";

        /// <summary>
        /// Gets the message logging status.
        /// </summary>
        /// <returns>Message logging status, or MessageLoggingControlStatus.Error in case of communication problem.</returns>
        public async Task<MessageLoggingControlStatus> GetMessageLoggingStatusAsync()
        {
            _logger.Debug("WilmaService GetMessageLoggingStatusAsync enter...");

            using (var client = new HttpClient())
            {
                var resp = await client.GetAsync(GetUrl(STATUS_GETLOGGING_URL_POSTFIX));

                if (resp.IsSuccessStatusCode)
                {
                    var jsonStr = await resp.Content.ReadAsStringAsync();
                    _logger.Debug("WilmaService GetMessageLoggingStatusAsync success, with result: {0}", jsonStr);

                    var ls = JsonConvert.DeserializeObject<LoggingStatus>(jsonStr);

                    if (ls.RequestLogging && ls.ResponseLogging)
                    {
                        return MessageLoggingControlStatus.On;
                    }

                    return MessageLoggingControlStatus.Off;
                }

                _logger.Debug("WilmaService GetMessageLoggingStatusAsync failed: {0}", resp.StatusCode);
                return MessageLoggingControlStatus.Error;
            }
        }

        /// <summary>
        /// Turns on/off the message logging status.
        /// </summary>
        /// <param name="control">Control on/off</param>
        /// <returns>True if the request is successful, otherwise returns false.</returns>
        public async Task<bool> SetMessageLoggingStatusAsync(MessageLoggingControlStatus control)
        {
            _logger.Debug("WilmaService SetMessageLoggingStatusAsync enter with value: " + control);

            using (var client = new HttpClient())
            {
                var resp = await client.GetAsync(GetUrl(STATUS_SETLOGGING_URL_POSTFIX_FORMAT, control.ToString().ToLower()));

                if (resp.IsSuccessStatusCode)
                {
                    _logger.Debug("WilmaService SetMessageLoggingStatus success.");
                    return true;
                }

                _logger.Debug("WilmaService SetMessageLoggingStatus failed: {0}", resp.StatusCode);
                return false;
            }
        }

        #endregion LOGGIN STATUS

        #region OPERATION MODE

        private const string OPERATION_GETTER_URL_POSTFIX = "config/public/switch/status";
        private const string OPERATION_SETTER_URL_POSTFIX_FORMAT = "config/admin/switch/{0}";

        /// <summary>
        /// Gets the operation mode status.
        /// </summary>
        /// <returns>Operation mode status, or OperationModes.ERROR in case of communication problem.</returns>
        public async Task<OperationModes> GetOperationModeAsync()
        {
            _logger.Debug("WilmaService GetOperationMode enter...");

            using (var client = new HttpClient())
            {
                var resp = await client.GetAsync(GetUrl(OPERATION_GETTER_URL_POSTFIX));

                if (resp.IsSuccessStatusCode)
                {
                    var jsonStr = await resp.Content.ReadAsStringAsync();
                    _logger.Debug("WilmaService GetOperationMode success, with result: {0}", jsonStr);

                    var om = JsonConvert.DeserializeObject<OperationMode>(jsonStr);

                    if (om.ProxyMode)
                    {
                        return OperationModes.PROXY;
                    }
                    if (om.StubMode)
                    {
                        return OperationModes.STUB;
                    }
                    if (om.WilmaMode)
                    {
                        return OperationModes.WILMA;
                    }

                    return OperationModes.ERROR;
                }

                _logger.Debug("WilmaService GetOperationMode failed: {0}", resp.StatusCode);
                return OperationModes.ERROR;
            }
        }

        /// <summary>
        /// Switch the operation mode.
        /// </summary>
        /// <param name="modes">Mode: wilma/stub/proxy</param>
        /// <returns>True if the request is successful, otherwise returns false.</returns>
        public async Task<bool> SetOperationModeAsync(OperationModes modes)
        {
            _logger.Debug("WilmaService SetOperationMode enter with value: " + modes);

            using (var client = new HttpClient())
            {
                var resp = await client.GetAsync(GetUrl(OPERATION_SETTER_URL_POSTFIX_FORMAT, modes.ToString().ToLower()));

                if (resp.IsSuccessStatusCode)
                {
                    _logger.Debug("WilmaService SetOperationMode success.");
                    return true;
                }

                _logger.Debug("WilmaService SetOperationMode failed: {0}", resp.StatusCode);
                return false;
            }
        }

        #endregion OPERATION MODE

        #region LOCALHOST

        private const string STATUS_GETLOCALHOST_URL_POSTFIX = "config/public/localhost/status";
        private const string STATUS_SETLOCALHOST_URL_POSTFIX_FORMAT = "config/admin/localhost/{0}";

        /// <summary>
        /// Gets the localhost blocking status.
        /// </summary>
        /// <returns>Localhost blocking status if successful, otherwise LocalhostControlStatuses.Error.</returns>
        public async Task<LocalhostControlStatuses> GetLocalhostBlockingStatusAsync()
        {
            _logger.Debug("WilmaService GetLocalhostBlockingStatusAsync enter...");

            using (var client = new HttpClient())
            {
                var resp = await client.GetAsync(GetUrl(STATUS_GETLOCALHOST_URL_POSTFIX));

                if (resp.IsSuccessStatusCode)
                {
                    _logger.Debug("WilmaService GetLocalhostBlockingStatusAsync success.");

                    var jsonStr = await resp.Content.ReadAsStringAsync();
                    var lcs = JsonConvert.DeserializeObject<LocalhostControlStatus>(jsonStr);

                    if (lcs.LocalhostMode)
                    {
                        return LocalhostControlStatuses.On;
                    }

                    return LocalhostControlStatuses.Off;
                }

                _logger.Debug("WilmaService GetLocalhostBlockingStatusAsync failed: {0}", resp.StatusCode);
                return LocalhostControlStatuses.Error;
            }
        }

        /// <summary>
        /// Turns on/off the localhost blocking.
        /// </summary>
        /// <param name="control">Control on/off</param>
        /// <returns>True if the request is successful, otherwise returns false.</returns>
        public async Task<bool> SetLocalhostBlockingStatusAsync(LocalhostControlStatuses control)
        {
            _logger.Debug("WilmaService SetLocalhostBlockingStatusAsync enter with value: " + control);

            using (var client = new HttpClient())
            {
                var resp = await client.GetAsync(GetUrl(STATUS_SETLOCALHOST_URL_POSTFIX_FORMAT, control.ToString().ToLower()));

                if (resp.IsSuccessStatusCode)
                {
                    return true;
                }

                _logger.Debug("WilmaService GetLocalhostBlockingStatusAsync failed: {0}", resp.StatusCode);
                return false;
            }
        }

        #endregion LOCALHOST

        #region STUB RELATED

        private const string GET_STUB_INFO_URL_POSTFIX = "config/public/stubdescriptor";
        private const string CHANGE_STUB_CONFIG_STATUS_URL_POSTFIX = "config/admin/stub/changestatus?groupname={0}&nextstatus={1}";
        private const string CHANGE_STUB_CONFIG_ORDER_URL_POSTFIX = "config/admin/stub/changeorder?groupname={0}&direction={1}";
        private const string DROP_STUB_CONFIG_URL_POSTFIX = "config/admin/stub/drop?groupname={0}";
        private const string SAVE_STUB_CONFIG_URL = "config/admin/stub/save";

        /// <summary>
        /// Gets the stub configuration information.
        /// </summary>
        /// <returns>Stub configuration as JsonObject.</returns>
        public async Task<object> GetStubConfigInformationAsync()
        {
            _logger.Debug("WilmaService GetStubConfigInformationAsync enter...");

            using (var client = new HttpClient())
            {
                var resp = await client.GetAsync(GetUrl(GET_STUB_INFO_URL_POSTFIX));

                if (resp.IsSuccessStatusCode)
                {
                    var jsonStr = await resp.Content.ReadAsStringAsync();

                    _logger.Debug("WilmaService GetStubConfigInformationAsync success: {0}", jsonStr);

                    var res = JsonConvert.DeserializeObject(jsonStr);
                    return res;
                }

                _logger.Debug("WilmaService GetLocalhostBlockingStatusAsync failed: {0}", resp.StatusCode);
                return null;
            }
        }

        /// <summary>
        /// Enable/disable the given group.
        /// </summary>
        /// <param name="groupName">Name of the Group.</param>
        /// <param name="status">Status to set: Enabled/Disabled</param>
        /// <returns>True if succesful, otherwise returns false.</returns>
        public async Task<bool> ChangeStubConfigStatusAsync(string groupName, StubConfigStatus status)
        {
            _logger.Debug("WilmaService ChangeStubConfigStatusAsync to: {0} for: {1}", status, groupName);

            using (var client = new HttpClient())
            {
                var resp = await client.GetAsync(GetUrl(CHANGE_STUB_CONFIG_STATUS_URL_POSTFIX, groupName, status == StubConfigStatus.Enabled));

                if (resp.IsSuccessStatusCode)
                {
                    _logger.Debug("WilmaService ChangeStubConfigStatusAsync success.");
                    return true;
                }

                _logger.Debug("WilmaService ChangeStubConfigStatusAsync failed: {0}", resp.StatusCode);
                return false;
            }
        }

        /// <summary>
        ///  Sets new order for the given group, move up or down in the list.
        /// </summary>
        /// <param name="groupName">Name of Group to move.</param>
        /// <param name="order">Move direction: Up/Down</param>
        /// <returns>True if success, otherwise returns false.</returns>
        public async Task<bool> ChangeStubConfigOrderAsync(String groupName, StubConfigOrder order)
        {
            _logger.Debug("WilmaService ChangeStubConfigOrderAsync to: {0} for: {1}", order, groupName);

            using (var client = new HttpClient())
            {
                var resp = await client.GetAsync(GetUrl(CHANGE_STUB_CONFIG_ORDER_URL_POSTFIX, groupName, (int)order));

                if (resp.IsSuccessStatusCode)
                {
                    _logger.Debug("WilmaService ChangeStubConfigOrderAsync success.");
                    return true;
                }

                _logger.Debug("WilmaService ChangeStubConfigOrderAsync failed: {0}", resp.StatusCode);
                return false;
            }
        }

        /// <summary>
        /// Drops the given stub configuration.
        /// </summary>
        /// <param name="groupName">Name of Group to drop.</param>
        /// <returns>True if succes, otherwise returns false.</returns>
        public async Task<bool> DropStubConfigAsync(String groupName)
        {
            _logger.Debug("WilmaService DropStubConfigAsync: {0}", groupName);

            using (var client = new HttpClient())
            {
                var resp = await client.GetAsync(GetUrl(DROP_STUB_CONFIG_URL_POSTFIX, groupName));

                if (resp.IsSuccessStatusCode)
                {
                    _logger.Debug("WilmaService DropStubConfigAsync success.");
                    return true;
                }

                _logger.Debug("WilmaService DropStubConfigAsync failed: {0}", resp.StatusCode);
                return false;
            }
        }

        /// <summary>
        /// Persists the actual stub configuration.
        /// </summary>
        /// <returns>True if success, otherwise returns false.</returns>
        public async Task<bool> PersistActualStubConfigAsync()
        {
            _logger.Debug("WilmaService PersistActualStubConfigAsync.");

            using (var client = new HttpClient())
            {
                var resp = await client.GetAsync(GetUrl(SAVE_STUB_CONFIG_URL));

                if (resp.IsSuccessStatusCode)
                {
                    _logger.Debug("WilmaService PersistActualStubConfigAsync success.");
                    return true;
                }

                _logger.Debug("WilmaService PersistActualStubConfigAsync failed: {0}", resp.StatusCode);
                return false;
            }
        }

        #endregion STUB RELATED

        #region UPLOADS

        private const string CONDITION_CHECKER_UPLOAD_URL_POSTFIX = "config/admin/stub/conditionchecker?fileName={0}";
        private const string STUB_CONFIGURATION_UPLOAD_URL_POSTFIX = "config/admin/stub/templates?fileName={0}";
        private const string TEMPLATE_UPLOAD_URL_POSTFIX = "config/admin/stub/templateformatter?fileName={0}";
        private const string TEMPLATE_FORMATTER_UPLOAD_URL_POSTFIX = "config/admin/stub/stubconfig?fileName={0}";

        /// <summary>
        /// Uploads condition checker configuration.
        /// </summary>
        /// <param name="fileName">Name of file.</param>
        /// <param name="stream">FileStream to upload.</param>
        /// <returns>True if success, otherwise returns false.</returns>
        public async Task<bool> UploadConditionCheckerAsync(string fileName, Stream stream)
        {
            _logger.Debug("WilmaService UploadConditionChecker: {0}", fileName);

            using (var client = new HttpClient())
            {
                var resp = await client.PostAsync(GetUrl(CONDITION_CHECKER_UPLOAD_URL_POSTFIX, fileName), new StreamContent(stream));

                if (resp.IsSuccessStatusCode)
                {
                    _logger.Debug("WilmaService UploadConditionChecker success.");
                    return true;
                }

                _logger.Debug("WilmaService UploadConditionChecker failed: {0}", resp.StatusCode);
                return false;
            }
        }

        /// <summary>
        /// Uploads template.
        /// </summary>
        /// <param name="fileName">Name of file.</param>
        /// <param name="stream">FileStream to upload.</param>
        /// <returns>True if success, otherwise returns false.</returns>
        public async Task<bool> UploadTemplateAsync(string fileName, Stream stream)
        {
            _logger.Debug("WilmaService UploadTemplateAsync: {0}", fileName);

            using (var client = new HttpClient())
            {
                var resp = await client.PostAsync(GetUrl(TEMPLATE_UPLOAD_URL_POSTFIX, fileName), new StreamContent(stream));

                if (resp.IsSuccessStatusCode)
                {
                    _logger.Debug("WilmaService UploadTemplateAsync success.");
                    return true;
                }

                _logger.Debug("WilmaService UploadTemplateAsync failed: {0}", resp.StatusCode);
                return false;
            }
        }

        /// <summary>
        /// Uploads template formatter.
        /// </summary>
        /// <param name="fileName">Name of file.</param>
        /// <param name="stream">FileStream to upload.</param>
        /// <returns>True if success, otherwise returns false.</returns>
        public async Task<bool> UploadTemplateFormatterAsync(string fileName, Stream stream)
        {
            _logger.Debug("WilmaService UploadTemplateFormatterAsync: {0}", fileName);

            using (var client = new HttpClient())
            {
                var resp = await client.PostAsync(GetUrl(TEMPLATE_FORMATTER_UPLOAD_URL_POSTFIX, fileName), new StreamContent(stream));

                if (resp.IsSuccessStatusCode)
                {
                    _logger.Debug("WilmaService UploadTemplateFormatterAsync success.");
                    return true;
                }

                _logger.Debug("WilmaService UploadTemplateFormatterAsync failed: {0}", resp.StatusCode);
                return false;
            }
        }

        /// <summary>
        /// Uploads Stub Configuration.
        /// </summary>
        /// <param name="fileName">Name of file.</param>
        /// <param name="stream">FileStream to upload.</param>
        /// <returns>True if success, otherwise returns false.</returns>
        public async Task<bool> UploadStubConfigurationAsync(string fileName, Stream stream)
        {
            _logger.Debug("WilmaService UploadStubConfigurationAsync: {0}", fileName);

            using (var client = new HttpClient())
            {
                var resp = await client.PostAsync(GetUrl(STUB_CONFIGURATION_UPLOAD_URL_POSTFIX, fileName), new StreamContent(stream));

                if (resp.IsSuccessStatusCode)
                {
                    _logger.Debug("WilmaService UploadStubConfigurationAsync success.");
                    return true;
                }

                _logger.Debug("WilmaService UploadStubConfigurationAsync failed: {0}", resp.StatusCode);
                return false;
            }
        }

        #endregion UPLOADS
    }
}
