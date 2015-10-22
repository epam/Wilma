using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Newtonsoft.Json;

namespace epam.wilma_service_api.ServiceCommClasses
{
    /// <summary>
    /// LoggingStatus class.
    /// </summary>
    public class LoggingStatus
    {
        /// <summary>
        /// Request logging
        /// </summary>
        [JsonProperty("requestLogging")]
        public bool RequestLogging { get; set; }

        /// <summary>
        /// Response logging
        /// </summary>
        [JsonProperty("responseLogging")]
        public bool ResponseLogging { get; set; }
    }
}
