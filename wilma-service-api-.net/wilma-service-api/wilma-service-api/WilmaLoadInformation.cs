namespace epam.wilma_service_api
{
    public class WilmaLoadInformation
    {
        public int deletedFilesCount { get; set; }
        public int countOfMessages { get; set; }
        public int responseQueueSize { get; set; }
        public int loggerQueueSize { get; set; }
    }
}
