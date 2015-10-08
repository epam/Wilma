using System.Diagnostics;

namespace epam.wilma_service_api
{
    public class WilmaServiceConfig
    {
        public string Host { get; private set; }
        public uint Port { get; private set; }

        public WilmaServiceConfig(string host, uint port)
        {
            Host = host;
            Port = port;
        }
    }
}
