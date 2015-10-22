using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Newtonsoft.Json;

namespace epam.wilma_service_api.ServiceCommClasses
{
    /// <summary>
    /// OperationMode class.
    /// </summary>
    public class OperationMode
    {
        /// <summary>
        /// Proxy
        /// </summary>
        [JsonProperty("proxyMode")]
        public bool ProxyMode { get; set; }
        /// <summary>
        /// Stub
        /// </summary>
        [JsonProperty("stubMode")]
        public bool StubMode { get; set; }
        /// <summary>
        /// Wilma
        /// </summary>
        [JsonProperty("wilmaMode")]
        public bool WilmaMode { get; set; }
    }
}
