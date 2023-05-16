package com.common.seq.web.shop.util;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RequestAction {

    private final MockMvc mockMvc;

    private MockHttpServletRequestBuilder mockHttpServletRequestBuilder;

    public ResultActions doAction(MockHttpServletRequestBuilder mockHttpServletRequestBuilder) throws Exception{
        return doAction(mockHttpServletRequestBuilder, null);
    }

    public ResultActions doAction(MockHttpServletRequestBuilder mockHttpServletRequestBuilder, Object content) throws Exception{
        this.mockHttpServletRequestBuilder = mockHttpServletRequestBuilder;
        setContent(makeJsonString(content));
        setContentType();
        setAccept();
        return mockMvc.perform(mockHttpServletRequestBuilder);
    }

    public RequestAction setParams(Map<String, String> params) {
        params.forEach((key, value) -> {
            setParam(key, value);
        });
        return this;
    }

    private void setParam(String key, String value) {
        mockHttpServletRequestBuilder.param(key, value);
    }

    private void setContent(String content){
        if (content != null) {
            mockHttpServletRequestBuilder.content(content);
        }
    }

    private String makeJsonString(Object object) throws Exception{
        if(object == null) {
            return null;
        }
        return new ObjectMapper().writeValueAsString(object);
    }

    private void setContentType() {
        setContentType(MediaType.APPLICATION_JSON_VALUE);
    }

    private void setContentType(String contentType) {
        mockHttpServletRequestBuilder.contentType(contentType);
    }

    private void setAccept() {
        setAccept(MediaType.APPLICATION_JSON_VALUE);
    }
    
    private void setAccept(String accept) {
        mockHttpServletRequestBuilder.accept(accept);
    }

}
