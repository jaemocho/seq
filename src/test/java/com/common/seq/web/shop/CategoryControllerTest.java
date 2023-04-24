package com.common.seq.web.shop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.common.seq.data.dto.shop.ReqCategoryDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ActiveProfiles("dev")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK) 
public class CategoryControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void create_test() throws Exception {
                // 정상 data test
        ReqCategoryDto reqCategoryDto = ReqCategoryDto.builder()
                                            .name("WOMEN")
                                            .build();

        String content = new ObjectMapper().writeValueAsString(reqCategoryDto);
        

        ResultActions resultAction = mockMvc.perform(post("/api/v1/shop/category")
                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                        .content(content)
                                        .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(status().isCreated())
                    .andExpect(content().string("SUCCESS"))
                    .andDo(MockMvcResultHandlers.print());

        // name 누락 test
        reqCategoryDto = ReqCategoryDto.builder()
                        .build();

        content = new ObjectMapper().writeValueAsString(reqCategoryDto);

        resultAction = mockMvc.perform(post("/api/v1/shop/category")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("name:must not be null"))
                    .andDo(MockMvcResultHandlers.print());                        
    }

    private void setCategoryList() throws Exception {
        List<ReqCategoryDto> reqCategoryDtos = new ArrayList<ReqCategoryDto>();
        reqCategoryDtos.add(ReqCategoryDto.builder().name("category1").build());
        reqCategoryDtos.add(ReqCategoryDto.builder().name("category2").build());
        reqCategoryDtos.add(ReqCategoryDto.builder().name("category2").build());

        for(ReqCategoryDto reqCategoryDto : reqCategoryDtos) {
            String content = new ObjectMapper().writeValueAsString(reqCategoryDto);

            mockMvc.perform(post("/api/v1/shop/category")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content)
                .accept(MediaType.APPLICATION_JSON_VALUE));

        }
    }

    @Test
    public void delete_get_test() throws Exception {

        // category data insert
        setCategoryList();

        // 전체 category 조회 테스트 
        ResultActions resultAction = mockMvc.perform(get("/api/v1/shop/categorys")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE));


        resultAction.andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].name").value("category1"))
                    .andExpect(jsonPath("$[1].name").value("category2"))
                    .andExpect(jsonPath("$[2].name").value("category2"))
                    .andDo(MockMvcResultHandlers.print());                    


        // category id 삭제 테스트
         resultAction = mockMvc.perform(delete("/api/v1/shop/category/2")
                                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                                            .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(status().isOk())
                    .andExpect(content().string("SUCCESS"))
                    .andDo(MockMvcResultHandlers.print());        


        
        // 삭제 후 전체 id 조회 테스트 
        resultAction = mockMvc.perform(get("/api/v1/shop/categorys")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE));


        resultAction.andExpect(status().isOk())
                    .andExpect(jsonPath("$[3]").doesNotExist())
                    .andDo(MockMvcResultHandlers.print());      

        // category id 조회 테스트
        resultAction = mockMvc.perform(get("/api/v1/shop/category/3")
                                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                                            .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("category2"))
                    .andDo(MockMvcResultHandlers.print());

        

        // category  조회 테스트 by name 
        resultAction = mockMvc.perform(get("/api/v1/shop/categorys?name=category2")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE));


        resultAction.andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].name").value("category2"))
                    .andExpect(jsonPath("$[1].name").value("category2"))
                    .andExpect(jsonPath("$[3]").doesNotExist())
                    .andDo(MockMvcResultHandlers.print());                                        


    }

}
