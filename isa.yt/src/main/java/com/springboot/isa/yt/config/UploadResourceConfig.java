package com.springboot.isa.yt.config;

import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class UploadResourceConfig implements WebMvcConfigurer{

	 private final String rootDir;

	 	@Autowired
	    public UploadResourceConfig(@Value("${app.storage.root:uploads}") String rootDir) {
	        this.rootDir = rootDir;
	    }

	    @Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	        String location = Paths.get(rootDir).toAbsolutePath().toUri().toString(); 
	        registry.addResourceHandler("/upload/videos/**")
	                .addResourceLocations(location);
	    }
}
