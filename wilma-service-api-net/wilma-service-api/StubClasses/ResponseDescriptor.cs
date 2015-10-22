using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Newtonsoft.Json;

namespace epam.wilma_service_api.StubClasses
{
    internal class ResponseDescriptor
    {
        [JsonProperty("delay")]
        public int Delay { get; set; }
        [JsonProperty("code")]
        public string Code { get; set; }
        [JsonProperty("mimeType")]
        public string MimeType { get; set; }
        [JsonProperty("template")]
        public Template Template { get; set; }
        [JsonProperty("sequenceDescriptorKey")]
        public string SequenceDescriptorKey { get; set; }


        [JsonProperty("templateFormatterSet")]
        public HashSet<TemplateFormatterDescriptor> TemplateFormatterSet { get; set; }

        public ResponseDescriptor()
        {
            TemplateFormatterSet = new HashSet<TemplateFormatterDescriptor>();
        }
    }
}
