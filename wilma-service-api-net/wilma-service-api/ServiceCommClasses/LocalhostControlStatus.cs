using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Newtonsoft.Json;

namespace epam.wilma_service_api.ServiceCommClasses
{
    /// <summary>
    /// LocalhostControlStatus class.
    /// </summary>
    public class LocalhostControlStatus
    {
        /// <summary>
        /// LocalhostMode
        /// </summary>
        [JsonProperty("localhostMode")]
        public bool LocalhostMode { get; set; }
    }
}
