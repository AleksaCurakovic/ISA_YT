import { Routes } from '@angular/router';
import { Login } from './infrastructure/login/login';
import { Register } from './infrastructure/register/register';
import { Home } from './home/home';

export const routes: Routes = [
    { path: 'login', component: Login },
    { path: 'register', component: Register },
    { path: '', component: Home },
];
