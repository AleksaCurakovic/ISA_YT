package com.springboot.isa.yt;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.isa.yt.controller.AuthenticationContoller;
import com.springboot.isa.yt.dto.JwtAuthenticationRequestDTO;
import com.springboot.isa.yt.model.Comment;
import com.springboot.isa.yt.service.CommentService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {

	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @MockitoBean
    private CommentService commentService;
    
    @Autowired
    private AuthenticationContoller authController;

    @Test
    void testRateLimit() throws Exception {
    	
    	JwtAuthenticationRequestDTO authRequest = new JwtAuthenticationRequestDTO("aleksa02", "123456");
    	HttpServletRequest request = mock(HttpServletRequest.class);
    	HttpServletResponse response = mock(HttpServletResponse.class);
    	var authResponse = authController.createAuthenticationToken(authRequest, response, request);
    	Mockito.when(commentService.save(any(Comment.class)))
        .thenReturn(new Comment());
        Comment comment = new Comment();
        comment.setAuthorUsername("BOB");
        String commentJson = objectMapper.writeValueAsString(comment);

        for (int i = 0; i < 60; i++) {
        	mockMvc.perform(
	                MockMvcRequestBuilders.request(HttpMethod.POST, "/comment")
	                .header("Authorization", "Bearer " + authResponse.getBody().getAccessToken())
	                        .contentType(MediaType.APPLICATION_JSON)
	                        .content(commentJson)).andDo(print());
        }

        
        mockMvc.perform(
	            MockMvcRequestBuilders.request(HttpMethod.POST, "/comment")
	            .header("Authorization", "Bearer " + authResponse.getBody().getAccessToken())
	                    .contentType(MediaType.APPLICATION_JSON)
	                    .content(commentJson)
	    ).andDo(print()).andExpect(status().isTooManyRequests());
    }
}
