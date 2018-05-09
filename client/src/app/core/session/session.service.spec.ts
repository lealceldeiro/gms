import { inject, TestBed } from '@angular/core/testing';

import { SessionService } from './session.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { StorageService } from '../storage/storage.service';

describe('SessionService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionService, { provide: StorageService, useValue: {} }]
    });
  });

  it('should be created', inject([SessionService], (service: SessionService) => {
    expect(service).toBeTruthy();
  }));
});
