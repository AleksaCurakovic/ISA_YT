import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VideoUpload } from '../../model/videoUpload'

@Component({
  selector: 'app-video-play',
  imports: [CommonModule],
  templateUrl: './video-play.html',
  styleUrl: './video-play.css',
})
export class VideoPlay {
  video: VideoUpload = { id: 1,
  title: 'Building a YouTube-Style Video Platform with Angular & Spring Boot',
  description: `In this video we walk through the complete architecture of a modern
YouTube-like platform using Angular on the frontend and Spring Boot on the backend.
We cover video streaming, thumbnails, metadata, and performance tips.`,
  author: 'Aleksa Curakovic',
  tags: 'angular,spring-boot,video-streaming,web-development',
  thumbnailUrl: 'https://picsum.photos/640/360',
  videoUrl: 'https://www.w3schools.com/html/mov_bbb.mp4',
  createdAt: new Date('2025-01-10T14:32:00'),
  geoLocation: 'Belgrade, Serbia'}

  suggestedVideos: VideoUpload[] = [];

  comments = [
    { author: 'Alex', text: 'Great video!', time: '2 hours ago' },
    { author: 'Maria', text: 'This helped a lot 👍', time: '1 day ago' }
  ];
}
