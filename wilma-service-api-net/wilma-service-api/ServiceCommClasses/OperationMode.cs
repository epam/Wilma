/*==========================================================================
 Copyright 2016 EPAM Systems

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

using Newtonsoft.Json;

namespace epam.wilma_service_api.ServiceCommClasses
{
    /// <summary>
    /// OperationMode class.
    /// </summary>
    public class OperationMode
    {
        /// <summary>
        /// Proxy
        /// </summary>
        [JsonProperty("proxyMode")]
        public bool ProxyMode { get; set; }
        /// <summary>
        /// Stub
        /// </summary>
        [JsonProperty("stubMode")]
        public bool StubMode { get; set; }
        /// <summary>
        /// Wilma
        /// </summary>
        [JsonProperty("wilmaMode")]
        public bool WilmaMode { get; set; }
    }
}
