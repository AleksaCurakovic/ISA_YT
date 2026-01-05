package com.springboot.isa.yt.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.isa.yt.model.Comment;
import com.springboot.isa.yt.repository.CommentRepository;
import com.springboot.isa.yt.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {
	
	@Autowired
	private CommentRepository commentRepository;

	@Override
	public List<Comment> findByVideoUploadId(Long id) {
		return  commentRepository.findByVideoUploadId(id);
	}

	@Override
	public Comment save(Comment comment) {
		return commentRepository.save(comment);
	}

}
