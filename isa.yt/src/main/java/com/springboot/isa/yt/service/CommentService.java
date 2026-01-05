package com.springboot.isa.yt.service;

import java.util.List;

import com.springboot.isa.yt.model.Comment;

public interface CommentService {
	List<Comment> findByVideoUploadId(Long id);
	Comment save(Comment comment);
	
}
