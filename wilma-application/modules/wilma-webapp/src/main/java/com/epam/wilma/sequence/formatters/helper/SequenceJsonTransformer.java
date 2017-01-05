package com.epam.wilma.sequence.formatters.helper;
/*==========================================================================
Copyright 2013-2017 EPAM Systems

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.sequence.formatters.helper.converter.Converter;
import com.epam.wilma.sequence.formatters.helper.converter.NeutralConverter;
import com.epam.wilma.sequence.formatters.helper.converter.XmlConverter;

/**
 * Used to create a map based on the resolved names as keys and the bodies (of
 * requests and responses converted to JSON format) as values.
 * @author Adam_Csaba_Kiraly
 */
@Component
public class SequenceJsonTransformer extends SequenceTransformer {

    @Autowired
    private XmlConverter xmlConverter;
    @Autowired
    private NeutralConverter neutralConverter;

    @Override
    protected Converter getXmlConverter() {
        return xmlConverter;
    }

    @Override
    protected Converter getJsonConverter() {
        return neutralConverter;
    }

}
