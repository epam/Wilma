﻿/*==========================================================================
 Copyright 2016 EPAM Systems

 This file is part of Wilma.

 Wilma is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Wilma is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Wilma.  If not, see <http://www.gnu.org/licenses/>.
 ===========================================================================*/

using System;
using System.Threading.Tasks;
using System.Net.Http;
using Newtonsoft.Json;
using System.IO;
using epam.wilma_service_api.ServiceCommClasses;

namespace epam.wilma_service_api
{
    /// <summary>
    /// WilmaService class to acccess and manipulate programmatically WilmaApp.
    /// </summary>
    public class WilmaService
    {
        #region ENUMS

        /// <summary>
        /// MessageLoggingStatusEnum
        /// </summary>
        public enum MessageLoggingStatusEnum
        {
            /// <summary>
            /// Communication error.
            /// </summary>
            Error,
            /// <summary>
            /// On.
            /// </summary>
            On,
            /// <summary>
            /// Off.
            /// </summary>
            Off,
        }

        /// <summary>
        /// OperationModeEnum
        /// </summary>
        public enum OperationModeEnum
        {
            /// <summary>
            /// Communication error.
            /// </summary>
            ERROR,
            /// <summary>
            /// Wilma.
            /// </summary>
            WILMA,
            /// <summary>
            /// Stub.
            /// </summary>
            STUB,
            /// <summary>
            /// Proxy.
            /// </summary>
            PROXY
        }

        /// <summary>
        /// LocalhostControlStatuses
        /// </summary>
        public enum LocalhostControlStatusEnum
        {
            /// <summary>
            /// Communication error.
            /// </summary>
            Error,
            /// <summary>
            /// On.
            /// </summary>
            On,
            /// <summary>
            /// Off.
            /// </summary>
            Off
        }

        /// <summary>
        /// MessageMarkingStatusEnum
        /// </summary>
        public enum MessageMarkingStatusEnum
        {
            /// <summary>
            /// Communication error.
            /// </summary>
            Error,
            /// <summary>
            /// On.
            /// </summary>
            On,
            /// <summary>
            /// Off.
            /// </summary>
            Off
        }

        /// <summary>
        /// ResponseVolatilityStatusEnum
        /// </summary>
        public enum ResponseVolatilityStatusEnum
        {
            /// <summary>
            /// Communication error.
            /// </summary>
            Error,
            /// <summary>
            /// On.
            /// </summary>
            On,
            /// <summary>
            /// Off.
            /// </summary>
            Off
        }
        
        /// <summary>
        /// StubConfigStatusEnum
        /// </summary>
        public enum StubConfigStatusEnum
        {
            /// <summary>
            /// Enabled.
            /// </summary>
            Enabled,
            /// <summary>
            /// Disabled.
            /// </summary>
            Disabled
        }

        /// <summary>
        /// StubConfigOrderEnum
        /// </summary>
        public enum StubConfigOrderEnum
        {
            /// <summary>
            /// Up.
            /// </summary>
            Up = 1,
            /// <summary>
            /// Down.
            /// </summary>
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

        private readonly ILogger _logger;

        private readonly HttpClient _httpClient;

        #endregion PRIVATES

        /// <summary>
        /// WilmaService constructor.
        /// </summary>
        /// <param name="config">WilmaServiceConfig stores host and port.</param>
        /// <param name="logger">Object implements ILogger interface, this methods are called for logging.</param>
        /// <param name="httpClient">HttpClient.</param>
        /// <exception cref="System.ArgumentNullException">Thrown when config or logger is null.</exception>
        public WilmaService(WilmaServiceConfig config, ILogger logger, HttpClient httpClient)
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
            _httpClient = httpClient;
        }

        /// <summary>
        /// WilmaService constructor.
        /// </summary>
        /// <param name="config">WilmaServiceConfig stores host and port.</param>
        /// <param name="logger">Object implements ILogger interface, this methods are called for logging.</param>
        /// <exception cref="System.ArgumentNullException">Thrown when config or logger is null.</exception>
        public WilmaService(WilmaServiceConfig config, ILogger logger)
            : this(config, logger, new HttpClient())
        {
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

            var resp = await _httpClient.GetAsync(GetUrl(VERSION_INFO_URL_POSTFIX));
            if (resp.IsSuccessStatusCode)
            {
                _logger.Debug("WilmaService GetVersionInformationAsync success.");
                return await resp.Content.ReadAsStringAsync();
            }

            _logger.Debug("WilmaService GetVersionInformationAsync failed: {0}", resp.StatusCode);
            return null;
        }

