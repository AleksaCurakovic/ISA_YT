package com.springboot.isa.yt;

import static org.mockito.ArgumentMatchers.any;
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
import com.springboot.isa.yt.model.Comment;
import com.springboot.isa.yt.service.CommentService;


@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {

	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @MockitoBean
    private CommentService commentService;

    @Test
    void testRateLimit() throws Exception {
    	Mockito.when(commentService.save(any(Comment.class)))
        .thenReturn(new Comment());
        Comment comment = new Comment();
        comment.setAuthorUsername("BOB");
        String commentJson = objectMapper.writeValueAsString(comment);

        for (int i = 0; i < 60; i++) {
        	mockMvc.perform(
	                MockMvcRequestBuilders.request(HttpMethod.POST, "/comment")
	                        .contentType(MediaType.APPLICATION_JSON)
	                        .content(commentJson)).andDo(print());
        }

        
        mockMvc.perform(
	            MockMvcRequestBuilders.request(HttpMethod.POST, "/comment")
	                    .contentType(MediaType.APPLICATION_JSON)
	                    .content(commentJson)
	    ).andDo(print()).andExpect(status().isTooManyRequests());
    }
}
