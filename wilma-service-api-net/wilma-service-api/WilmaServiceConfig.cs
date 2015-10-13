using System;

namespace epam.wilma_service_api
{

    /// <summary>
    /// Configuraation class to store WilmaService config.
    /// </summary>
    public class WilmaServiceConfig
    {
        /// <summary>
        /// WilmaApp host.
        /// </summary>
        public string Host { get; private set; }
        /// <summary>
        /// WilmaApp port.
        /// </summary>
        public ushort Port { get; private set; }

        /// <summary>
        /// Constructor.
        /// </summary>
        /// <param name="host">WilmaApp host.</param>
        /// <param name="port">WilmaApp port.</param>
        public WilmaServiceConfig(string host, ushort port)
        {
            if (string.IsNullOrEmpty(host))
            {
                throw new ArgumentNullException("WilmaServiceConfig host is null or empty.");
            }
            Host = host;
            Port = port;
        }
    }
}
