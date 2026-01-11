package com.springboot.isa.yt.dto;

import java.util.Date;

import com.springboot.isa.yt.model.VideoUpload;

public class VideoPreviewDTO {
	private Long id;
	private String title;
	private String author;
	private String tags;
	private String thumbnailUrl;
	private Date createdAt;
	private int duration;
	
	public VideoPreviewDTO(VideoUpload videoUpload) {
		this.id = videoUpload.getId();
		this.title = videoUpload.getTitle();
		this.author = videoUpload.getAuthor();
		this.tags = videoUpload.getTags();
		this.thumbnailUrl = videoUpload.getThumbnailUrl();
		this.createdAt = videoUpload.getCreatedAt();
		this.duration = videoUpload.getDuration();
	}
	
	
	public int getDuration() {
		return duration;
	}


	public void setDuration(int duration) {
		this.duration = duration;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

}
