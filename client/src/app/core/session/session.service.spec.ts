import { inject, TestBed } from '@angular/core/testing';

import { SessionService } from './session.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { StorageService } from '../storage/storage.service';
import { BehaviorSubject } from 'rxjs/index';

describe('SessionService', () => {
  function dummyFn() { return new BehaviorSubject<string>('test').asObservable(); }
  const storageServiceStub = {
    set: dummyFn,
    get: dummyFn,
    clear: dummyFn,
    putCookie: dummyFn,
    getCookie: dummyFn,
    clearCookie: dummyFn,
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionService, { provide: StorageService, useValue: storageServiceStub }]
    });
  });

  it('should be created', inject([SessionService], (service: SessionService) => {
    expect(service).toBeTruthy();
  }));
});
