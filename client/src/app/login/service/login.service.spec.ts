import { inject, TestBed } from '@angular/core/testing';

import { LoginService } from './login.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { SessionService } from '../../core/session/session.service';
import { SessionUserService } from '../../core/session/session-user.service';
import { HttpClient } from '@angular/common/http';

describe('LoginService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        LoginService,
        {provide: HttpClient, useClass: HttpClientTestingModule},
        { provide: SessionService, useValue: {}},
        { provide: SessionUserService, useValue: {}}
        ]
    });
  });

  it('should be created', inject([LoginService], (service: LoginService) => {
    expect(service).toBeTruthy();
  }));
});
