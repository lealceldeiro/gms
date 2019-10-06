import { HttpClient } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { inject, TestBed } from '@angular/core/testing';

import { UserPdModel } from '../response/paginated-data/impl/user-pd-.model';
import { LinksModel } from '../response/paginated-data/links.model';
import { PaginatedDataModel } from '../response/paginated-data/model';
import { PageModel } from '../response/paginated-data/page.model';
import { SelfModel } from '../response/paginated-data/self.model';
import { SessionUserService } from './session-user.service';
import { userMock } from './user.mock.model';
import { MockAppConfig } from '../../shared/test-util/mock/app.config';
import { AppConfig } from '../config/app.config';

describe('SessionUserService', () => {
  const url = MockAppConfig.settings.apiServer.url;
  let httpClient: HttpClient;
  let httpTestingController: HttpTestingController;
  let sessionUserService: SessionUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionUserService]
    });
    AppConfig.settings = MockAppConfig.settings;
    httpClient = TestBed.get(HttpClient);
    httpTestingController = TestBed.get(HttpTestingController);
    sessionUserService = TestBed.get(SessionUserService);
  });

  afterEach(() => {
    // After every test, assert that there are no more pending requests.
    httpTestingController.verify();
  });

  it('should be created', inject([SessionUserService], (service: SessionUserService) => {
    expect(service).toBeTruthy();
  }));

  it('should get the proper value for current user and return it as a `UserPdModel`', () => {
    sessionUserService.getCurrentUser('test').subscribe((data: PaginatedDataModel) => {
      const val = (data as UserPdModel)._embedded.user;
      expect(val.length > 0).toBeTruthy('failed to fetch the user returned in the response data');
      const u = val ? val[0] : {};
      expect(u).toEqual(userMock, 'response data is not equal to mocked data');
      expect(data.page.totalElements).toBe(numbVal);
      expect(data.page.size).toBe(numbVal);
      expect(data.page.totalPages).toBe(numbVal);
      expect(data.page.number).toBe(numbVal);
    });

    // use matcher function instead of  httpTestingController.expectOne(url + 'user/search/username-email'),
    // see: https://github.com/angular/angular/issues/19974
    const req = httpTestingController
      .expectOne(r => r.method === 'GET' && r.url === url + 'user/search/username-email');

    const href = 'hrefTest';
    const numbVal = 24;
    const selfModel: SelfModel = { href: href };
    const linksModel: LinksModel = { self: selfModel };
    const pageModel: PageModel = { size: numbVal, totalElements: numbVal, totalPages: numbVal, number: numbVal };
    const response: PaginatedDataModel = {
      _embedded: { user: [userMock] },
      _links: linksModel,
      page: pageModel
    };

    req.flush(response);
  });
});
