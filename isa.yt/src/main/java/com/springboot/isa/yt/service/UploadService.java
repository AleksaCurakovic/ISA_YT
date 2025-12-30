package com.springboot.isa.yt.service;

import java.util.List;

import com.springboot.isa.yt.dto.UploadRequestDTO;
import com.springboot.isa.yt.model.VideoUpload;

public interface UploadService {
	VideoUpload save(UploadRequestDTO uploadRequest);
	VideoUpload findByAuthor(String author);
	List<VideoUpload> findAll();
	byte[] getThumbnail(String filname);
}
