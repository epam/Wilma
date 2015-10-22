using Newtonsoft.Json;

namespace epam.wilma_service_api.ServiceCommClasses
{
    /// <summary>
    /// Wilma Service Load Information.
    /// </summary>
    public class LoadInformation
    {
        /// <summary>
        /// Deleted files count.
        /// </summary>
        [JsonProperty("deletedFilesCount")]
        public int DeletedFilesCount { get; set; }

        /// <summary>
        /// Messages count.
        /// </summary>
        [JsonProperty("countOfMessages")]
        public int CountOfMessages { get; set; }

        /// <summary>
        /// Response queue size.
        /// </summary>
        [JsonProperty("responseQueueSize")]
        public int ResponseQueueSize { get; set; }

        /// <summary>
        /// Logger queue size.
        /// </summary>
        [JsonProperty("loggerQueueSize")]
        public int LoggerQueueSize { get; set; }
    }
}
