using Newtonsoft.Json;

namespace epam.wilma_service_api.ServiceCommClasses
{
    public class LoadInformation
    {
        [JsonProperty("deletedFilesCount")]
        public int DeletedFilesCount { get; set; }

        [JsonProperty("countOfMessages")]
        public int CountOfMessages { get; set; }

        [JsonProperty("responseQueueSize")]
        public int ResponseQueueSize { get; set; }

        [JsonProperty("loggerQueueSize")]
        public int LoggerQueueSize { get; set; }
    }
}
