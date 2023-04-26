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
import com.common.seq.data.dto.shop.ReqMemberDto;
import com.common.seq.data.dto.shop.ReqOrderDto;
import com.common.seq.web.shop.util.ControllerTestUtils;
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
public class OrderControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    private void setInitData() throws Exception {
        
        // member insert
        List<ReqMemberDto> reqMemberDtos = new ArrayList<ReqMemberDto>();

        ReqMemberDto reqMemberDto1 = ReqMemberDto.builder().id("memberA").address("서울").phoneNumber("01111111111").build();
        ReqMemberDto reqMemberDto2 = ReqMemberDto.builder().id("memberB").address("서울").phoneNumber("01111111111").build();

        reqMemberDtos.add(reqMemberDto1);
        reqMemberDtos.add(reqMemberDto2);

        
        ControllerTestUtils.<ReqMemberDto>requestInitDataSimplePost(mockMvc, reqMemberDtos, "/api/v1/shop/member");

        // category insert 
        List<ReqCategoryDto> reqCategoryDtos = new ArrayList<ReqCategoryDto>();
        
        ReqCategoryDto reqCategoryDto1 = ReqCategoryDto.builder().name("WOMEN").build();
        ReqCategoryDto reqCategoryDto2 = ReqCategoryDto.builder().name("MEN").build();
        ReqCategoryDto reqCategoryDto3 = ReqCategoryDto.builder().name("KIDS").build();
        
        reqCategoryDtos.add(reqCategoryDto1);
        reqCategoryDtos.add(reqCategoryDto2);
        reqCategoryDtos.add(reqCategoryDto3);

        ControllerTestUtils.<ReqCategoryDto>requestInitDataSimplePost(mockMvc, reqCategoryDtos, "/api/v1/shop/category");

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


