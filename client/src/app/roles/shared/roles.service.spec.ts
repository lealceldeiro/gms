import { HttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { environment } from 'src/environments/environment';
import { ParamsService } from '../../core/request/params/params.service';
import { getRandomNumber } from '../../shared/test-util/functions.util';
import { RolesService } from './roles.service';

describe('RolesService', () => {
  const spy = { getHttpParams: (_a: any) => { }, get: (_a: any, _b: any) => { }, delete: (_a: any, _b: any) => { } };
  const httpParamsValue = { someRandomProperty: 'someRandomValue' + getRandomNumber() };
  const paramsServiceStub = { getHttpParams: (a: any) => { spy.getHttpParams(a); return httpParamsValue; } };
  const httpClientMock = { get: (a: any, b: any) => spy.get(a, b), delete: (_a: any, _b: any) => spy.delete(_a, _b) };
  let roleService: RolesService;
  let httpClientGetSpy: jasmine.Spy;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        RolesService,
        { provide: ParamsService, useValue: paramsServiceStub },
        { provide: HttpClient, useValue: httpClientMock }
      ]
    });
    roleService = TestBed.get(RolesService);
    httpClientGetSpy = spyOn(spy, 'get');
  });

  it('should be created', () => {
    expect(roleService).toBeTruthy();
  });

  it('#getRoles should call HttpClient#get with the proper parameters, retrieved from ParamsService#getHttpParams', () => {
    const paramsServiceGetParamsSpy = spyOn(spy, 'getHttpParams');
    const size = getRandomNumber(1, 200);
    const page = getRandomNumber(1, 300);
    const params: { [key: string]: number } = {};
    params[ParamsService.SIZE] = size;
    params[ParamsService.PAGE] = page;

    roleService.getRoles(size, page);

    expect(paramsServiceGetParamsSpy).toHaveBeenCalledTimes(1);
    expect(paramsServiceGetParamsSpy).toHaveBeenCalledWith(params);
    expect(httpClientGetSpy).toHaveBeenCalledTimes(1);
    expect(httpClientGetSpy).toHaveBeenCalledWith(`${environment.apiBaseUrl}role`, { params: httpParamsValue });
  });

  it('#getRoleInfo should call HttpClient#get with the provided id as parameter', () => {
    const id = getRandomNumber();
    roleService.getRoleInfo(id);

    expect(httpClientGetSpy).toHaveBeenCalledTimes(1);
    expect(httpClientGetSpy).toHaveBeenCalledWith(`${environment.apiBaseUrl}role/${id}`, undefined); // undefined for no params
  });

  it('#deleteRoleInfo should call HttpClient#delete with the provided id as parameter', () => {
    const httpClientDeleteSpy = spyOn(spy, 'delete');
    const id = getRandomNumber();
    roleService.deleteRoleInfo(id);

    expect(httpClientDeleteSpy).toHaveBeenCalledTimes(1);
    expect(httpClientDeleteSpy.calls.mostRecent().args[0]).toEqual(`${environment.apiBaseUrl}role/${id}`);
  });

  it('#getRolePermissions should call HttpClient#get with the provided id as parameter', () => {
    const id = getRandomNumber();
    roleService.getRolePermissions(id);

    expect(httpClientGetSpy).toHaveBeenCalledTimes(1);
    expect(httpClientGetSpy.calls.mostRecent().args[0]).toEqual(`${environment.apiBaseUrl}role/${id}/permissions`);
  });
});
