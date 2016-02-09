package com.epam.wilma.service.unit.response;
/*==========================================================================
 Copyright 2013-2016 EPAM Systems

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

import com.epam.wilma.service.unit.helper.Template;
import com.epam.wilma.service.unit.helper.TemplateFormatter;

import java.util.LinkedList;

/**
 * A Response Descriptor class.
 *
 * @author Tamas_Kohegyi
 *
 */
public class ResponseDescriptor {
    private String delay;
    private String code;
    private String mimeType;
    private Template template;
    private LinkedList<TemplateFormatter> templateFormatters;


    public ResponseDescriptor(String delay, String code, String mimeType, Template template, LinkedList<TemplateFormatter> templateFormatters) {
        this.delay = delay;
        this.code = code;
        this.mimeType = mimeType;
        this.template = template;
        this.templateFormatters = templateFormatters;
    }
    public String responseDescriptorToString() {
        String responseDescriptor = "<response-descriptor delay=\"" + delay + "\" "
            + "code=\"" + code + "\" mimetype=\"" + mimeType + "\" template=\"" + template.getName() + "\" >\n";
        for (TemplateFormatter templateFormatter: templateFormatters) {
                responseDescriptor += templateFormatter.toString();
        }
        responseDescriptor += "</response-descriptor>\n";
        return responseDescriptor;
    }

    public String templateToString() {
        return template.toString();
    }

}
