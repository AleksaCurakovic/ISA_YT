import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VideoUpload } from '../../model/videoUpload';
import { CommentService } from '../service/comment-service';
import { UploadService } from '../../upload/service/upload-service';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { Comment } from '../../model/comment';
import { Auth } from '../../infrastructure/service/auth'
import { VideoPreview } from '../../model/videoPreview';
import { ToastrService } from 'ngx-toastr';


@Component({
  selector: 'app-video-play',
  imports: [CommonModule, RouterModule],
  templateUrl: './video-play.html',
  styleUrl: './video-play.css',
})
export class VideoPlay implements OnInit {
  video = signal<VideoUpload | null>(null)
  loggedInUsername: string | null = null;
  suggestedVideos = signal<VideoPreview[]>([]);
  comments =  signal<Comment[]>([]);
  currentCommentPage = 0;
  commentPageSize = 10;

  constructor(private commentService: CommentService, private uploadService: UploadService,
              private route: ActivatedRoute, private authService: Auth, private router: Router,
              private toastr: ToastrService
  ){}

  ngOnInit(): void{
    this.authService.authState$.subscribe(username  => {
      this.loggedInUsername = username;
    })
    this.route.queryParams.subscribe(params => {
    const videoId = params['videoId'];
    this.uploadService.getAllUploads().subscribe(videos => {
      this.suggestedVideos.set(videos.filter(video => video.id != videoId))
    })
    if (videoId) {
      this.uploadService.getUpload(videoId).subscribe(video => {
        this.video.set(video)
      }, error => {
        console.error('Failed to load video', error);
      });
      this.commentService.getVideoComments(videoId, this.currentCommentPage, this.commentPageSize).subscribe(comments => {
        this.comments.set(comments);
      }, error => {
        console.error('Failed to load video', error);
      });
    }
  });
  }

  nextcCommentsPage(page: number): void {
    this.currentCommentPage += 1;
    this.commentService.getVideoComments(this.video()!.id, this.currentCommentPage, this.commentPageSize).subscribe(comments => {
            this.comments.set(comments);
          }, error => {
            console.error('Failed to load video', error);
    });
  }

  likeVideo(): void {
    if (!this.loggedInUsername)
    {
      this.toastr.error('Please login first to like')
      this.router.navigate(['/login'])
    }
    //Like logic
  }

  commentVideo(): void {
     if (!this.loggedInUsername)
    {
      this.toastr.error('Please login first to comment')
      this.router.navigate(['/login'])
    }
    //Comment logic
  }

  playSuggestedVideo(id: number): void {
    this.router.navigate(['/play'], { queryParams: { videoId: id } })
  }

  getDurationLabel(seconds: number): string {
    if (!seconds || seconds <= 0) return "0:00";

    const total = Math.floor(seconds);
    const h = Math.floor(total / 3600);
    const m = Math.floor((total % 3600) / 60);
    const s = total % 60;

    if (h > 0) {
      return `${h}:${m.toString().padStart(2, "0")}:${s.toString().padStart(2, "0")}`;
    }

    return `${m}:${s.toString().padStart(2, "0")}`;
  }


}
