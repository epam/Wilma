using System;
using System.Diagnostics;
using System.Threading.Tasks;
using System.Net.Http;
using Newtonsoft.Json;
using System.Collections.Generic;

namespace epam.wilma_service_api
{
    public class WilmaService
    {
        #region ENUMS

        public enum MessageLoggingControlStatus
        {
            On,
            Off,
        }

        public enum OperationMode
        {
            ERROR,
            WILMA,
            STUB,
            PROXY
        }

        #endregion ENUMS

        #region PRIVATES

        private readonly WilmaServiceConfig _config;
        private string GetUrl(string postfix)
        {
            return string.Format("{0}:{1}/{2}", _config.Host, _config.Port, postfix);
        }
        private string GetUrl(string postfixFormat, params object[] prms)
        {
            return string.Format("{0}:{1}/{2}", _config.Host, _config.Port, string.Format(postfixFormat, prms));
        }

        #endregion PRIVATES

        #region URL_POSTFIXES

        private const string VERSION_INFO_URL_POSTFIX = "config/public/version";
        private const string ACTUAL_LOAD_INFO_URL_POSTFIX = "config/public/actualload";
        private const string SHUTDOWN_URL_POSTFIX = "config/admin/shutdown";

        private const string STATUS_GETTER_URL_POSTFIX = "config/public/logging/status";
        private const string STATUS_SETTER_URL_POSTFIX_FORMAT = "config/admin/logging/{0}";

        private const string OPERATION_GETTER_URL_POSTFIX = "config/public/switch/status";
        private const string OPERATION_SETTER_URL_POSTFIX_FORMAT = "config/admin/switch/{0}";

        #endregion URL_POSTFIXES

        public WilmaService(WilmaServiceConfig config)
        {
            Debug.WriteLine("WilmaService created.");

            if (config == null)
            {
                Debug.WriteLine("WilmaServiceConfig is NULL.");

                throw new ArgumentNullException();
            }

            _config = config;
        }

        #region PUBLIC METHODES

        public async Task<string> GetVersionInformationAsync()
        {
            Debug.WriteLine("WilmaService GetVersionInformationAsync enter...");

            using (var client = new HttpClient())
            {
                var resp = await client.GetAsync(GetUrl(VERSION_INFO_URL_POSTFIX));

                if (resp.IsSuccessStatusCode)
                {
                    Debug.WriteLine("WilmaService GetVersionInformationAsync success.");

                    return await resp.Content.ReadAsStringAsync();
                }

                Debug.WriteLine("WilmaService GetVersionInformationAsync failed: {0}", resp.StatusCode);
                return null;
            }
        }

        public async Task<WilmaLoadInformation> GetActualLoadInformationAsync()
        {
            Debug.WriteLine("WilmaService GetActualLoadInformation enter...");

            using (var client = new HttpClient())
            {
                var resp = await client.GetAsync(GetUrl(ACTUAL_LOAD_INFO_URL_POSTFIX));

                if (resp.IsSuccessStatusCode)
                {
                    var jsonStr = await resp.Content.ReadAsStringAsync();
                    Debug.WriteLine("WilmaService GetActualLoadInformation success, with result: {0}", jsonStr);

                    return JsonConvert.DeserializeObject<WilmaLoadInformation>(jsonStr);
                }

                Debug.WriteLine("WilmaService GetActualLoadInformation failed: {0}", resp.StatusCode);
                return null;
            }
        }

        public async Task<bool> ShutdownApplicationAsync()
        {
            Debug.WriteLine("WilmaService ShutdownApplication enter...");

            using (var client = new HttpClient())
            {
                var resp = await client.GetAsync(GetUrl(SHUTDOWN_URL_POSTFIX));

                if (resp.IsSuccessStatusCode)
                {
                    Debug.WriteLine("WilmaService ShutdownApplication success.");
                    return true;
                }

                Debug.WriteLine("WilmaService ShutdownApplication failed: {0}", resp.StatusCode);
                return false;
            }
        }

        public async Task<MessageLoggingControlStatus> GetMessageLoggingStatusAsync()
        {
            Debug.WriteLine("WilmaService GetMessageLoggingStatusAsync enter...");

            using (var client = new HttpClient())
            {
                var resp = await client.GetAsync(GetUrl(STATUS_GETTER_URL_POSTFIX));

                if (resp.IsSuccessStatusCode)
                {
                    var jsonStr = await resp.Content.ReadAsStringAsync();
                    Debug.WriteLine("WilmaService GetMessageLoggingStatusAsync success, with result: {0}", jsonStr);

                    var dic = JsonConvert.DeserializeObject<Dictionary<string, bool>>(jsonStr);

                    if (dic["requestLogging"] && dic["responseLogging"])
                    {
                        return MessageLoggingControlStatus.On;
                    }

                    return MessageLoggingControlStatus.Off;
                }

                Debug.WriteLine("WilmaService GetMessageLoggingStatusAsync failed: {0}", resp.StatusCode);
                return MessageLoggingControlStatus.Off;
            }
        }

        public async Task<bool> SetMessageLoggingStatusAsync(MessageLoggingControlStatus control)
        {
            Debug.WriteLine("WilmaService GetMessageLoggingStatusAsync enter with value: " + control);

            using (var client = new HttpClient())
            {
                var resp = await client.GetAsync(GetUrl(STATUS_SETTER_URL_POSTFIX_FORMAT, control.ToString().ToLower()));

                if (resp.IsSuccessStatusCode)
                {
                    Debug.WriteLine("WilmaService SetMessageLoggingStatus success.");
                    return true;
                }

                Debug.WriteLine("WilmaService SetMessageLoggingStatus failed: {0}", resp.StatusCode);
                return false;
            }
        }

        public async Task<OperationMode> GetOperationModeAsync()
        {
            Debug.WriteLine("WilmaService GetOperationMode enter...");

            using (var client = new HttpClient())
            {
                var resp = await client.GetAsync(GetUrl(OPERATION_GETTER_URL_POSTFIX));

                if (resp.IsSuccessStatusCode)
                {
                    var jsonStr = await resp.Content.ReadAsStringAsync();
                    Debug.WriteLine("WilmaService GetOperationMode success, with result: {0}", jsonStr);

                    var dic = JsonConvert.DeserializeObject<Dictionary<string, bool>>(jsonStr);

                    bool proxyMode = dic["proxyMode"];
                    bool stubMode = dic["stubMode"];
                    bool wilmaMode = dic["wilmaMode"];

                    if (proxyMode)
                    {
                        return OperationMode.PROXY;
                    }
                    if (stubMode)
                    {
                        return OperationMode.STUB;
                    }
                    if (wilmaMode)
                    {
                        return OperationMode.WILMA;
                    }

                    return OperationMode.ERROR;
                }

                Debug.WriteLine("WilmaService GetOperationMode failed: {0}", resp.StatusCode);
                return OperationMode.ERROR;
            }
        }

        public async Task<bool> SetOperationModeAsync(OperationMode mode)
        {
            Debug.WriteLine("WilmaService SetOperationMode enter with value: " + mode);

            using (var client = new HttpClient())
            {
                var resp = await client.GetAsync(GetUrl(OPERATION_SETTER_URL_POSTFIX_FORMAT, mode.ToString().ToLower()));

                if (resp.IsSuccessStatusCode)
                {
                    Debug.WriteLine("WilmaService SetOperationMode success.");
                    return true;
                }

                Debug.WriteLine("WilmaService SetOperationMode failed: {0}", resp.StatusCode);
                return false;
            }
        }

        #endregion PUBLIC METHODES
    }
}
