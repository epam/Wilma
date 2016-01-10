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