        ControllerTestUtils.<ReqItemDto>requestInitDataSimplePost(mockMvc, reqItemDtos, "/api/v1/shop/item");
    }

    @Test
    public void order_test() throws Exception {

        // category data insert
        // category 3개 
        // item 4개 입력 
        setInitData() ;

        ResultActions resultAction;
        String content;
        ReqOrderDto reqOrderDto;
        
        
        // 신규 order test 
        // 주문자 : memberA
        // items 2종류 
        ReqOrderDto.RequestItem requestItem1 
                        = ReqOrderDto.RequestItem.builder()
                                            .itemId(1L)
                                            .requestQty(30)
                                            .build();

        ReqOrderDto.RequestItem requestItem2 
                        = ReqOrderDto.RequestItem.builder()
                                            .itemId(2L)
                                            .requestQty(50)
                                            .build();

        List<ReqOrderDto.RequestItem> requestItems = 
                new ArrayList<ReqOrderDto.RequestItem>();
        requestItems.add(requestItem1);
        requestItems.add(requestItem2);
                                                                    
        
        reqOrderDto = ReqOrderDto.builder()
                                .memberId("memberA")
                                .requestItem(requestItems)
                                .build();
                                
        content = new ObjectMapper().writeValueAsString(reqOrderDto);

        resultAction = mockMvc.perform(post("/api/v1/shop/order")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(content)
                                .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(status().isCreated())
                    .andExpect(content().string("1"))
                    .andDo(MockMvcResultHandlers.print());


        // 주문 member id null test 
        reqOrderDto = ReqOrderDto.builder()
                                // .memberId("memberA")
                                .requestItem(requestItems)
                                .build();
                                
        content = new ObjectMapper().writeValueAsString(reqOrderDto);

        resultAction = mockMvc.perform(post("/api/v1/shop/order")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(content)
                                .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("memberId:must not be null"))
                    .andDo(MockMvcResultHandlers.print());
                    
        
        // 주문  requestItems null test 
        reqOrderDto = ReqOrderDto.builder()
                                .memberId("memberA")
                                //.requestItem(requestItems)
                                .build();
                                
        content = new ObjectMapper().writeValueAsString(reqOrderDto);

        resultAction = mockMvc.perform(post("/api/v1/shop/order")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(content)
                                .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("requestItem:must not be null"))
                    .andDo(MockMvcResultHandlers.print());                                           

        // id 1 번 조회 test
        resultAction = mockMvc.perform(get("/api/v1/shop/order/1")
                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                        .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(status().isOk())
                    .andExpect(jsonPath("$.memberId").value("memberA"))
                    .andExpect(jsonPath("$.orderItemInfos[0].itemId").value(1))
                    .andExpect(jsonPath("$.orderItemInfos[0].itemName").value("T-shirt"))
                    .andExpect(jsonPath("$.orderItemInfos[0].itemRequestQty").value(30))
                    .andExpect(jsonPath("$.orderItemInfos[0].categoryId").value(1))
                    .andExpect(jsonPath("$.orderItemInfos[0].categoryName").value("WOMEN"))
                    .andExpect(jsonPath("$.orderItemInfos[1].itemId").value(2))
                    .andExpect(jsonPath("$.orderItemInfos[1].itemName").value("Y-shirt"))
                    .andExpect(jsonPath("$.orderItemInfos[1].itemRequestQty").value(50))
                    .andExpect(jsonPath("$.orderItemInfos[1].categoryId").value(1))
                    .andExpect(jsonPath("$.orderItemInfos[1].categoryName").value("WOMEN"))
                    .andExpect(jsonPath("$.orderState").value("REQUEST"))
                    .andDo(MockMvcResultHandlers.print());
        
        // memberA 주문 추가 
        reqOrderDto = ReqOrderDto.builder()
                                .memberId("memberA")
                                .requestItem(requestItems)
                                .build();
                                
        content = new ObjectMapper().writeValueAsString(reqOrderDto);

        resultAction = mockMvc.perform(post("/api/v1/shop/order")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(content)
                                .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(status().isCreated())
                    .andExpect(content().string("2"))
                    .andDo(MockMvcResultHandlers.print());


        // memberid로 조회 테스트 
        resultAction = mockMvc.perform(get("/api/v1/shop/order/member/memberA")
                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                        .accept(MediaType.APPLICATION_JSON_VALUE));
        
        // model and view 로 해서 확인하는 법으로 교체를 ...
        resultAction.andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].memberId").value("memberA"))
                    .andExpect(jsonPath("$[0].orderItemInfos[0].itemId").value(1))
                    .andExpect(jsonPath("$[0].orderItemInfos[0].itemName").value("T-shirt"))
                    .andExpect(jsonPath("$[0].orderItemInfos[0].itemRequestQty").value(30))
                    .andExpect(jsonPath("$[0].orderItemInfos[0].categoryId").value(1))
                    .andExpect(jsonPath("$[0].orderItemInfos[0].categoryName").value("WOMEN"))
                    .andExpect(jsonPath("$[0].orderItemInfos[1].itemId").value(2))
                    .andExpect(jsonPath("$[0].orderItemInfos[1].itemName").value("Y-shirt"))
                    .andExpect(jsonPath("$[0].orderItemInfos[1].itemRequestQty").value(50))
                    .andExpect(jsonPath("$[0].orderItemInfos[1].categoryId").value(1))
                    .andExpect(jsonPath("$[0].orderItemInfos[1].categoryName").value("WOMEN"))
                    .andExpect(jsonPath("$[0].orderState").value("REQUEST"))
                    .andExpect(jsonPath("$[1].memberId").value("memberA"))
                    .andExpect(jsonPath("$[1].orderItemInfos[0].itemId").value(1))
                    .andExpect(jsonPath("$[1].orderItemInfos[0].itemName").value("T-shirt"))
                    .andExpect(jsonPath("$[1].orderItemInfos[0].itemRequestQty").value(30))
                    .andExpect(jsonPath("$[1].orderItemInfos[0].categoryId").value(1))
                    .andExpect(jsonPath("$[1].orderItemInfos[0].categoryName").value("WOMEN"))
                    .andExpect(jsonPath("$[1].orderItemInfos[1].itemId").value(2))
                    .andExpect(jsonPath("$[1].orderItemInfos[1].itemName").value("Y-shirt"))
                    .andExpect(jsonPath("$[1].orderItemInfos[1].itemRequestQty").value(50))
                    .andExpect(jsonPath("$[1].orderItemInfos[1].categoryId").value(1))
                    .andExpect(jsonPath("$[1].orderItemInfos[1].categoryName").value("WOMEN"))
                    .andExpect(jsonPath("$[1].orderState").value("REQUEST"))
                    .andExpect(jsonPath("$[2]").doesNotExist())
                    .andDo(MockMvcResultHandlers.print());

        // 주문 취소 테스트 (1번 주문)
        resultAction = mockMvc.perform(delete("/api/v1/shop/order/1")
                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                        .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(status().isOk())
                    .andExpect(content().string("SUCCESS"))
                    .andDo(MockMvcResultHandlers.print());                                        
                    
        
        // id 1 번 조회 test (주문 취소 후 상태 변경 확인)
        resultAction = mockMvc.perform(get("/api/v1/shop/order/1")
                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                        .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(status().isOk())
                    .andExpect(jsonPath("$.memberId").value("memberA"))
                    .andExpect(jsonPath("$.orderState").value("CANCEL"))
                    .andDo(MockMvcResultHandlers.print());
        
    }
}
