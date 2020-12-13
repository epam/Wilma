package com.epam.sandbox.interceptor;

import com.epam.sandbox.common.SuperLogic;
import com.epam.wilma.domain.http.WilmaHttpResponse;
import com.epam.wilma.domain.stubconfig.interceptor.ResponseInterceptor;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;

public class ResponseInterceptorJared implements ResponseInterceptor{
    
    private SuperLogic superLogic = new SuperLogic();

    @Override
    public void onResponseReceive(WilmaHttpResponse response, ParameterList parameters){
        System.out.println("response interceptor: " + superLogic.getResult());
    }
}