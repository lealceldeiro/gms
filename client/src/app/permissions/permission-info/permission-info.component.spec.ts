import { Location } from '@angular/common';
import { async, ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';
import { getRandomNumber } from '../../shared/test-util/functions.util';
import { Permission } from '../shared/permission.model';
import { PermissionService } from '../shared/permission.service';
import { PermissionInfoComponent } from './permission-info.component';
import { By } from '@angular/platform-browser';
import { Role } from 'src/app/roles/shared/role.model';
import { RolePd } from 'src/app/roles/shared/role.pd';

describe('PermissionInfoComponent', () => {
  let component: PermissionInfoComponent;
  let fixture: ComponentFixture<PermissionInfoComponent>;
  const fakeRoles: Array<Role> = [
    new Role(),
    {
      description: 'des-' + getRandomNumber(),
      enabled: (getRandomNumber() % 2 === 0),
      id: getRandomNumber(),
      label: ('label-' + getRandomNumber())
    }
  ];
  const fakeRolesResult: Observable<RolePd> = new BehaviorSubject<RolePd>({ _embedded: { role: fakeRoles } } as RolePd).asObservable();
  const spy = { location: { back: () => { } }, permission: { getInfo: (_id: any) => { }, getRoles: (_id: any) => {} } };
  const permissionServiceStub = {
    getPermissionInfo: (_id: number): Observable<Permission> => {
      spy.permission.getInfo(_id);
      return new BehaviorSubject<Permission>(new Permission()).asObservable();
    },
    getPermissionRoles: (_id: number) => {
      spy.permission.getRoles(_id);
      return fakeRolesResult;
    }
  };
  const locationStub = { back: () => { spy.location.back(); } };
  const id = getRandomNumber();
  let idGetter = (): any => id;
  const activatedRouteStub = { snapshot: { paramMap: { get: (): any => idGetter() } } };
  let getPermissionInfoSpy: jasmine.Spy;
  let getPermissionRolesSpy: jasmine.Spy;
  let locationBackSpy: jasmine.Spy;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [PermissionInfoComponent],
      providers: [
        { provide: PermissionService, useValue: permissionServiceStub },
        { provide: Location, useValue: locationStub },
        { provide: ActivatedRoute, useValue: activatedRouteStub }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    idGetter = (): any => id;
    fixture = TestBed.createComponent(PermissionInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call PermissionService#getPermissionInfo on init', () => {
    getPermissionInfoSpy = spyOn(spy.permission, 'getInfo');
    component.ngOnInit();
    expect(getPermissionInfoSpy).toHaveBeenCalledTimes(1);
    expect(getPermissionInfoSpy).toHaveBeenCalledWith(id);
  });

  it('should not call PermissionService#getPermissionInfo, but Location#back if the id is an invalid value', () => {
    getPermissionInfoSpy = spyOn(spy.permission, 'getInfo');
    locationBackSpy = spyOn(spy.location, 'back');
    idGetter = (): any => 'someInvalidId';
    component['getPermissionInfo']();
    expect(getPermissionInfoSpy).not.toHaveBeenCalled();
    expect(locationBackSpy).toHaveBeenCalledTimes(1);
  });

  it('should call Location#back on call to back function', () => {
    locationBackSpy = spyOn(spy.location, 'back');
    component['back']();
    expect(locationBackSpy).toHaveBeenCalledTimes(1);
  });

  it('should show a text to allow retrieving the roles using the selected permission and hide the table of roles', () => {
    expect(component.rolesLoaded).toEqual(false);
    expect(component.roles).toEqual([]);
    expect(fixture.debugElement.query(By.css('#showRolesLnk'))).toBeTruthy();
    expect(fixture.debugElement.query(By.css('#rolesTable'))).toBeFalsy();
  });

  it('should hide the text to allow retrieving the roles using the selected permission and show the table of roles', fakeAsync(() => {
    getPermissionRolesSpy = spyOn(spy.permission, 'getRoles');
    component.showInRoles();
    fixture.detectChanges();
    tick();
    expect(getPermissionRolesSpy).toHaveBeenCalledTimes(1);
    expect(getPermissionRolesSpy).toHaveBeenCalledWith(id);
    expect(component.rolesLoaded).toEqual(true);
    expect(component.roles).toEqual(fakeRoles);
    expect(fixture.debugElement.query(By.css('#showRolesLnk'))).toBeFalsy();
    expect(fixture.debugElement.query(By.css('#rolesTable'))).toBeTruthy();
  }));

});
