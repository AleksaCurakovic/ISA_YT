import { Routes } from '@angular/router';
import { Login } from './infrastructure/login/login';
import { Register } from './infrastructure/register/register';
import { Home } from './home/home';
import { VideoUpload } from './upload/video-upload/video-upload';
import { VideoPlay } from './play/video-play/video-play'

export const routes: Routes = [
    { path: 'login', component: Login },
    { path: 'register', component: Register },
    { path: 'upload', component: VideoUpload },
    { path: '', component: Home },
    { path: 'play', component: VideoPlay}
];
