using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Newtonsoft.Json;

namespace epam.wilma_service_api.StubClasses
{
    internal class SequenceDescriptor
    {
        [JsonProperty("name")]
        public string Name { get; set; }

        [JsonProperty("groupname")]
        public string GroupName { get; set; }

        [JsonProperty("defaultTimeout")]
        public long DefaultTimeout { get; set; }

        [JsonProperty("active")]
        public bool Active { get; set; }

        [JsonProperty("parameterList")]
        public List<Parameter> Paramters { get; set; }

        [JsonProperty("sequences")]
        public Dictionary<string, WilmaSequence> Sequences { get; set; }
        [JsonProperty("conditionDescriptors")]
        public List<ConditionDescriptor> ConditionDescriptors { get; set; }
        [JsonProperty("dialogDescriptors")]
        public List<DialogDescriptor> DialogDescriptors { get; set; }

        

        public SequenceDescriptor()
        {
            Paramters = new List<Parameter>();
            Active = true;
        }
    }
}
