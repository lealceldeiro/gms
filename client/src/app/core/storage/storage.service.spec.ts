import { inject, TestBed } from '@angular/core/testing';

import { StorageService } from './storage.service';
import { CookieService } from 'ngx-cookie';

describe('StorageService', () => {
  const cookiesMock = {};
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [StorageService, { provide: CookieService, useValue: cookiesMock }]
    });
  });

  it('should be created', inject([StorageService], (service: StorageService) => {
    expect(service).toBeTruthy();
  }));
});
