package com.springboot.isa.yt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.isa.yt.model.Comment;
import com.springboot.isa.yt.service.CommentService;

@RestController
public class CommentController {

	@Autowired
	private CommentService commentService;
	
	@GetMapping("/comments/{videoId}")
	public ResponseEntity<List<Comment>> getVideoComments(@PathVariable Long videoId){
		return ResponseEntity.ok(commentService.findByVideoUploadId(videoId));
	}
}
