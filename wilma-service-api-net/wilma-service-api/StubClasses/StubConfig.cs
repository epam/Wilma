using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Newtonsoft.Json;

namespace epam.wilma_service_api.StubClasses
{
    internal class StubConfig
    {
        [JsonProperty("configs")]
        public List<StubDescriptor> StubDescriptors { get; set; }

        public StubConfig()
        {
             StubDescriptors = new List<StubDescriptor>();
        }
    }
}
