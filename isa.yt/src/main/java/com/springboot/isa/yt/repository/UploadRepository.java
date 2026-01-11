package com.springboot.isa.yt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.isa.yt.model.VideoUpload;

public interface UploadRepository extends JpaRepository<VideoUpload, Long>{
	VideoUpload findByAuthor(String author);
	List<VideoUpload> findAll();
	List<VideoUpload> findAllByAuthor(String author);
}
