using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Newtonsoft.Json;

namespace epam.wilma_service_api.StubClasses
{
    public class WilmaSequence
    {
        [JsonProperty("timestamp")]
        public TimeSpan TimeStamp { get; set; }

        [JsonProperty("sequenceKey")]
        public string SequenceKey { get; set; }

        [JsonProperty("messageStore")]
        public WilmaSequencePairs MessageStore { get; set; }
    }
}
