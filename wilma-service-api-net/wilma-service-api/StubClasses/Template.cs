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
    internal class Template
    {
        [JsonProperty("name")]
        public string Name { get; set; }

        [JsonProperty("type")]
        public TemplateTypes Type { get; set; }

        [JsonProperty("resource")]
        public byte[] Resource { get; set; }
    }

    internal enum TemplateTypes
    {
        XML,
        TEXT,
        HTML,
        JSON,
        XMLFILE,
        HTMLFILE,
        TEXTFILE,
        JSONFILE,
        EXTERNAL
    }
}