        /// <summary>
        /// Gets the actual load information of the application.
        /// </summary>
        /// <returns>Actual load information, or null in case of communication problem.</returns>
        public async Task<LoadInformation> GetActualLoadInformationAsync()
        {
            _logger.Debug("WilmaService GetActualLoadInformation enter...");

            var resp = await _httpClient.GetAsync(GetUrl(ACTUAL_LOAD_INFO_URL_POSTFIX));
            if (resp.IsSuccessStatusCode)
            {
                var jsonStr = await resp.Content.ReadAsStringAsync();
                _logger.Debug("WilmaService GetActualLoadInformation success, with result: {0}", jsonStr);
                return JsonConvert.DeserializeObject<LoadInformation>(jsonStr);
            }

            _logger.Debug("WilmaService GetActualLoadInformation failed: {0}", resp.StatusCode);
            return null;
        }

        /// <summary>
        /// Shutdown the Wilma application.
        /// </summary>
        /// <returns>True if the request is successful, otherwise returns false</returns>
        public async Task<bool> ShutdownApplicationAsync()
        {
            _logger.Debug("WilmaService ShutdownApplication enter...");

            var resp = await _httpClient.GetAsync(GetUrl(SHUTDOWN_URL_POSTFIX));
            if (resp.IsSuccessStatusCode)
            {
                _logger.Debug("WilmaService ShutdownApplication success.");
                return true;
            }

            _logger.Debug("WilmaService ShutdownApplication failed: {0}", resp.StatusCode);
            return false;
        }

        #endregion APP RELATED

        #region LOGGING STATUS

        private const string STATUS_GETLOGGING_URL_POSTFIX = "config/public/logging/status";
        private const string STATUS_SETLOGGING_URL_POSTFIX_FORMAT = "config/admin/logging/{0}";

        /// <summary>
        /// Gets the message logging status.
        /// </summary>
        /// <returns>Message logging status, or MessageLoggingControlStatusEnum.Error in case of communication problem.</returns>
        public async Task<MessageLoggingStatusEnum> GetMessageLoggingStatusAsync()
        {
            _logger.Debug("WilmaService GetMessageLoggingStatusAsync enter...");

            var resp = await _httpClient.GetAsync(GetUrl(STATUS_GETLOGGING_URL_POSTFIX));
            if (resp.IsSuccessStatusCode)
            {
                var jsonStr = await resp.Content.ReadAsStringAsync();
                _logger.Debug("WilmaService GetMessageLoggingStatusAsync success, with result: {0}", jsonStr);

                var ls = JsonConvert.DeserializeObject<LoggingStatus>(jsonStr);
                if (ls.RequestLogging && ls.ResponseLogging)
                {
                    return MessageLoggingStatusEnum.On;
                }
                return MessageLoggingStatusEnum.Off;
            }

            _logger.Debug("WilmaService GetMessageLoggingStatusAsync failed: {0}", resp.StatusCode);
            return MessageLoggingStatusEnum.Error;
        }

        /// <summary>
        /// Turns on/off the message logging status.
        /// </summary>
        /// <param name="control">Control on/off</param>
        /// <returns>True if the request is successful, otherwise returns false.</returns>
        public async Task<bool> SetMessageLoggingStatusAsync(MessageLoggingStatusEnum control)
        {
            _logger.Debug("WilmaService SetMessageLoggingStatusAsync enter with value: " + control);

            var resp = await _httpClient.GetAsync(GetUrl(STATUS_SETLOGGING_URL_POSTFIX_FORMAT, control.ToString().ToLower()));
            if (resp.IsSuccessStatusCode)
            {
                _logger.Debug("WilmaService SetMessageLoggingStatus success.");
                return true;
            }

            _logger.Debug("WilmaService SetMessageLoggingStatus failed: {0}", resp.StatusCode);
            return false;
        }

