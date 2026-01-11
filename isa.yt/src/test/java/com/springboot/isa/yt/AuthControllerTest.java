package com.springboot.isa.yt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.isa.yt.dto.JwtAuthenticationRequestDTO;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
	
	@Autowired
    private ObjectMapper objectMapper;
	
	@Autowired
	private MockMvc mockMvc;
	

	@Test
	void test_login_rate_limit() throws Exception
	{
		JwtAuthenticationRequestDTO request = new JwtAuthenticationRequestDTO("aleksa02", "123456");
		String json = objectMapper.writeValueAsString(request);


		for (int i = 1; i <= 5; i++) {
	        mockMvc.perform(
	                MockMvcRequestBuilders.request(HttpMethod.POST, "/login")
	                        .contentType(MediaType.APPLICATION_JSON)
	                        .content(json)).andDo(print());
	    }

	    // 6th request must be blocked
	    mockMvc.perform(
	            MockMvcRequestBuilders.request(HttpMethod.POST, "/login")
	                    .contentType(MediaType.APPLICATION_JSON)
	                    .content(json)
	    ).andDo(print()).andExpect(status().isTooManyRequests());
	}
}
