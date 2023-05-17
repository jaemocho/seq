package com.common.seq.web.shop.util;

import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Map;

public interface RequestAction {

    public ResultActions doAction(MockHttpServletRequestBuilder mockHttpServletRequestBuilder) throws Exception;

    public ResultActions doAction(MockHttpServletRequestBuilder mockHttpServletRequestBuilder, Object content) throws Exception;
        
    public RequestAction setParams(Map<String, String> params);

}
