package com.epam.wilma.webapp.stub.response.formatter.string;

import java.util.List;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.dialog.response.template.TemplateFormatter;
import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;

/**
 * Example template formatter showing how to get the request body
 * and also how to work with the parameters.
 * @author Tunde_Kovacs
 *
 */
public class ExampleTemplateFormatter implements TemplateFormatter {

    @Override
    public byte[] formatTemplate(final WilmaHttpRequest wilmaRequest, final byte[] templateResource, final ParameterList params) throws Exception {
        String body = wilmaRequest.getBody();
        List<Parameter> allParameters = params.getAllParameters();
        Parameter parameter = allParameters.get(0);
        String name = parameter.getName();
        String value = parameter.getValue();
        String result = body.replace(name, value);
        return result.getBytes();
    }

}
