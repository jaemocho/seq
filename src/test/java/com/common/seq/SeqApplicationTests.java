package com.common.seq;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.common.seq.data.dto.ReqGUID;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;


@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK) 
class SeqApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void createGUID_test() throws Exception{

		ReqGUID reqGUID = ReqGUID.builder()
						.fromServer("a01")
						.guid("")
						.build();
		
		
		String content = new ObjectMapper().writeValueAsString(reqGUID);
		
		ResultActions resultAction = mockMvc.perform(post("/api/v1/seq/guid")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_VALUE));

		resultAction
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.[?(@.guid =~ /a01xxxxxxxxx1.*?/i)]").exists())  // 뒤에 17자리가 random 이라 앞에 13자리만 비교 
		.andDo(MockMvcResultHandlers.print());


		reqGUID.setGuid("a01xxxxxxxxx1xxxxxxxxxxxxxxxxx");
		reqGUID.setFromServer("b01");
		
		content = new ObjectMapper().writeValueAsString(reqGUID);
		
		resultAction = mockMvc.perform(post("/api/v1/seq/guid")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_VALUE));

		resultAction
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.[?(@.guid =~ /a01b01xxxxxx2.*?/i)]").exists())  // 뒤에 17자리가 random 이라 앞에 13자리만 비교 
		.andDo(MockMvcResultHandlers.print());


	}

	@Test
	public void getSEQ_test() throws Exception{

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date = formatter.format(new Date(System.currentTimeMillis()));

		ResultActions resultAction = mockMvc.perform(get("/api/v1/seq/sequence")
				.accept(MediaType.APPLICATION_JSON_VALUE));

		resultAction
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.seq").value(0))
		.andExpect(jsonPath("$.date").value(date))
		.andDo(MockMvcResultHandlers.print());	
		
	}


	@Test
	public void increseSEQ_test() throws Exception{

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date = formatter.format(new Date(System.currentTimeMillis()));

		ResultActions resultAction = mockMvc.perform(post("/api/v1/seq/sequence")
				.accept(MediaType.APPLICATION_JSON_VALUE));

		resultAction
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.seq").value(1))
		.andExpect(jsonPath("$.date").value(date))
		.andDo(MockMvcResultHandlers.print());	


		resultAction = mockMvc.perform(post("/api/v1/seq/sequence")
				.accept(MediaType.APPLICATION_JSON_VALUE));

		resultAction
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.seq").value(2))
		.andExpect(jsonPath("$.date").value(date))
		.andDo(MockMvcResultHandlers.print());	


		for (int i = 3; i < 100; i++) {
			resultAction = mockMvc.perform(post("/api/v1/seq/sequence")
				.accept(MediaType.APPLICATION_JSON_VALUE));

			resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.seq").value(i))
			.andExpect(jsonPath("$.date").value(date))
			.andDo(MockMvcResultHandlers.print());	
		}
	}

}
