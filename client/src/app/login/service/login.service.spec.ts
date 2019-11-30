import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { fakeAsync, TestBed, tick } from '@angular/core/testing';

import { Subject } from 'rxjs';

import { AppConfig } from '../../core/config/app.config';
import { InterceptorHelperService } from '../../core/interceptor/interceptor-helper.service';
import { UserPdModel } from '../../core/response/paginated-data/impl/user-pd-.model';
import { LinksModel } from '../../core/response/paginated-data/links.model';
import { PageModel } from '../../core/response/paginated-data/page.model';
import { SelfModel } from '../../core/response/paginated-data/self.model';
import { LoginResponseModel } from '../../core/session/login-response.model';
import { SessionUserService } from '../../core/session/session-user.service';
import { SessionService } from '../../core/session/session.service';
import { userMock } from '../../core/session/user.mock.model';
import { MockAppConfig } from '../../shared/test-util/mock/app.config';
import { LoginService } from './login.service';

describe('LoginService', () => {
  // region user paginated data mock
  const self: SelfModel = { href: 'test' };
  const linkM: LinksModel = { self };
  const page: PageModel = { totalPages: 1, number: 1, size: 1, totalElements: 1 };
  const value = { page, _links: linkM, _embedded: { user: [userMock] } };
  const userModelSubject = new Subject<UserPdModel>();
  // endregion
  const url = MockAppConfig.settings.apiServer.url;
  const loginUrl = MockAppConfig.settings.apiServer.loginUrl;
  const intHelperServiceStub = { addExcludedFromErrorHandling: () => { } };

  let httpTestingController: HttpTestingController;
  let loginService: LoginService;

  let spyAddExcludedFromErrorHandling: jasmine.Spy;
  let sessionServiceSpy: jasmine.SpyObj<SessionService>;
  let sessionUserServiceSpy: jasmine.SpyObj<SessionUserService>;

  beforeEach(() => {
    sessionServiceSpy = jasmine.createSpyObj('SessionService', ['setAuthData', 'setLoggedIn', 'setUser']);

    sessionUserServiceSpy = jasmine.createSpyObj('SessionUserService', ['getCurrentUser']);
    sessionUserServiceSpy.getCurrentUser.and.returnValue(userModelSubject.asObservable());

    spyAddExcludedFromErrorHandling = spyOn(intHelperServiceStub, 'addExcludedFromErrorHandling');

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        LoginService,
        { provide: SessionService, useValue: sessionServiceSpy },
        { provide: SessionUserService, useValue: sessionUserServiceSpy },
        { provide: InterceptorHelperService, useValue: intHelperServiceStub }
      ]
    });
    AppConfig.settings = MockAppConfig.settings;
    httpTestingController = TestBed.get(HttpTestingController);
    loginService = TestBed.get(LoginService);
  });

  afterEach(() => {
    // After every test, assert that there are no more pending requests.
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(loginService).toBeTruthy();
  });

  it('should call InterceptorHelperService#addExcludedFromErrorHandling on init', () => {
    expect(spyAddExcludedFromErrorHandling).toHaveBeenCalledTimes(1);
  });

  it(`should post a 'LoginRequestModel' payload to the login url and, if it succeeds, set the auth data  in
    SessionService and call the getUserMethod in the SessionUserService in order to retrieve the current User info`, fakeAsync(() => {
    const body = { usernameOrEmail: userMock.username, password: 'test' };
    const response: LoginResponseModel = { access_token: 'access_tokenS', username: userMock.username };

    loginService.login(body).toPromise().then((data: LoginResponseModel) => {
      expect(data).toBeTruthy('data is not ok');
      expect(data.access_token).toBeTruthy('access token is not ok');
      expect(sessionServiceSpy.setAuthData).toHaveBeenCalledTimes(1);
      expect(sessionServiceSpy.setLoggedIn).toHaveBeenCalledTimes(1);
      expect(sessionUserServiceSpy.getCurrentUser).toHaveBeenCalledTimes(1);
      expect(sessionUserServiceSpy.getCurrentUser.calls.first().args[0]).toEqual(userMock.username, 'username used for calling ' +
        'the `getCurrentUser` service is not the same as the one which came in the response');
      expect(sessionServiceSpy.setUser).toHaveBeenCalledTimes(1);
      expect(sessionServiceSpy.setUser.calls.first().args[0]).toEqual(userMock, 'sessionUserService not ' +
        'called with the correct username');
    });

    // request mock
    const req = httpTestingController.match(r => r.url === url + loginUrl && r.method === 'POST' && r.body === body);
    expect(req.length).toBeGreaterThan(0, 'no request was found');
    req[0].flush(response);

    userModelSubject.next(value);

    tick();
  }
  ));
});
