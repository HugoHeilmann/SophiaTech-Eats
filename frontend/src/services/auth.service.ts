import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { environment } from '../environment/environment';
import { tap, catchError } from 'rxjs/operators';
import { of,map } from 'rxjs';

@Injectable({
    providedIn: 'root',
})
export class AuthService {

    private authStatus = new BehaviorSubject<boolean>(false);
    private customerId = new BehaviorSubject<number>(0);

    constructor(private http: HttpClient) { }


    public loginWithCredentials(email: string, password: string): Observable<any> {
        return this.http.post(`${environment.apiUrl}/auth/login`, { email, password }, { withCredentials: true }).pipe(
            map((response: any) => {
                this.customerId.next(response.id);
            }),
            tap(() => this.authStatus.next(true)),
            catchError(err => {
                console.error('Login failed:', err);
                throw err; // Re-throw for components to handle
            })
        );
    }

    public register(name: string, email: string, password: string): Observable<any> {
        return this.http.post(`${environment.apiUrl}/auth/register`, { name, email, password }, { withCredentials: true });
    }

    public loginWithGoogle(googleCredential: string): Observable<any> { 
        return this.http.post(`${environment.apiUrl}/auth/google-login`, { googleCredential }, { withCredentials: true }).pipe(
            tap(() => this.authStatus.next(true)),
            catchError(err => {
                console.error('Google login failed:', err);
                throw err;
            })
        );
    }

    public loginWithFacebook(facebookToken: string): Observable<any> {
        return this.http.post(`${environment.apiUrl}/auth/facebook-login`, { facebookToken }, { withCredentials: true }).pipe(
            tap(() => this.authStatus.next(true)),
            catchError(err => {
                console.error('Facebook login failed:', err);
                throw err;
            })
        );
    }

    public isLoggedIn(): Observable<boolean> {
        return this.authStatus.asObservable();
    }

    public getCustomerId(): Observable<number> {
        return this.customerId.asObservable();
    }

    public validateToken(): Observable<any> {
        return this.http.get(`${environment.apiUrl}/auth/validate-token`, { withCredentials: true }).pipe(
            map((response: any) => {
                this.customerId.next(response.id);
            }),
            tap(() => this.authStatus.next(true)),
            catchError(() => {
                this.authStatus.next(false);
                return of(null);
            })
        );
    }
    private getCookie(name: string): string | null {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) return parts.pop()?.split(';').shift() || null;
        return null;
    }

    public logout(): void {
        this.http.post(`${environment.apiUrl}/auth/logout`, {}, { withCredentials: true }).subscribe({
            complete: () => this.authStatus.next(false),
            error: err => console.error('Logout failed:', err),
        });
    }
}