        #endregion LOGGIN STATUS

        #region MESSAGEMARKING STATUS

        private const string STATUS_GETMESSAGEMARKING_URL_POSTFIX = "config/public/messagemarking/status";
        private const string STATUS_SETMESSAGEMARKING_URL_POSTFIX_FORMAT = "config/admin/messagemarking/{0}";

        /// <summary>
        /// Gets the message marking status.
        /// </summary>
        /// <returns>Message marking status, or MessageMarkingStatusEnum.Error in case of communication problem.</returns>
        public async Task<MessageMarkingStatusEnum> GetMessageMarkingStatusAsync()
        {
            _logger.Debug("WilmaService GetMessageMarkingAsync enter...");

            var resp = await _httpClient.GetAsync(GetUrl(STATUS_GETMESSAGEMARKING_URL_POSTFIX));
            if (resp.IsSuccessStatusCode)
            {
                var jsonStr = await resp.Content.ReadAsStringAsync();
                _logger.Debug("WilmaService GetMessageMarkingAsync success, with result: {0}", jsonStr);

                var ls = JsonConvert.DeserializeObject<MessageMarkingStatus>(jsonStr);
                if (ls.MessageMarking)
                {
                    return MessageMarkingStatusEnum.On;
                }
                return MessageMarkingStatusEnum.Off;
            }

            _logger.Debug("WilmaService GetMessageMarking failed: {0}", resp.StatusCode);
            return MessageMarkingStatusEnum.Error;
        }

        /// <summary>
        /// Turns on/off the message marking status.
        /// </summary>
        /// <param name="control">Control on/off</param>
        /// <returns>True if the request is successful, otherwise returns false.</returns>
        public async Task<bool> SetMessageMarkingStatusAsync(MessageMarkingStatusEnum control)
        {
            _logger.Debug("WilmaService SetMessageMarkingStatusAsync enter with value: " + control);

            var resp = await _httpClient.GetAsync(GetUrl(STATUS_SETMESSAGEMARKING_URL_POSTFIX_FORMAT, control.ToString().ToLower()));
            if (resp.IsSuccessStatusCode)
            {
                _logger.Debug("WilmaService SetMessageMarkingStatus success.");
                return true;
            }

            _logger.Debug("WilmaService SetMessageMarkingStatus failed: {0}", resp.StatusCode);
            return false;
        }

        #endregion MESSAGEMARKING STATUS

        #region RESPONSE VOLATILITY

        private const string STATUS_GETRESPONSE_VOLATILITY_URL_POSTFIX = "config/public/responsevolatility/status";
        private const string STATUS_SETRESPONSE_VOLATILITY_URL_POSTFIX_FORMAT = "config/admin/responsevolatility/{0}";

        /// <summary>
        /// Gets the response volatility status.
        /// </summary>
        /// <returns>Response volatility status, or ResponseVolatilityStatusEnum.Error in case of communication problem.</returns>
        public async Task<ResponseVolatilityStatusEnum> GetResponseVolatilityStatusAsync()
        {
            _logger.Debug("WilmaService GetResponseVolatilityStatusAsync enter...");

            var resp = await _httpClient.GetAsync(GetUrl(STATUS_GETRESPONSE_VOLATILITY_URL_POSTFIX));
            if (resp.IsSuccessStatusCode)
            {
                var jsonStr = await resp.Content.ReadAsStringAsync();
                _logger.Debug("WilmaService GetResponseVolatilityStatusAsync success, with result: {0}", jsonStr);

                var ls = JsonConvert.DeserializeObject<ResponseVolatilityStatus>(jsonStr);
                if (ls.ResponseVolatility)
                {
                    return ResponseVolatilityStatusEnum.On;
                }
                return ResponseVolatilityStatusEnum.Off;
            }

            _logger.Debug("WilmaService GetResponseVolatilityStatusAsync failed: {0}", resp.StatusCode);
            return ResponseVolatilityStatusEnum.Error;
        }

