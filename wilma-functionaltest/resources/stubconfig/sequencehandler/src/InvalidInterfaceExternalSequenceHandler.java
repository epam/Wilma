package com.epam.wilma.sequence;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.dialog.response.template.TemplateFormatter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.domain.stubconfig.sequence.WilmaSequence;

public class InvalidInterfaceExternalSequenceHandler implements TemplateFormatter {

    @Override
    public byte[] formatTemplate(WilmaHttpRequest wilmaRequest, byte[] templateResource, ParameterList params, WilmaSequence sequence) throws Exception {
        return new byte[0];
    }
}
