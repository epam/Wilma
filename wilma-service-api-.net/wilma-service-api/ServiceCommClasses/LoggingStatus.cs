using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Newtonsoft.Json;

namespace epam.wilma_service_api.ServiceCommClasses
{
    public class LoggingStatus
    {
        [JsonProperty("requestLogging")]
        public bool RequestLogging { get; set; }

        [JsonProperty("responseLogging")]
        public bool ResponseLogging { get; set; }
    }
}
