import { inject, TestBed } from '@angular/core/testing';

import { SessionService } from './session.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { StorageService } from '../storage/storage.service';
import { BehaviorSubject, Observable } from 'rxjs/index';

describe('SessionService', () => {
  function dummyFn() { return Observable.create(new BehaviorSubject<string>('test')); }
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
