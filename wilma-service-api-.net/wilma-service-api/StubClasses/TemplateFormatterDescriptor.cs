using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Newtonsoft.Json;

namespace epam.wilma_service_api.StubClasses
{
    public class TemplateFormatterDescriptor
    {
        [JsonProperty("templateFormatter")]
        public TemplateFormatter TemplateFormatter { get; set; }

        [JsonProperty("params")]
        public List<Parameter> Parameters { get; set; }
    }
}
