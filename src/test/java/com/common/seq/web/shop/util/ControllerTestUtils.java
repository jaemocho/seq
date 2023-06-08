package com.common.seq.web.shop.util;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ControllerTestUtils {
    
    public static <T> void requestInitDataSimplePost(MockMvc mockMvc, List<T> list, String url) throws Exception {
        
        String content ;

        for(T t : list) {
            content = makeContentByObjectMapper(t);

            mockMvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(content)
                    .accept(MediaType.APPLICATION_JSON_VALUE));
        }
    }

    private static String makeContentByObjectMapper(Object object) throws Exception{
        return new ObjectMapper().writeValueAsString(object);
    }

    public static <T> List<T> makeDtoListByContent(String content, Class<T> elementClass) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        return mapper.readValue(content
            ,typeFactory.constructCollectionType(List.class, elementClass));
        
    }
}
