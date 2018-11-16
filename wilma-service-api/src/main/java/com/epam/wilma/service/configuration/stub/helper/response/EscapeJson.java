package com.epam.wilma.service.configuration.stub.helper.response;

/*==========================================================================
 Copyright since 2013, EPAM Systems

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

/**
 * Class to escape Json strings.
 *
 * @author Tamas_Kohegyi
 */
public class EscapeJson {

    /**
     * Escape text so that it can be used as text representation of the value of a Json object.
     *
     * @param text is the text that should be escaped
     * @return with a json safe text
     */
    public String escapeJson(final String text) {
        String revisedText = null;
        if (text != null) {
            StringBuilder sb = new StringBuilder(text.length() * 2);
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                switch (c) {
                case '\"':
                    sb.append("\\\"");
                    break;
                default:
                    sb.append(c);
                }
            }
            //with escaped string we return
            revisedText = sb.toString();
        }
        return revisedText;
    }

}
