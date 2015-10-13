using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Newtonsoft.Json;

namespace epam.wilma_service_api.StubClasses
{
    public class StubDescriptor
    {
        [JsonProperty("sequenceDescriptors")]
        public List<SequenceDescriptor> SequenceDescriptors { get; set; }

        [JsonProperty("dialogDescriptors")]
        public List<DialogDescriptor> DialogDescriptors { get; set; }

        [JsonProperty("interceptorDescriptors")]
        public List<InterceptorDescriptor> InterceptorDescriptors { get; set; }

        [JsonProperty("active")]
        public bool Active { get; set; }

        [JsonProperty("groupname")]
        public string GroupName { get; set; }

        public StubDescriptor()
        {
            SequenceDescriptors = new List<SequenceDescriptor>();
            DialogDescriptors = new List<DialogDescriptor>();
            InterceptorDescriptors = new List<InterceptorDescriptor>();
        }
    }
}
