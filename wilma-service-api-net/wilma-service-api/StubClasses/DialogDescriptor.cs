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

using Newtonsoft.Json;

namespace epam.wilma_service_api.StubClasses
{
    internal class DialogDescriptor
    {
        public string Name { get; set; }
        public UsageTypes Usage { get; set; }

        [JsonProperty("hitcount")]
        public long Hitcount { get; set; }

        [JsonProperty("timeout")]
        public long Timeout { get; set; }

        [JsonProperty("comment")]
        public string Comment { get; set; }

        public enum UsageTypes
        {
            always,
            timeout,
            hitcount,
            disabled
        }


        [JsonProperty("conditionDescriptor")]
        public ConditionDescriptor ConditionDescriptor { get; set; }

        [JsonProperty("responseDescriptor")]
        public ResponseDescriptor ResponseDescriptor { get; set; }
    }
}
