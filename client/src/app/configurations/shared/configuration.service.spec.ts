import { HttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { environment } from 'src/environments/environment';
import { ConfigurationService } from './configuration.service';

describe('ConfigurationService', () => {
  const spy = { get: (_a: any, _b: any) => { } };
  const httpClientMock = { get: (a: any, b: any) => spy.get(a, b) };
  let configurationService: ConfigurationService;
  let httpClientGetSpy: jasmine.Spy;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        ConfigurationService,
        { provide: HttpClient, useValue: httpClientMock }
      ]
    });
    configurationService = TestBed.get(ConfigurationService);
    httpClientGetSpy = spyOn(spy, 'get');
  });

  it('should be created', () => {
    const service: ConfigurationService = TestBed.get(ConfigurationService);
    expect(service).toBeTruthy();
  });

  it('#getPermissions should call HttpClient#get with the proper parameters, retrieved from ParamsService#getHttpParams', () => {
    configurationService.getConfigurations();
    expect(httpClientGetSpy).toHaveBeenCalledTimes(1);
    expect(httpClientGetSpy).toHaveBeenCalledWith(environment.apiBaseUrl + 'configuration', undefined);
  });
});
