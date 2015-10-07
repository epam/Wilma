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
           public enum MessageLoggingControlStatus 
           {
               On, 
               Off,
           }

        private readonly WilmaServiceConfig _config;

        private const string VERSION_INFO_URL_POSTFIX = "config/public/version";
        private const string ACTUAL_LOAD_INFO_URL_POSTFIX = "config/public/actualload";
        private const string SHUTDOWN_URL_POSTFIX = "config/admin/shutdown";

         private const string  STATUS_GETTER_URL_POSTFIX = "config/public/logging/status";
         private const string STATUS_SETTER_URL_POSTFIX_FORMAT = "config/admin/logging/{0}";

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

        public async Task<string> GetVersionInformationAsync()
        {
            Debug.WriteLine("WilmaService GetVersionInformationAsync enter...");

            using (var client = new HttpClient())
            {
                var resp = await client.GetAsync(string.Format("{0}:{1}/{2}", _config.Host, _config.Port, VERSION_INFO_URL_POSTFIX));

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
                var resp = await client.GetAsync(string.Format("{0}:{1}/{2}", _config.Host, _config.Port, ACTUAL_LOAD_INFO_URL_POSTFIX));

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
                var resp = await client.GetAsync(string.Format("{0}:{1}/{2}", _config.Host, _config.Port, SHUTDOWN_URL_POSTFIX));

                if (resp.IsSuccessStatusCode)
                {
                    Debug.WriteLine("WilmaService ShutdownApplication success.");
                    return true;
                }

                Debug.WriteLine("WilmaService ShutdownApplication failed: {0}", resp.StatusCode);
                return false;
            }
        }

        public  async Task<MessageLoggingControlStatus> GetMessageLoggingStatusAsync()
        {
            Debug.WriteLine("WilmaService GetMessageLoggingStatusAsync enter...");

            using (var client = new HttpClient())
            {
                var resp = await client.GetAsync(string.Format("{0}:{1}/{2}", _config.Host, _config.Port, STATUS_GETTER_URL_POSTFIX));

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
                var resp = await client.GetAsync(string.Format("{0}:{1}/{2}", _config.Host, _config.Port, string.Format(STATUS_SETTER_URL_POSTFIX_FORMAT, control.ToString().ToLower())));

                if (resp.IsSuccessStatusCode)
                {
                    Debug.WriteLine("WilmaService SetMessageLoggingStatus success.");
                    return true;
                }

                Debug.WriteLine("WilmaService SetMessageLoggingStatus failed: {0}", resp.StatusCode);
                return false;
            }
        }
    }
}
