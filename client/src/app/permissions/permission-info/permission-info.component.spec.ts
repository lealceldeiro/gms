import { Location } from '@angular/common';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';
import { getRandomNumber } from '../../shared/test-util/functions.util';
import { Permission } from '../shared/permission.model';
import { PermissionService } from '../shared/permission.service';
import { PermissionInfoComponent } from './permission-info.component';

describe('PermissionInfoComponent', () => {
  let component: PermissionInfoComponent;
  let fixture: ComponentFixture<PermissionInfoComponent>;
  const spy = { location: { back: () => { } }, permission: { getInfo: (_id: any) => { } } };
  const permissionServiceStub = {
    getPermissionInfo: (_id: number): Observable<Permission> => {
      spy.permission.getInfo(_id);
      return new BehaviorSubject<Permission>(new Permission()).asObservable();
    }
  };
  const locationStub = { back: () => { spy.location.back(); } };
  const id = getRandomNumber();
  let idGetter = (): any => id;
  const activatedRouteStub = { snapshot: { paramMap: { get: (): any => idGetter() } } };
  let getPermissionInfoSpy: jasmine.Spy;
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

});
