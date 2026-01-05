package com.springboot.isa.yt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.isa.yt.model.Comment;

public interface CommentRepository extends JpaRepository<Comment,Long>{
	List<Comment> findByVideoUploadId(Long id);
}
