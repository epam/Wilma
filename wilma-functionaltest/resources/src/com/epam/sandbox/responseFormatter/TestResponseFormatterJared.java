package com.epam.sandbox.responseFormatter;

/**
 * WARNING: THIS DOCUMENT CONTAINS CONFIDENTIAL INFORMATION PROPERTY OF EPAM SYSTEMS.
 * THIS CONTENT MAY NOT BE USED OR DISCLOSED WITHOUT PRIOR WRITTEN CONSENT OF THE OWNER.
 * THE EPAM IP IS BEING PROVIDED ON AN "AS IS" BASIS. EPAM DISCLAIMS ALL WARRANTIES,
 * EITHER EXPRESS OR IMPLIED INCLUDING, BUT NOT LIMITED TO, IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT
 */

import com.epam.sandbox.common.SuperLogic;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.dialog.response.ResponseFormatter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;

import javax.servlet.http.HttpServletResponse;

public class TestResponseFormatterJared implements ResponseFormatter {

    SuperLogic superLogic = new SuperLogic();

    @Override
    public byte[] formatResponse(final WilmaHttpRequest wilmaRequest, HttpServletResponse resp,
                                 final byte[] templateResource, final ParameterList params) throws Exception {
        if (superLogic.getResult()) {
            return new byte[0];
        } else {
            return null;
        }
    }

}