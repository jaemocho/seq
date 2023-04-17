package com.common.seq.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.CoreMatchers.containsString;

import com.common.seq.web.auth.WithAuthUser;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK) 
@WithAuthUser(email = "test@gmail.com", role = "ROLE_USER") 
public class ShortenUrlControllerTest {
    
    @Autowired
	private MockMvc mockMvc;

    @Test
	public void getShortenUrl_test() throws Exception{

		String content = "www.naver.com";
		
		ResultActions resultAction = mockMvc.perform(get("/api/v1/shorten-url/")
                .param("originUrl", content)
				.contentType(MediaType.TEXT_PLAIN_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE));

		resultAction
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.orgUrl").value("www.naver.com"))
        .andExpect(jsonPath("$.shortenUrl", containsString("https://me2.do")))
		
		.andDo(MockMvcResultHandlers.print());

    }
}