        /// <summary>
        /// Turns on/off the response volatility status.
        /// </summary>
        /// <param name="control">Control on/off</param>
        /// <returns>True if the request is successful, otherwise returns false.</returns>
        public async Task<bool> SetResponseVolatilityStatusAsync(ResponseVolatilityStatusEnum control)
        {
            _logger.Debug("WilmaService SetResponseVolatilityStatusAsync enter with value: " + control);

            var resp = await _httpClient.GetAsync(GetUrl(STATUS_SETRESPONSE_VOLATILITY_URL_POSTFIX_FORMAT, control.ToString().ToLower()));
            if (resp.IsSuccessStatusCode)
            {
                _logger.Debug("WilmaService SetResponseVolatilityStatusAsync success.");
                return true;
            }

            _logger.Debug("WilmaService SetResponseVolatilityStatusAsync failed: {0}", resp.StatusCode);
            return false;
        }

        #endregion RESPONSE VOLATILITY

        #region OPERATION MODE

        private const string OPERATION_GETTER_URL_POSTFIX = "config/public/switch/status";
        private const string OPERATION_SETTER_URL_POSTFIX_FORMAT = "config/admin/switch/{0}";

        /// <summary>
        /// Gets the operation mode status.
        /// </summary>
        /// <returns>Operation mode status, or OperationModesEnum.ERROR in case of communication problem.</returns>
        public async Task<OperationModeEnum> GetOperationModeAsync()
        {
            _logger.Debug("WilmaService GetOperationMode enter...");

            var resp = await _httpClient.GetAsync(GetUrl(OPERATION_GETTER_URL_POSTFIX));
            if (resp.IsSuccessStatusCode)
            {
                var jsonStr = await resp.Content.ReadAsStringAsync();
                _logger.Debug("WilmaService GetOperationMode success, with result: {0}", jsonStr);

                var om = JsonConvert.DeserializeObject<OperationMode>(jsonStr);
                if (om.ProxyMode)
                {
                    return OperationModeEnum.PROXY;
                }
                if (om.StubMode)
                {
                    return OperationModeEnum.STUB;
                }
                if (om.WilmaMode)
                {
                    return OperationModeEnum.WILMA;
                }
                return OperationModeEnum.ERROR;
            }

            _logger.Debug("WilmaService GetOperationMode failed: {0}", resp.StatusCode);
            return OperationModeEnum.ERROR;
        }

        /// <summary>
        /// Switch the operation mode.
        /// </summary>
        /// <param name="modes">Mode: wilma/stub/proxy</param>
        /// <returns>True if the request is successful, otherwise returns false.</returns>
        public async Task<bool> SetOperationModeAsync(OperationModeEnum modes)
        {
            _logger.Debug("WilmaService SetOperationMode enter with value: " + modes);

            var resp = await _httpClient.GetAsync(GetUrl(OPERATION_SETTER_URL_POSTFIX_FORMAT, modes.ToString().ToLower()));
            if (resp.IsSuccessStatusCode)
            {
                _logger.Debug("WilmaService SetOperationMode success.");
                return true;
            }

            _logger.Debug("WilmaService SetOperationMode failed: {0}", resp.StatusCode);
            return false;
        }

        #endregion OPERATION MODE

        #region LOCALHOST

        private const string STATUS_GETLOCALHOST_URL_POSTFIX = "config/public/localhost/status";
        private const string STATUS_SETLOCALHOST_URL_POSTFIX_FORMAT = "config/admin/localhost/{0}";

        /// <summary>
        /// Gets the localhost blocking status.
        /// </summary>
        /// <returns>Localhost blocking status if successful, otherwise LocalhostControlStatuses.Error.</returns>
        public async Task<LocalhostControlStatusEnum> GetLocalhostBlockingStatusAsync()
        {
            _logger.Debug("WilmaService GetLocalhostBlockingStatusAsync enter...");

            var resp = await _httpClient.GetAsync(GetUrl(STATUS_GETLOCALHOST_URL_POSTFIX));
            if (resp.IsSuccessStatusCode)
            {
                _logger.Debug("WilmaService GetLocalhostBlockingStatusAsync success.");

                var jsonStr = await resp.Content.ReadAsStringAsync();
                var lcs = JsonConvert.DeserializeObject<LocalhostControlStatus>(jsonStr);
                if (lcs.LocalhostMode)
                {
                    return LocalhostControlStatusEnum.On;
                }
                return LocalhostControlStatusEnum.Off;
            }

            _logger.Debug("WilmaService GetLocalhostBlockingStatusAsync failed: {0}", resp.StatusCode);
            return LocalhostControlStatusEnum.Error;
        }

