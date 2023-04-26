package com.common.seq.web.shop.util;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ControllerTestUtils {
    
    public static <T> void requestInitDataSimplePost(MockMvc mockMvc, List<T> list, String url) throws Exception {
        
        String content ;

        for(T t : list) {
            content = new ObjectMapper().writeValueAsString(t);

            mockMvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(content)
                    .accept(MediaType.APPLICATION_JSON_VALUE));
        }
    }
}
