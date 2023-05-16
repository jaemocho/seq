package com.common.seq.web.shop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.common.seq.data.dto.shop.ReqCategoryDto;
import com.common.seq.web.shop.util.RequestAction;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ActiveProfiles("dev")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) 
public class CategoryControllerTest {

    @Autowired
    public RequestAction requestAction;
    
    @Test
    @DisplayName("Category 생성 테스트")
    public void test1() throws Exception {
        ReqCategoryDto reqCategoryDto = ReqCategoryDto.builder()
                                                    .name("WOMEN")
                                                    .build();
        
        ResultActions resultAction = requestAction.doAction(post("/api/v1/shop/category"), reqCategoryDto);

        resultAction.andExpect(status().isCreated())
                    .andExpect(content().string("SUCCESS"))
                    .andDo(MockMvcResultHandlers.print());                     
    }    
    
    @Test
    @DisplayName("Category 생성 테스트 (name 누락)")
    public void test2() throws Exception {
        ReqCategoryDto reqCategoryDto = ReqCategoryDto.builder()
                                                    .build();
        ResultActions resultAction = requestAction.doAction(post("/api/v1/shop/category"), reqCategoryDto);

        resultAction.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("name:must not be null"))
                    .andDo(MockMvcResultHandlers.print());                        
    }


    @Test
    @DisplayName("Category 조회 테스트")
    public void test3() throws Exception {
        
        createCategoryListForTest();

        // 전체 category 조회 테스트 
        ResultActions resultAction = requestAction.doAction(get("/api/v1/shop/categorys"));
                                            

        resultAction.andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].name").value("category1"))
                    .andExpect(jsonPath("$[1].name").value("category2"))
                    .andExpect(jsonPath("$[2].name").value("category2"))
                    .andDo(MockMvcResultHandlers.print());
                    
                            // category id 조회 테스트
        resultAction = requestAction.doAction(get("/api/v1/shop/category/3"));
                                            

        resultAction.andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("category2"))
                    .andDo(MockMvcResultHandlers.print());

        
        
        // category  조회 테스트 by name 
        resultAction = requestAction.doAction(get("/api/v1/shop/categorys?name=category2"));

        resultAction.andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].name").value("category2"))
                    .andExpect(jsonPath("$[1].name").value("category2"))
                    .andExpect(jsonPath("$[3]").doesNotExist())
                    .andDo(MockMvcResultHandlers.print());                                        


    }


    @Test
    @DisplayName("Category 삭제 테스트")
    public void test4() throws Exception {
        createCategoryListForTest();

        // category id 삭제 테스트
        ResultActions resultAction = requestAction.doAction(delete("/api/v1/shop/category/7"));

        resultAction.andExpect(status().isOk())
                    .andExpect(content().string("SUCCESS"))
                    .andDo(MockMvcResultHandlers.print());        


        // 삭제 후 전체 id 조회 테스트 
        resultAction = requestAction.doAction(get("/api/v1/shop/categorys"));
                            
        resultAction.andExpect(status().isOk())
                    .andExpect(jsonPath("$[3]").doesNotExist())
                    .andDo(MockMvcResultHandlers.print());
    }

    private void createCategoryListForTest() throws Exception {
        List<ReqCategoryDto> reqCategoryDtos = createTestReqCategoryDtos();
        
        for(ReqCategoryDto reqCategoryDto : reqCategoryDtos) {
            requestAction.doAction(post("/api/v1/shop/category"), reqCategoryDto);        
        }
    }

    private List<ReqCategoryDto> createTestReqCategoryDtos() {
        List<ReqCategoryDto> reqCategoryDtos = new ArrayList<ReqCategoryDto>();
        reqCategoryDtos.add(ReqCategoryDto.builder().name("category1").build());
        reqCategoryDtos.add(ReqCategoryDto.builder().name("category2").build());
        reqCategoryDtos.add(ReqCategoryDto.builder().name("category2").build());

        return reqCategoryDtos;
    }

}
