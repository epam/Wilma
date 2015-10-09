using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Newtonsoft.Json;

namespace epam.wilma_service_api.ServiceCommClasses
{
    public class OperationMode
    {
        [JsonProperty("proxyMode")]
        public bool ProxyMode { get; set; }
        [JsonProperty("stubMode")]
        public bool StubMode { get; set; }
        [JsonProperty("wilmaMode")]
        public bool WilmaMode { get; set; }
    }
}
