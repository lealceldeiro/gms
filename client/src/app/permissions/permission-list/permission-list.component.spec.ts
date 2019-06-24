import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { BehaviorSubject } from 'rxjs';
import { DummyStubComponent } from '../../shared/mock/dummy-stub.component';
import { MockModule } from '../../shared/mock/mock.module';
import { SharedModule } from '../../shared/shared.module';
import { getRandomNumber } from '../../shared/test-util/functions.util';
import { PermissionPd } from '../shared/permission.pd';
import { PermissionService } from '../shared/permission.service';
import { PermissionListComponent } from './permission-list.component';

describe('PermissionListComponent', () => {
  let component: PermissionListComponent;
  let fixture: ComponentFixture<PermissionListComponent>;
  let componentEl: HTMLElement;

  const getSampleData = () => {
    return {
      _embedded: {
        permission: []
      },
      _links: {
        self: {
          href: 'href'
        }
      },
      page: {
        number: 0,
        totalElements: 0, // changed later
        size: 0,          // changed later
        totalPages: 0     // changed later
      }
    };
  };

  const addPermissionsToSampleData = (sampleData: { _embedded: any; _links?: { self: { href: string; }; }; page: any; }) => {
    const rangeBottom = 25;
    const rangeTop = 40;
    const randomAmountValue = getRandomNumber(rangeBottom, rangeTop);
    for (let i = 0; i < randomAmountValue; i++) {
      sampleData._embedded.permission.push({ name: 'n' + i, label: 'l' + i, id: i });
    }
    // re-calculate values accordingly to the random value
    sampleData.page.size = randomAmountValue - 7;
    sampleData.page.totalElements = randomAmountValue;
    sampleData.page.totalPages = Math.floor(sampleData.page.totalElements / sampleData.page.size);
  };

  const subjectLRM = new BehaviorSubject<PermissionPd>(getSampleData());
  const ret = () => subjectLRM.asObservable();
  const spy = { getPermissions: (a: any, b: any) => { } };
  const permissionServiceStub = { getPermissions: (size: number, page: number) => { spy.getPermissions(size, page); return ret(); } };

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [PermissionListComponent],
      imports: [SharedModule, MockModule, RouterTestingModule.withRoutes([{ path: './', component: DummyStubComponent }])],
      providers: [ { provide: PermissionService, useValue: permissionServiceStub } ]
    }).compileComponents();
  }));

  beforeEach(() => {
    // make the observable return a random value for each test
    const sampleData = getSampleData();
    addPermissionsToSampleData(sampleData);
    subjectLRM.next(sampleData);

    fixture = TestBed.createComponent(PermissionListComponent);
    component = fixture.componentInstance;
    componentEl = fixture.nativeElement;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render a table with as many rows as elements returned by the (mock) service', () => {
    const tr = componentEl.querySelectorAll('tr.p-row');
    expect(tr.length).toBe(subjectLRM.getValue()._embedded.permission.length, 'rows count does not match the total elements');
  });

  it('should call service for getting new permissions list when #loadList is called', () => {
    const getPermissionsSpy = spyOn(spy, 'getPermissions');

    const toPage = 3;
    component.loadList(toPage);

    expect(getPermissionsSpy).toHaveBeenCalledTimes(1);
    expect(getPermissionsSpy).toHaveBeenCalledWith(component.page.size, toPage - 1);
  });
});