        /// <summary>
        /// Turns on/off the localhost blocking.
        /// </summary>
        /// <param name="control">Control on/off</param>
        /// <returns>True if the request is successful, otherwise returns false.</returns>
        public async Task<bool> SetLocalhostBlockingStatusAsync(LocalhostControlStatusEnum control)
        {
            _logger.Debug("WilmaService SetLocalhostBlockingStatusAsync enter with value: " + control);

            var resp = await _httpClient.GetAsync(GetUrl(STATUS_SETLOCALHOST_URL_POSTFIX_FORMAT, control.ToString().ToLower()));
            if (resp.IsSuccessStatusCode)
            {
                return true;
            }

            _logger.Debug("WilmaService GetLocalhostBlockingStatusAsync failed: {0}", resp.StatusCode);
            return false;
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

            var resp = await _httpClient.GetAsync(GetUrl(GET_STUB_INFO_URL_POSTFIX));
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

        /// <summary>
        /// Enable/disable the given group.
        /// </summary>
        /// <param name="groupName">Name of the Group.</param>
        /// <param name="status">Status to set: Enabled/Disabled</param>
        /// <returns>True if succesful, otherwise returns false.</returns>
        public async Task<bool> ChangeStubConfigStatusAsync(string groupName, StubConfigStatusEnum status)
        {
            _logger.Debug("WilmaService ChangeStubConfigStatusAsync to: {0} for: {1}", status, groupName);

            var resp = await _httpClient.GetAsync(GetUrl(CHANGE_STUB_CONFIG_STATUS_URL_POSTFIX, groupName, status == StubConfigStatusEnum.Enabled));
            if (resp.IsSuccessStatusCode)
            {
                _logger.Debug("WilmaService ChangeStubConfigStatusAsync success.");
                return true;
            }

            _logger.Debug("WilmaService ChangeStubConfigStatusAsync failed: {0}", resp.StatusCode);
            return false;
        }

        /// <summary>
        ///  Sets new order for the given group, move up or down in the list.
        /// </summary>
        /// <param name="groupName">Name of Group to move.</param>
        /// <param name="order">Move direction: Up/Down</param>
        /// <returns>True if success, otherwise returns false.</returns>
        public async Task<bool> ChangeStubConfigOrderAsync(String groupName, StubConfigOrderEnum order)
        {
            _logger.Debug("WilmaService ChangeStubConfigOrderAsync to: {0} for: {1}", order, groupName);

            var resp = await _httpClient.GetAsync(GetUrl(CHANGE_STUB_CONFIG_ORDER_URL_POSTFIX, groupName, (int)order));
            if (resp.IsSuccessStatusCode)
            {
                _logger.Debug("WilmaService ChangeStubConfigOrderAsync success.");
                return true;
            }

            _logger.Debug("WilmaService ChangeStubConfigOrderAsync failed: {0}", resp.StatusCode);
            return false;
        }

        /// <summary>
        /// Drops the given stub configuration.
        /// </summary>
        /// <param name="groupName">Name of Group to drop.</param>
        /// <returns>True if succes, otherwise returns false.</returns>
        public async Task<bool> DropStubConfigAsync(String groupName)
        {
            _logger.Debug("WilmaService DropStubConfigAsync: {0}", groupName);

            var resp = await _httpClient.GetAsync(GetUrl(DROP_STUB_CONFIG_URL_POSTFIX, groupName));
            if (resp.IsSuccessStatusCode)
            {
                _logger.Debug("WilmaService DropStubConfigAsync success.");
                return true;
            }

            _logger.Debug("WilmaService DropStubConfigAsync failed: {0}", resp.StatusCode);
            return false;
        }

        /// <summary>
        /// Persists the actual stub configuration.
        /// </summary>
        /// <returns>True if success, otherwise returns false.</returns>
        public async Task<bool> PersistActualStubConfigAsync()
        {
            _logger.Debug("WilmaService PersistActualStubConfigAsync.");

            var resp = await _httpClient.GetAsync(GetUrl(SAVE_STUB_CONFIG_URL));
            if (resp.IsSuccessStatusCode)
            {
                _logger.Debug("WilmaService PersistActualStubConfigAsync success.");
                return true;
            }

            _logger.Debug("WilmaService PersistActualStubConfigAsync failed: {0}", resp.StatusCode);
            return false;
        }

        #endregion STUB RELATED

        #region UPLOADS

        private const string CONDITION_CHECKER_UPLOAD_URL_POSTFIX = "config/admin/stub/conditionchecker?fileName={0}";
        private const string TEMPLATE_UPLOAD_URL_POSTFIX = "config/admin/stub/template?fileName={0}";
        private const string RESPONSE_FORMATTER_UPLOAD_URL_POSTFIX = "config/admin/stub/responseformatter?fileName={0}";
        private const string STUB_CONFIGURATION_UPLOAD_URL_POSTFIX = "config/admin/stub/stubconfig?fileName={0}";

        /// <summary>
        /// Uploads condition checker configuration.
        /// </summary>
        /// <param name="fileName">Name of file.</param>
        /// <param name="stream">FileStream to upload.</param>
        /// <returns>True if success, otherwise returns false.</returns>
        public async Task<bool> UploadConditionCheckerAsync(string fileName, Stream stream)
        {
            _logger.Debug("WilmaService UploadConditionChecker: {0}", fileName);

            var resp = await _httpClient.PostAsync(GetUrl(CONDITION_CHECKER_UPLOAD_URL_POSTFIX, fileName), new StreamContent(stream));
            if (resp.IsSuccessStatusCode)
            {
                _logger.Debug("WilmaService UploadConditionChecker success.");
                return true;
            }

            _logger.Debug("WilmaService UploadConditionChecker failed: {0}", resp.StatusCode);
            return false;
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

            var resp = await _httpClient.PostAsync(GetUrl(TEMPLATE_UPLOAD_URL_POSTFIX, fileName), new StreamContent(stream));
            if (resp.IsSuccessStatusCode)
            {
                _logger.Debug("WilmaService UploadTemplateAsync success.");
                return true;
            }

            _logger.Debug("WilmaService UploadTemplateAsync failed: {0}", resp.StatusCode);
            return false;
        }

        /// <summary>
        /// Uploads response formatter.
        /// </summary>
        /// <param name="fileName">Name of file.</param>
        /// <param name="stream">FileStream to upload.</param>
        /// <returns>True if success, otherwise returns false.</returns>
        public async Task<bool> UploadResponseFormatterAsync(string fileName, Stream stream)
        {
            _logger.Debug("WilmaService UploadResponseFormatterAsync: {0}", fileName);

            var resp = await _httpClient.PostAsync(GetUrl(RESPONSE_FORMATTER_UPLOAD_URL_POSTFIX, fileName), new StreamContent(stream));
            if (resp.IsSuccessStatusCode)
            {
                _logger.Debug("WilmaService UploadResponseFormatterAsync success.");
                return true;
            }

            _logger.Debug("WilmaService UploadResponseFormatterAsync failed: {0}", resp.StatusCode);
            return false;
        }

        /// <summary>
        /// Uploads Stub Configuration.
        /// </summary>
        /// <param name="fileName">Name of file.</param>
        /// <param name="str">Configuration in string format to upload.</param>
        /// <returns>True if success, otherwise returns false.</returns>
        public async Task<bool> UploadStubConfigurationAsync(string fileName, String str)
        {
            _logger.Debug("WilmaService UploadStubConfigurationAsync: {0}", fileName);
          
            
            var resp = await _httpClient.PostAsync(GetUrl(STUB_CONFIGURATION_UPLOAD_URL_POSTFIX, fileName), new StringContent(str, System.Text.Encoding.UTF8, "application/json"));
            if (resp.IsSuccessStatusCode)
            {
                _logger.Debug("WilmaService UploadStubConfigurationAsync success.");
                return true;
            }

            _logger.Debug("WilmaService UploadStubConfigurationAsync failed: {0}", resp.StatusCode);
            return false;
        }

        #endregion UPLOADS
    }
}
