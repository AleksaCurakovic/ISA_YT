import { Component, OnDestroy, OnInit } from '@angular/core';
import {
  ReactiveFormsModule,
  FormBuilder,
  Validators,
  AbstractControl,
  ValidationErrors,
  ValidatorFn,
  FormGroup
} from '@angular/forms';
import { UploadService } from '../service/upload-service';
import { ChangeDetectorRef } from '@angular/core';
import { HttpEventType, HttpErrorResponse } from '@angular/common/http';
import { Auth } from '../../infrastructure/service/auth';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';


function requiredFile(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null =>
    control.value instanceof File ? null : { requiredFile: true };
}

function maxFileSize(maxBytes: number): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const file = control.value as File | null;
    if (!(file instanceof File)) return null;
    return file.size <= maxBytes ? null : { maxFileSize: { maxBytes, actual: file.size } };
  };
}

function mimeTypes(allowed: string[]): ValidatorFn {
  const allowedSet = new Set(allowed.map(x => x.toLowerCase()));
  return (control: AbstractControl): ValidationErrors | null => {
    const file = control.value as File | null;
    if (!(file instanceof File)) return null;
    const t = (file.type || '').toLowerCase();
    return allowedSet.has(t) ? null : { mimeType: { allowed, actual: file.type } };
  };
}

function formatBytes(bytes: number): string {
  const units = ['B', 'KB', 'MB', 'GB'];
  let i = 0;
  let n = bytes;
  while (n >= 1024 && i < units.length - 1) {
    n /= 1024;
    i++;
  }
  return `${n.toFixed(i === 0 ? 0 : 1)} ${units[i]}`;
}

function getVideoDurationSeconds(file: File): Promise<number> {
  return new Promise((resolve, reject) => {
    const url = URL.createObjectURL(file);
    const video = document.createElement('video');
    video.preload = 'metadata';
    video.onloadedmetadata = () => {
      URL.revokeObjectURL(url);
      resolve(video.duration);
    };
    video.onerror = () => {
      URL.revokeObjectURL(url);
      reject(new Error('Failed to read video metadata'));
    };
    video.src = url;
  });
}

@Component({
  selector: 'app-video-upload',
  imports: [ReactiveFormsModule],
  templateUrl: './video-upload.html',
  styleUrl: './video-upload.css',
})
export class VideoUpload implements OnDestroy {
  videoForm: FormGroup;
  private readonly MAX_THUMB_BYTES = 4_194_304;      
  private readonly MAX_VIDEO_BYTES = 209_715_200;  
  thumbnailPreviewUrl: string | null = null;
  videoPreviewUrl: string | null = null;
  videoDurationSec: number | null = null;
  videoFileName = '—';
  videoFileSize = '—';
  uploadPct = 0;
  isUploading = false;
  uploadFinished = false;
  uploadError: string | null = null;
  loggedInUsername: string | null = null;
  
  constructor(private uploadService: UploadService, private formBuilder: FormBuilder, private cdr: ChangeDetectorRef,
    private authService: Auth, private toastr: ToastrService, private router: Router
  ) {
    this.videoForm = this.formBuilder.group({
      title: ['', [Validators.required, Validators.maxLength(100)]],
      description: ['', [Validators.required, Validators.maxLength(500)]],
      tags: ['', Validators.required],
      geoLocation: [''],
      thumbnailFile: [
        null as File | null,
        [requiredFile(), maxFileSize(this.MAX_THUMB_BYTES), mimeTypes(['image/jpeg'])],
    ],
      videoFile: [
        null as File | null,
        [requiredFile(), maxFileSize(this.MAX_VIDEO_BYTES), mimeTypes(['video/mp4'])],
    ]
    });
  }

    getDurationLabel(): string {
      if (this.videoDurationSec == null) return '—';
      const s = Math.floor(this.videoDurationSec);
      const m = Math.floor(s / 60);
      const r = s % 60;
      return `${m}:${String(r).padStart(2, '0')}`;
  }

