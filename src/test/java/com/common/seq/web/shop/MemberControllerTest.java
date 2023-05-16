package com.common.seq.web.shop;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.common.seq.data.dto.shop.ReqMemberDto;
import com.common.seq.web.shop.util.RequestAction;

@ActiveProfiles("dev")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) 
public class MemberControllerTest {
    
    @Autowired
    public RequestAction requestAction;

    @Test
    public void join_test() throws Exception{
        
        // 정상 data test
        ReqMemberDto reqMemberDto = ReqMemberDto.builder()
                                            .id("newMember")
                                            .address("한국")
                                            .phoneNumber("01111111111")
                                            .build();

        ResultActions resultAction = requestAction.doAction(post("/api/v1/shop/member"), reqMemberDto);

        resultAction.andExpect(status().isCreated())
                    .andExpect(content().string("SUCCESS"))
                    .andDo(MockMvcResultHandlers.print());

        // id 누락 test
        reqMemberDto = ReqMemberDto.builder()
                        //.id("newMember")
                        .address("한국")
                        .phoneNumber("01111111111")
                        .build();

        resultAction = requestAction.doAction(post("/api/v1/shop/member"), reqMemberDto);
        
        resultAction.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("id:must not be null"))
                    .andDo(MockMvcResultHandlers.print());


        // address 누락 test    
        reqMemberDto = ReqMemberDto.builder()
                        .id("newMember")
                        //.address("한국")
                        .phoneNumber("01111111111")
                        .build();

        resultAction = requestAction.doAction(post("/api/v1/shop/member"), reqMemberDto);
        
        resultAction.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("address:must not be null"))
                    .andDo(MockMvcResultHandlers.print());
            
    }



    @Test
    public void get_test() throws Exception{

        // member date insert
        setMemberList();

        // member id 조회 테스트
        ResultActions resultAction = requestAction.doAction(get("/api/v1/shop/member/member1"));
        
        resultAction.andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value("member1"))
                    .andExpect(jsonPath("$.address").value("한국"))
                    .andExpect(jsonPath("$.phoneNumber").value("01111111111"))
                    .andDo(MockMvcResultHandlers.print());


        // 전체 member 조회 테스트 
        resultAction = requestAction.doAction(get("/api/v1/shop/members"));

        resultAction.andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value("member1"))
                    .andExpect(jsonPath("$[1].id").value("member2"))
                    .andExpect(jsonPath("$[2].id").value("member3"))
                    .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void delete_update_test() throws Exception{
        
        // member date insert
        setMemberList();

        // member id 삭제 테스트
        ResultActions resultAction = requestAction.doAction(delete("/api/v1/shop/member/member1"));

        resultAction.andExpect(status().isOk())
                    .andExpect(content().string("SUCCESS"))
                    .andDo(MockMvcResultHandlers.print());


        // 삭제 후 전체 member 조회 테스트 
        resultAction = requestAction.doAction(get("/api/v1/shop/members"));


        resultAction.andExpect(status().isOk())
                    .andExpect(jsonPath("$[3]").doesNotExist())
                    .andDo(MockMvcResultHandlers.print());


        // member update test
        ReqMemberDto reqMemberDto = ReqMemberDto.builder()
                                            .id("member2")
                                            .address("미국")
                                            .phoneNumber("0222222222")
                                            .build();

        resultAction = requestAction.doAction(put("/api/v1/shop/member/member2"), reqMemberDto);
        

        resultAction.andExpect(status().isOk())
                    .andExpect(content().string("SUCCESS"))
                    .andDo(MockMvcResultHandlers.print());

        
        // update 후 조회
        resultAction = requestAction.doAction(get("/api/v1/shop/member/member2"));
        
        resultAction.andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value("member2"))
                    .andExpect(jsonPath("$.address").value("미국"))
                    .andExpect(jsonPath("$.phoneNumber").value("0222222222"))
                    .andDo(MockMvcResultHandlers.print());
    }

    private void setMemberList() throws Exception{
        
        List<ReqMemberDto> reqMemberDtos = createTestReqMemberDto();
        
        for(ReqMemberDto reqMemberDto : reqMemberDtos) {
            requestAction.doAction(post("/api/v1/shop/member"), reqMemberDto);
        }
        
    }

    private List<ReqMemberDto> createTestReqMemberDto() {
        List<ReqMemberDto> reqMemberDtos = new ArrayList<ReqMemberDto>();
        
        reqMemberDtos.add(ReqMemberDto.builder().id("member1").address("한국").phoneNumber("01111111111").build());
        reqMemberDtos.add(ReqMemberDto.builder().id("member2").address("한국").phoneNumber("01111111111").build());
        reqMemberDtos.add(ReqMemberDto.builder().id("member3").address("한국").phoneNumber("01111111111").build());

        return reqMemberDtos;
        
    }

    
}
