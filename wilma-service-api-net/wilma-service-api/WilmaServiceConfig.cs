/*==========================================================================
 Copyright 2015 EPAM Systems

 This file is part of Wilma.

 Wilma is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Wilma is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Wilma.  If not, see <http://www.gnu.org/licenses/>.
 ===========================================================================*/

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
