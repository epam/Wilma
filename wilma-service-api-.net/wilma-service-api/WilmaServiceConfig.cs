using System.Diagnostics;

namespace epam.wilma_service_api
{
    public class WilmaServiceConfig
    {
        public string Host { get; private set; }
        public uint Port { get; private set; }

        public WilmaServiceConfig(string host, uint port)
        {
            Debug.WriteLine("WilmaServiceConfig cretaed host: {0}, port: {1}", host, port);
            Host = host;
            Port = port;
        }
    }
}
