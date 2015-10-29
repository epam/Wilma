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

using System.Collections.Generic;
using Newtonsoft.Json;

namespace epam.wilma_service_api.StubClasses
{
    internal class SequenceDescriptor
    {
        [JsonProperty("name")]
        public string Name { get; set; }

        [JsonProperty("groupname")]
        public string GroupName { get; set; }

        [JsonProperty("defaultTimeout")]
        public long DefaultTimeout { get; set; }

        [JsonProperty("active")]
        public bool Active { get; set; }

        [JsonProperty("parameterList")]
        public List<Parameter> Paramters { get; set; }

        [JsonProperty("sequences")]
        public Dictionary<string, WilmaSequence> Sequences { get; set; }
        [JsonProperty("conditionDescriptors")]
        public List<ConditionDescriptor> ConditionDescriptors { get; set; }
        [JsonProperty("dialogDescriptors")]
        public List<DialogDescriptor> DialogDescriptors { get; set; }

        

        public SequenceDescriptor()
        {
            Paramters = new List<Parameter>();
            Active = true;
        }
    }
}
