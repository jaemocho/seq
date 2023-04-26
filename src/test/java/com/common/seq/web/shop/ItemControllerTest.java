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
import com.common.seq.data.dto.shop.ReqItemDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private void setInitData() throws Exception {
        
        // category insert 
        List<ReqCategoryDto> reqCategoryDtos = new ArrayList<ReqCategoryDto>();
        
        ReqCategoryDto reqCategoryDto1 = ReqCategoryDto.builder().name("WOMEN").build();
        ReqCategoryDto reqCategoryDto2 = ReqCategoryDto.builder().name("MEN").build();
        ReqCategoryDto reqCategoryDto3 = ReqCategoryDto.builder().name("KIDS").build();
        
        reqCategoryDtos.add(reqCategoryDto1);
        reqCategoryDtos.add(reqCategoryDto2);
        reqCategoryDtos.add(reqCategoryDto3);

        String content;
        
        for(ReqCategoryDto reqCategoryDto : reqCategoryDtos) {
             content = new ObjectMapper().writeValueAsString(reqCategoryDto);
        
             mockMvc.perform(post("/api/v1/shop/category")
                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                        .content(content)
                                        .accept(MediaType.APPLICATION_JSON_VALUE));
        }

        // item insert 
        List<ReqItemDto> reqItemDtos = new ArrayList<ReqItemDto>();
        
        ReqItemDto reqItemDto1 = ReqItemDto.builder().name("T-shirt").price(5000).remainQty(1000).categoryId(1L).build();
        ReqItemDto reqItemDto2 = ReqItemDto.builder().name("Y-shirt").price(4000).remainQty(500).categoryId(1L).build();
        ReqItemDto reqItemDto3 = ReqItemDto.builder().name("T-shirt").price(3000).remainQty(200).categoryId(2L).build();
        ReqItemDto reqItemDto4 = ReqItemDto.builder().name("T-shirt").price(2000).remainQty(200).categoryId(3L).build();

        reqItemDtos.add(reqItemDto1);
        reqItemDtos.add(reqItemDto2);
        reqItemDtos.add(reqItemDto3);
        reqItemDtos.add(reqItemDto4);

        for(ReqItemDto reqItemDto : reqItemDtos) {
            content = new ObjectMapper().writeValueAsString(reqItemDto);
       
            mockMvc.perform(post("/api/v1/shop/item")
                                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                                       .content(content)
                                       .accept(MediaType.APPLICATION_JSON_VALUE));
       }
    }

    @Test
    public void item_test() throws Exception {
        
        // category data insert
        // category 3개 
        // item 4개 입력 
        setInitData();

        ResultActions resultAction;
        String content;
        ReqItemDto reqItemDto;

        // MEN Category T-shirt insert test 
        // 201 성공
        // item 5개
        reqItemDto = ReqItemDto.builder()
                                        .name("Y-shirt")
                                        .price(5000)
                                        .remainQty(1000)
                                        .categoryId(2L)
                                        .build();

        content = new ObjectMapper().writeValueAsString(reqItemDto);

        resultAction = mockMvc.perform(post("/api/v1/shop/item")
                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                        .content(content)
                                        .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(status().isCreated())
                    .andExpect(content().string("SUCCESS"))
                    .andDo(MockMvcResultHandlers.print());


        // WOMEN Category T-shirt insert test (name null test )
        // 400 error 실패 
        reqItemDto = ReqItemDto.builder()
                                        .price(5000)
                                        .remainQty(1000)
                                        .categoryId(2L)
                                        .build();

        content = new ObjectMapper().writeValueAsString(reqItemDto);

        resultAction = mockMvc.perform(post("/api/v1/shop/item")
                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                        .content(content)
                                        .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("name:must not be null"))
                    .andDo(MockMvcResultHandlers.print());                                           


        // MEN Category T-shirt delete test 
        // 200 성공
        // item 4개 
        resultAction = mockMvc.perform(delete("/api/v1/shop/item/5")
                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                        .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(status().isOk())
                    .andExpect(content().string("SUCCESS"))
                    .andDo(MockMvcResultHandlers.print());                                        


        // id 1 번 조회 test
        // 200 성공
        // item 4개 
        resultAction = mockMvc.perform(get("/api/v1/shop/item/1")
                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                        .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("T-shirt"))
                    .andExpect(jsonPath("$.price").value(5000))
                    .andExpect(jsonPath("$.remainQty").value(1000))
                    .andExpect(jsonPath("$.categoryId").value(1))
                    .andExpect(jsonPath("$.categoryName").value("WOMEN"))
                    .andDo(MockMvcResultHandlers.print());

        
        // category로 item 조회 test
        // 200 성공
        // WOMEN Category t-shirt/y-shirt 두개 
        resultAction = mockMvc.perform(get("/api/v1/shop/items")
                                        .param("categoryId", "1")
                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                        .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].categoryId").value(1))
                    .andExpect(jsonPath("$[0].categoryName").value("WOMEN"))
                    .andExpect(jsonPath("$[1].categoryId").value(1))
                    .andExpect(jsonPath("$[1].categoryName").value("WOMEN"))
                    .andExpect(jsonPath("$[3]").doesNotExist())
                    .andDo(MockMvcResultHandlers.print());       


        
        // update test 
        // 200 성공
        reqItemDto = ReqItemDto.builder()
                            .name("pants")
                            .price(10000)
                            .remainQty(9000)
                            .build();

        content = new ObjectMapper().writeValueAsString(reqItemDto);

        resultAction = mockMvc.perform(put("/api/v1/shop/item/1")
                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                        .content(content)
                                        .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(status().isOk())
                    .andExpect(content().string("SUCCESS"))
                    .andDo(MockMvcResultHandlers.print());   


        // update 후 조회 
        // item 4개 
        resultAction = mockMvc.perform(get("/api/v1/shop/item/1")
                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                        .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("pants"))
                    .andExpect(jsonPath("$.price").value(10000))
                    .andExpect(jsonPath("$.remainQty").value(9000))
                    .andExpect(jsonPath("$.categoryId").value(1))
                    .andExpect(jsonPath("$.categoryName").value("WOMEN"))
                    .andDo(MockMvcResultHandlers.print());                    

    }

    
}
