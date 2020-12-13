package com.epam.sandbox.interceptor;

/**
 * WARNING: THIS DOCUMENT CONTAINS CONFIDENTIAL INFORMATION PROPERTY OF EPAM SYSTEMS.
 * THIS CONTENT MAY NOT BE USED OR DISCLOSED WITHOUT PRIOR WRITTEN CONSENT OF THE OWNER.
 * THE EPAM IP IS BEING PROVIDED ON AN "AS IS" BASIS. EPAM DISCLAIMS ALL WARRANTIES,
 * EITHER EXPRESS OR IMPLIED INCLUDING, BUT NOT LIMITED TO, IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT
 */

import com.epam.sandbox.common.SuperLogic;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.interceptor.RequestInterceptor;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;

public class RequestInterceptorJared implements RequestInterceptor {
    
    private SuperLogic superLogic = new SuperLogic();

    @Override
    public void onRequestReceive(final WilmaHttpRequest request, final ParameterList parameters) {
        System.out.println("request interceptor: " + superLogic.getResult());
    }

}
