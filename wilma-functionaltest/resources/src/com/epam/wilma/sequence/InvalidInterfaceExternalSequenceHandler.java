package com.epam.wilma.sequence;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.dialog.response.template.TemplateFormatter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;

import javax.servlet.http.HttpServletResponse;

public class InvalidInterfaceExternalSequenceHandler implements TemplateFormatter {

    @Override
    public byte[] formatTemplate(WilmaHttpRequest wilmaRequest, final HttpServletResponse resp,
                                 byte[] templateResource, ParameterList params) throws Exception {
        return new byte[0];
    }
}
