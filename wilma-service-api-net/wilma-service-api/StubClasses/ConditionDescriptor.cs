using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Newtonsoft.Json;

namespace epam.wilma_service_api.StubClasses
{
    public class ConditionDescriptor
    {
        [JsonProperty("condition")]
        public Condition Condition { get; set; }
    }
}
