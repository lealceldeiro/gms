import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PermissionListComponent } from './permission-list.component';
import { PermissionService } from '../shared/permission.service';
import { SharedModule } from '../../shared/shared.module';
import { Subject } from 'rxjs';
import { PermissionPd } from '../shared/permission-pd';

describe('PermissionListComponent', () => {
  let component: PermissionListComponent;
  let fixture: ComponentFixture<PermissionListComponent>;

  const subjectLRM = new Subject<PermissionPd>();
  const ret = () => subjectLRM.asObservable();
  const spy = { getP: (a, b) => {}, error: (a, b) => {} };
  const permissionServiceStub = { getPermissions: (size: number, page: number ) => { spy.getP(size, page); return ret(); } };

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PermissionListComponent ],
      imports: [ SharedModule ],
      providers: [
        { provide: PermissionService, useValue: permissionServiceStub },
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PermissionListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