  onThumbnailSelected(e: Event): void {
    const input = e.target as HTMLInputElement;
    const file = input.files?.[0] ?? null;

    this.videoForm.patchValue({ thumbnailFile: file });
    this.videoForm.get('thumbnailFile')!.markAsTouched();
    this.videoForm.get('thumbnailFile')!.updateValueAndValidity();

    if (this.thumbnailPreviewUrl) URL.revokeObjectURL(this.thumbnailPreviewUrl);
    this.thumbnailPreviewUrl = file ? URL.createObjectURL(file) : null;
  }

   async onVideoSelected(e: Event): Promise<void> {
    const input = e.target as HTMLInputElement;
    const file = input.files?.[0] ?? null;

    this.videoForm.patchValue({ videoFile: file });
    this.videoForm.get('videoFile')!.markAsTouched();
    this.videoForm.get('videoFile')!.updateValueAndValidity();

    if (this.videoPreviewUrl) URL.revokeObjectURL(this.videoPreviewUrl);
    this.videoPreviewUrl = file ? URL.createObjectURL(file) : null;

    // stats
    this.videoFileName = file?.name ?? '—';
    this.videoFileSize = file ? formatBytes(file.size) : '—';
    this.videoDurationSec = null;

    if (file) {
      try {
        this.videoDurationSec = await getVideoDurationSeconds(file);
      } catch {
        this.videoDurationSec = null;
      }
    }
    this.cdr.markForCheck();
  }

  uploadVideo(): void {
    if (this.videoForm.invalid) {
      this.videoForm.markAllAsTouched();
      return;
    }
    const rawFormat = this.videoForm.getRawValue();
    const payload = new FormData();
    payload.append('title', rawFormat.title);
    payload.append('author', this.loggedInUsername!);
    payload.append('description', rawFormat.description);
    payload.append('geoLocation', rawFormat.geoLocation ?? '');
    payload.append('tags', rawFormat.tags);
    payload.append('thumbnail', rawFormat.thumbnailFile!);
    payload.append('video', rawFormat.videoFile!);

    this.isUploading = true;
    this.uploadPct = 0;
    
    this.uploadService.uploadVideo(payload).subscribe({
        next: (event) => {
          if (event.type === HttpEventType.UploadProgress) {
            const total = event.total ?? 0;
            this.uploadPct = total ? Math.round((100 * event.loaded) / total) : 0;
            this.cdr.markForCheck();
          }

          if (event.type === HttpEventType.Response) {
            this.uploadPct = 100;
            this.isUploading = false;
            this.uploadFinished = true;
            this.cdr.markForCheck();
            console.log('Uploaded:', event.body);
            this.toastr.success('Video uploaded')
            this.router.navigate([''])
          }
            
        },
        error: (err: HttpErrorResponse) => {
          this.isUploading = false;
          this.uploadError =
            err.error?.message || (typeof err.error === 'string' ? err.error : 'Upload failed');
          this.toastr.error('Video upload failed')
          this.cdr.markForCheck();
        },
      });

  } 

  ngOnInit(): void {
    this.authService.authState$.subscribe(username  => {
      this.loggedInUsername = username;
    })
  }

  ngOnDestroy(): void {
    if (this.thumbnailPreviewUrl) {
      URL.revokeObjectURL(this.thumbnailPreviewUrl);
    }
    if (this.videoPreviewUrl) {
      URL.revokeObjectURL(this.videoPreviewUrl);
    }
  }

  resetForm(): void {
    this.videoForm.reset({
      title: '',
      description: '',
      tags: '',
      geoLocation: '',
      thumbnailFile: null,
      videoFile: null
    });

    // Reset file previews
    if (this.thumbnailPreviewUrl) {
      URL.revokeObjectURL(this.thumbnailPreviewUrl);
      this.thumbnailPreviewUrl = null;
    }

    if (this.videoPreviewUrl) {
      URL.revokeObjectURL(this.videoPreviewUrl);
      this.videoPreviewUrl = null;
    }

    // Reset UI metadata
    this.videoDurationSec = null;
    this.videoFileName = '—';
    this.videoFileSize = '—';
    this.uploadPct = 0;
    this.isUploading = false;
    this.uploadFinished = false;
    this.uploadError = null;

    // Clear validation state
    this.videoForm.markAsPristine();
    this.videoForm.markAsUntouched();
    this.toastr.info('Form reset')
  }

}
