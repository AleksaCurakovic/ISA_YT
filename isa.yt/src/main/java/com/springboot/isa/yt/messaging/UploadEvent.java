package com.springboot.isa.yt.messaging;

import java.util.Date;

import com.springboot.isa.yt.model.VideoUpload;


public class UploadEvent {
    private String title;
    private String author;
    private String description;
    private String tags;
    private Date createdAt;
    private int views;
    
    public UploadEvent(VideoUpload videoUpload) {
    	this.title = videoUpload.getTitle();
    	this.author = videoUpload.getAuthor();
    	this.description = videoUpload.getDescription();
    	this.tags = videoUpload.getTags();
    	this.createdAt = videoUpload.getCreatedAt();
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public int getViews() {
		return views;
	}
	public void setViews(int views) {
		this.views = views;
	}

}
