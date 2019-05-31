import { TestBed } from '@angular/core/testing';

import { AllRequestsInterceptor } from './all-requests.interceptor.service';
import { HTTP_INTERCEPTORS } from '@angular/common/http';

describe('AllRequestsInterceptor', () => {
    beforeEach(() => TestBed.configureTestingModule({
        providers: [
            AllRequestsInterceptor,
            { provide: HTTP_INTERCEPTORS, useClass: AllRequestsInterceptor, multi: true },
        ]
    }));

    it('should be created', () => {
        const service: AllRequestsInterceptor = TestBed.get(AllRequestsInterceptor);
        expect(service).toBeTruthy();
    });
});
