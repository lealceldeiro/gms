import { fakeAsync, inject, TestBed, tick } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient } from '@angular/common/http';
import { Subject } from 'rxjs';
import { environment } from '../../../environments/environment';

import { LoginService } from './login.service';
import { SessionService } from '../../core/session/session.service';
import { SessionUserService } from '../../core/session/session-user.service';
import { LoginResponseModel } from '../../core/session/login-response.model';
import { UserPdModel } from '../../core/response/paginated-data/impl/user-pd-.model';
import { PageModel } from '../../core/response/paginated-data/page.model';
import { LinksModel } from '../../core/response/paginated-data/links.model';
import { SelfModel } from '../../core/response/paginated-data/self.model';
import { userMock } from '../../core/session/user.mock.model';
import { InterceptorHelperService } from '../../core/interceptor/interceptor-helper.service';

describe('LoginService', () => {
  let httpClient: HttpClient;
  let httpTestingController: HttpTestingController;
  let sessionUserService: SessionUserService;
  let sessionService: SessionService;
  let loginService: LoginService;
  const api = environment.apiBaseUrl;
  const url = environment.apiLoginUrl;

  let spyAddEFEH: jasmine.Spy;

  const spy = {
    sus: { getCurrentUser: (a) => {} },
    ss: { setUser: (a) => {}, setAuthData: (a) => {}, setLoggedIn: (a) => {} }
  };
  const sessionServiceStub = {
    setAuthData : (a) => { spy.ss.setAuthData(a); },
    setLoggedIn: (a) => { spy.ss.setLoggedIn(a); },
    setUser: (a) => { spy.ss.setUser(a); return new Subject().asObservable(); }
  };
  const intHelperServiceStub = { addExcludedFromErrorHandling: () => {} };

  // region user paginated data mock
  const self: SelfModel = { href: 'test' };
  const linkM: LinksModel = { self: self };
  const page: PageModel = { totalPages: 1, number: 1, size: 1, totalElements: 1 };
  const value = { page: page, _links: linkM, _embedded: { user: [ userMock ] } };
  const userModelSubject = new Subject<UserPdModel>();
  // endregion

  const sessionUserServiceStub = {
    getCurrentUser: (a) => {
      spy.sus.getCurrentUser(a);
      return userModelSubject.asObservable();
    }
  };

  beforeEach(() => {
    spyAddEFEH = spyOn(intHelperServiceStub, 'addExcludedFromErrorHandling');
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        LoginService, { provide: SessionService, useValue: sessionServiceStub },
        { provide: SessionUserService, useValue: sessionUserServiceStub },
        { provide: InterceptorHelperService, useValue: intHelperServiceStub }
      ]
    });
    httpClient = TestBed.get(HttpClient);
    httpTestingController = TestBed.get(HttpTestingController);
    sessionUserService = TestBed.get(SessionUserService);
    sessionService = TestBed.get(SessionService);
    loginService = TestBed.get(LoginService);
  });

  afterEach(() => {
    // After every test, assert that there are no more pending requests.
    httpTestingController.verify();
  });

  it('should be created', inject([LoginService], (service: LoginService) => {
    expect(service).toBeTruthy();
  }));

  it('should call InterceptorHelperService#addExcludedFromErrorHandling on init', () => {
    expect(spyAddEFEH).toHaveBeenCalledTimes(1);
  });

  it('should post a `LoginRequestModel` payload to the login url and, if it succeeds, set the auth data  in ' +
    'SessionService and call the getUserMethod in the SessionUserService in order to retrieve the current User info',
    fakeAsync(() => {
        // region mocks and spies
        const setAuthDataSpy = spyOn(spy.ss, 'setAuthData');
        const setLoggedInSpy = spyOn(spy.ss, 'setLoggedIn');
        const setUserSpy = spyOn(spy.ss, 'setUser');
        const getCurrentUserSpy = spyOn(spy.sus, 'getCurrentUser');
        const body = { usernameOrEmail: userMock.username, password: 'test' };
        const response: LoginResponseModel = { access_token: 'access_tokenS', username: userMock.username };
        // endregion

        // region actual call
        loginService.login(body).toPromise().then( (data: LoginResponseModel) => {
          expect(data).toBeTruthy('data is not ok');
          expect(data.access_token).toBeTruthy('access token is not ok');
          expect(setAuthDataSpy).toHaveBeenCalledTimes(1);
          expect(setLoggedInSpy).toHaveBeenCalledTimes(1);
          expect(getCurrentUserSpy).toHaveBeenCalledTimes(1);
          expect(getCurrentUserSpy.calls.first().args[0]).toEqual(userMock.username, 'username used for calling ' +
            'the `getCurrentUser` service is not the same as the one which came in the response');
          expect(setUserSpy).toHaveBeenCalledTimes(1);
          expect(setUserSpy.calls.first().args[0]).toEqual(userMock, 'sessionUserService not ' +
            'called with the correct username');
        });
        // endregion

        // request mock
        const req = httpTestingController.match(r => r.url === api + url && r.method === 'POST' && r.body === body);
        expect(req.length).toBeGreaterThan(0, 'no request was found');
        req[0].flush(response);

        userModelSubject.next(value);

        tick();
      }
    ));
});
