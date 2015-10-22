using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Newtonsoft.Json;

namespace epam.wilma_service_api.StubClasses
{
    internal class DialogDescriptor
    {
        public string Name { get; set; }
        public UsageTypes Usage { get; set; }

        [JsonProperty("hitcount")]
        public long Hitcount { get; set; }

        [JsonProperty("timeout")]
        public long Timeout { get; set; }

        [JsonProperty("comment")]
        public string Comment { get; set; }

        public enum UsageTypes
        {
            always,
            timeout,
            hitcount,
            disabled
        }


        [JsonProperty("conditionDescriptor")]
        public ConditionDescriptor ConditionDescriptor { get; set; }

        [JsonProperty("responseDescriptor")]
        public ResponseDescriptor ResponseDescriptor { get; set; }
    }
}
