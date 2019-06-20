import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { Location } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable, BehaviorSubject } from 'rxjs';
import { getRandomNumber } from '../../shared/test-util/functions.util';
import { PermissionInfoComponent } from './permission-info.component';
import { PermissionService } from '../shared/permission.service';
import { Permission } from '../shared/permission.model';

describe('PermissionInfoComponent', () => {
  let component: PermissionInfoComponent;
  let fixture: ComponentFixture<PermissionInfoComponent>;
  const permissionServiceStub = {
    getPermissionInfo: (_id: number): Observable<Permission> => new BehaviorSubject<Permission>(new Permission()).asObservable()
  };
  const locationStub = { back: () => { } };
  const id = getRandomNumber();
  const activatedRouteStub = { snapshot: { paramMap: { get: (): any => id } } };
  let getPermissionInfoSpy: jasmine.Spy;

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
    activatedRouteStub.snapshot.paramMap.get = () => id;
    getPermissionInfoSpy = spyOn(permissionServiceStub, 'getPermissionInfo');
    fixture = TestBed.createComponent(PermissionInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
/*
  it('should call PermissionService#getPermissionInfo on init', () => {
    expect(getPermissionInfoSpy).toHaveBeenCalledTimes(1);
    expect(getPermissionInfoSpy).toHaveBeenCalledWith(id);
  });

  it('should not call PermissionService#getPermissionInfo, but Location#back if the id is an invalid value', () => {
    activatedRouteStub.snapshot.paramMap.get = () => 'someInvalidId';
    const locationBackSpy = spyOn(locationStub, 'back');
    component['getPermissionInfo']();
    expect(getPermissionInfoSpy).not.toHaveBeenCalled();
    expect(locationBackSpy).toHaveBeenCalledTimes(1);
  });

  it('should call Location#back on call to back function', () => {
    const locationBackSpy = spyOn(locationStub, 'back');
    component['back']();
    expect(locationBackSpy).toHaveBeenCalledTimes(1);
  });
  */
});
