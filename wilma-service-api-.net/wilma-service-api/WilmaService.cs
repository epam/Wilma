using System;
using System.Diagnostics;
using System.Threading.Tasks;
using System.Net.Http;
using Newtonsoft.Json;
using System.Collections.Generic;
using epam.wilma_service_api.ServiceCommClasses;

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

        public enum LocalhostControlStatus
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

        private const string STATUS_GETLOGGING_URL_POSTFIX = "config/public/logging/status";
        private const string STATUS_SETLOGGING_URL_POSTFIX_FORMAT = "config/admin/logging/{0}";

        private const string STATUS_GETLOCALHOST_URL_POSTFIX = "config/public/localhost/status";
        private const string STATUS_SETLOCALHOST_URL_POSTFIX_FORMAT = "config/admin/localhost/{0}";

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

        public async Task<LoadInformation> GetActualLoadInformationAsync()
        {
            Debug.WriteLine("WilmaService GetActualLoadInformation enter...");

            using (var client = new HttpClient())
            {
                var resp = await client.GetAsync(GetUrl(ACTUAL_LOAD_INFO_URL_POSTFIX));

                if (resp.IsSuccessStatusCode)
                {
                    var jsonStr = await resp.Content.ReadAsStringAsync();
                    Debug.WriteLine("WilmaService GetActualLoadInformation success, with result: {0}", jsonStr);

                    return JsonConvert.DeserializeObject<LoadInformation>(jsonStr);
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
                var resp = await client.GetAsync(GetUrl(STATUS_GETLOGGING_URL_POSTFIX));

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
                var resp = await client.GetAsync(GetUrl(STATUS_SETLOGGING_URL_POSTFIX_FORMAT, control.ToString().ToLower()));

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

        public async Task<LocalhostControlStatus> GetLocalhostBlockingStatusAsync()
        {
            Debug.WriteLine("WilmaService GetLocalhostBlockingStatusAsync enter...");

            using (var client = new HttpClient())
            {
                var resp = await client.GetAsync(GetUrl(STATUS_GETLOCALHOST_URL_POSTFIX));

                if (resp.IsSuccessStatusCode)
                {
                    Debug.WriteLine("WilmaService GetLocalhostBlockingStatusAsync success.");

                    var jsonStr = await resp.Content.ReadAsStringAsync();
                    var dic = JsonConvert.DeserializeObject<Dictionary<string, bool>>(jsonStr);

                    bool localhostMode = dic["localhostMode"];

                    if (localhostMode)
                    {
                        return LocalhostControlStatus.On;
                    }

                    return LocalhostControlStatus.Off;
                }

                Debug.WriteLine("WilmaService GetLocalhostBlockingStatusAsync failed: {0}", resp.StatusCode);
                return LocalhostControlStatus.Error;
            }
        }

        public async Task<bool> SetLocalhostBlockingStatusAsync(LocalhostControlStatus control)
        {
            Debug.WriteLine("WilmaService SetLocalhostBlockingStatusAsync enter with value: " + control);

            using (var client = new HttpClient())
            {
                var resp = await client.GetAsync(GetUrl(STATUS_SETLOCALHOST_URL_POSTFIX_FORMAT, control.ToString().ToLower()));

                if (resp.IsSuccessStatusCode)
                {
                    return true;
                }

                Debug.WriteLine("WilmaService GetLocalhostBlockingStatusAsync failed: {0}", resp.StatusCode);
                return false;
            }
        }


        private const string GET_STUB_INFO_URL_POSTFIX = "config/public/stubdescriptor";
        private const string CHANGE_STUB_CONFIG_STATUS_URL_POSTFIX = "config/admin/stub/changestatus?groupname={0}&nextstatus={1}";
        private const string CHANGE_STUB_CONFIG_ORDER_URL_POSTFIX = "config/admin/stub/changeorder?groupname={0}&direction={1}";
        private const string DROP_STUB_CONFIG_URL_POSTFIX = "config/admin/stub/drop?groupname={0}";
        private const string SAVE_STUB_CONFIG_URL = "config/admin/stub/save";

        public async Task<object> GetStubConfigInformationAsync()
        {
            Debug.WriteLine("WilmaService GetStubConfigInformationAsync enter...");

            using (var client = new HttpClient())
            {
                var resp = await client.GetAsync(GetUrl(GET_STUB_INFO_URL_POSTFIX));

                if (resp.IsSuccessStatusCode)
                {
                    var jsonStr = await resp.Content.ReadAsStringAsync();

                    Debug.WriteLine("WilmaService GetStubConfigInformationAsync success: {0}", jsonStr);

                    var res = JsonConvert.DeserializeObject(jsonStr);
                    return res;
                }

                Debug.WriteLine("WilmaService GetLocalhostBlockingStatusAsync failed: {0}", resp.StatusCode);
                return null;
            }
        }

        public async Task<bool> ChangeStubConfigStatusAsync(string groupName, StubConfigStatus status)
        {
            Debug.WriteLine("WilmaService ChangeStubConfigStatusAsync to: {0} for: {1}", status, groupName);

            using (var client = new HttpClient())
            {
                var resp = await client.GetAsync(GetUrl(CHANGE_STUB_CONFIG_STATUS_URL_POSTFIX, groupName, status == StubConfigStatus.Enabled));

                if (resp.IsSuccessStatusCode)
                {
                    Debug.WriteLine("WilmaService ChangeStubConfigStatusAsync success.");
                    return true;
                }

                Debug.WriteLine("WilmaService ChangeStubConfigStatusAsync failed: {0}", resp.StatusCode);
                return false;
            }
        }

        public async Task<bool> ChangeStubConfigOrderAsync(String groupName, StubConfigOrder order)
        {
            Debug.WriteLine("WilmaService ChangeStubConfigOrderAsync to: {0} for: {1}", order, groupName);

            using (var client = new HttpClient())
            {
                var resp = await client.GetAsync(GetUrl(CHANGE_STUB_CONFIG_ORDER_URL_POSTFIX, groupName, (int)order));

                if (resp.IsSuccessStatusCode)
                {
                    Debug.WriteLine("WilmaService ChangeStubConfigOrderAsync success.");
                    return true;
                }

                Debug.WriteLine("WilmaService ChangeStubConfigOrderAsync failed: {0}", resp.StatusCode);
                return false;
            }
        }

        public async Task<bool> DropStubConfigAsync(String groupName)
        {
            Debug.WriteLine("WilmaService DropStubConfigAsync: {0}", groupName);

            using (var client = new HttpClient())
            {
                var resp = await client.GetAsync(GetUrl(DROP_STUB_CONFIG_URL_POSTFIX, groupName));

                if (resp.IsSuccessStatusCode)
                {
                    Debug.WriteLine("WilmaService DropStubConfigAsync success.");
                    return true;
                }

                Debug.WriteLine("WilmaService DropStubConfigAsync failed: {0}", resp.StatusCode);
                return false;
            }
        }

        public async Task<bool> PersistActualStubConfigAsync()
        {
            Debug.WriteLine("WilmaService PersistActualStubConfigAsync.");

            using (var client = new HttpClient())
            {
                var resp = await client.GetAsync(GetUrl(SAVE_STUB_CONFIG_URL));

                if (resp.IsSuccessStatusCode)
                {
                    Debug.WriteLine("WilmaService PersistActualStubConfigAsync success.");
                    return true;
                }

                Debug.WriteLine("WilmaService PersistActualStubConfigAsync failed: {0}", resp.StatusCode);
                return false;
            }
        }


        //    /**
        //* Uploads condition checker configuration.
        //*
        //* @param fileName the name of the file
        //* @param file to upload
        //* @return <tt>true</tt> if the upload request is successful, otherwise return <tt>false</tt>
        //*/
        //    public boolean uploadConditionChecker(String fileName, File file)
        //    {
        //        LOG.debug("Upload condition checker configuration.");

        //        return fileUpload.uploadConditionChecker(fileName, file);
        //    }

        //    /**
        //     * Uploads template.
        //     *
        //     * @param fileName the name of the file
        //     * @param file to upload
        //     * @return <tt>true</tt> if the upload request is successful, otherwise return <tt>false</tt>
        //     */
        //    public boolean uploadTemplate(String fileName, File file)
        //    {
        //        LOG.debug("Upload template.");

        //        return fileUpload.uploadTemplate(fileName, file);
        //    }

        //    /**
        //     * Uploads template formatter.
        //     *
        //     * @param fileName the name of the file
        //     * @param file to upload
        //     * @return <tt>true</tt> if the upload request is successful, otherwise return <tt>false</tt>
        //     */
        //    public boolean uploadTemplateFormatter(String fileName, File file)
        //    {
        //        LOG.debug("Upload template formatter.");

        //        return fileUpload.uploadTemplateFormatter(fileName, file);
        //    }

        //    /**
        //     * Uploads stub configuration.
        //     *
        //     * @param fileName the name of the file
        //     * @param file to upload
        //     * @return <tt>true</tt> if the upload request is successful, otherwise return <tt>false</tt>
        //     */
        //    public boolean uploadStubConfiguration(String fileName, File file)
        //    {
        //        LOG.debug("Upload stub configuration.");

        //        return fileUpload.uploadStubConfiguration(fileName, file);
        //    }
        #endregion PUBLIC METHODES
    }
}
