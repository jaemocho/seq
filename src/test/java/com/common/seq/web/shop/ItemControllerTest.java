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
import com.common.seq.data.dto.shop.ReqItemDto;
import com.common.seq.web.shop.util.RequestAction;


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
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) 
public class ItemControllerTest {

    @Autowired
    public RequestAction requestAction;

    @Test
    @DisplayName("통합테스트")
    public void test1() throws Exception {
        
        // category data insert
        // category 3개 
        // item 4개 입력 
        setInitData();

        ResultActions resultAction;
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

        resultAction = requestAction.doAction(post("/api/v1/shop/item"), reqItemDto);

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
        resultAction = requestAction.doAction(post("/api/v1/shop/item"), reqItemDto);
        resultAction.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("name:must not be null"))
                    .andDo(MockMvcResultHandlers.print());                                           


        // MEN Category T-shirt delete test 
        // 200 성공
        // item 4개 
        resultAction = requestAction.doAction(delete("/api/v1/shop/item/5"));
        resultAction.andExpect(status().isOk())
                    .andExpect(content().string("SUCCESS"))
                    .andDo(MockMvcResultHandlers.print());                                        


        // id 1 번 조회 test
        // 200 성공
        // item 4개 
        resultAction = requestAction.doAction(get("/api/v1/shop/item/1"));
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
        resultAction = requestAction.doAction(get("/api/v1/shop/items?categoryId=1"));
       

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

        resultAction = requestAction.doAction(put("/api/v1/shop/item/1"),reqItemDto);
        
        resultAction.andExpect(status().isOk())
                    .andExpect(content().string("SUCCESS"))
                    .andDo(MockMvcResultHandlers.print());   


        // update 후 조회 
        // item 4개 
        resultAction = requestAction.doAction(get("/api/v1/shop/item/1"));

        resultAction.andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("pants"))
                    .andExpect(jsonPath("$.price").value(10000))
                    .andExpect(jsonPath("$.remainQty").value(9000))
                    .andExpect(jsonPath("$.categoryId").value(1))
                    .andExpect(jsonPath("$.categoryName").value("WOMEN"))
                    .andDo(MockMvcResultHandlers.print());                    

    }

    private void setInitData() throws Exception {
        
        // category insert 
        List<ReqCategoryDto> reqCategoryDtos = createTestReqCategoryDtos();
        
        for(ReqCategoryDto reqCategoryDto : reqCategoryDtos) {
             requestAction.doAction(post("/api/v1/shop/category"), reqCategoryDto);
        }

        // item insert 
        List<ReqItemDto> reqItemDtos = createTestReqItemDtos();
        for(ReqItemDto reqItemDto : reqItemDtos) {
            requestAction.doAction(post("/api/v1/shop/item"), reqItemDto);
       }
    }

    private List<ReqCategoryDto> createTestReqCategoryDtos() {
        List<ReqCategoryDto> reqCategoryDtos = new ArrayList<ReqCategoryDto>();
        
        ReqCategoryDto reqCategoryDto1 = ReqCategoryDto.builder().name("WOMEN").build();
        ReqCategoryDto reqCategoryDto2 = ReqCategoryDto.builder().name("MEN").build();
        ReqCategoryDto reqCategoryDto3 = ReqCategoryDto.builder().name("KIDS").build();
        
        reqCategoryDtos.add(reqCategoryDto1);
        reqCategoryDtos.add(reqCategoryDto2);
        reqCategoryDtos.add(reqCategoryDto3);

        return reqCategoryDtos;
    }

    private List<ReqItemDto> createTestReqItemDtos() {
        List<ReqItemDto> reqItemDtos = new ArrayList<ReqItemDto>();
        
        ReqItemDto reqItemDto1 = ReqItemDto.builder().name("T-shirt").price(5000).remainQty(1000).categoryId(1L).build();
        ReqItemDto reqItemDto2 = ReqItemDto.builder().name("Y-shirt").price(4000).remainQty(500).categoryId(1L).build();
        ReqItemDto reqItemDto3 = ReqItemDto.builder().name("T-shirt").price(3000).remainQty(200).categoryId(2L).build();
        ReqItemDto reqItemDto4 = ReqItemDto.builder().name("T-shirt").price(2000).remainQty(200).categoryId(3L).build();

        reqItemDtos.add(reqItemDto1);
        reqItemDtos.add(reqItemDto2);
        reqItemDtos.add(reqItemDto3);
        reqItemDtos.add(reqItemDto4);

        return reqItemDtos;
    }
    
}
