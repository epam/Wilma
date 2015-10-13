using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Newtonsoft.Json;

namespace epam.wilma_service_api.StubClasses
{
    public class InterceptorDescriptor
    {
        [JsonProperty("name")]
        public string Name { get; set; }

        [JsonProperty("requestInterceptor")]
        public RequestInterceptor RequestInterceptor { get; set; }

        [JsonProperty("responseInterceptor")]
        public ResponseInterceptor ResponseInterceptor;

        [JsonProperty("params")]
        public List<Parameter> Parameters { get; set; }

        public InterceptorDescriptor()
        {
                Parameters = new List<Parameter>();
        }
    }
}
