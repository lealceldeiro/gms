import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { getRandomNumber } from '../../shared/test-util/functions.util';
import { ConfigurationService } from '../shared/configuration.service';
import { ConfigurationListComponent } from './configuration-list.component';

describe('ConfigurationListComponent', () => {
  let component: ConfigurationListComponent;
  let fixture: ComponentFixture<ConfigurationListComponent>;
  const configurationObject: { [key: string]: string } = {};
  const numOfProps = getRandomNumber();

  for (let i = 0; i < numOfProps; i++) {
    configurationObject['randomKey-' + getRandomNumber() + '-' + i] = 'randomValue-' + getRandomNumber() + '-' + i;
  }

  const spy = { getConfigurations: () => { } };
  const configurationServiceStub = {
    getConfigurations: () => {
      spy.getConfigurations();
      return of<object>(configurationObject);
    }
  };
  let getConfigurationsSpy: jasmine.Spy;
  let loadConfigurationsSpy: jasmine.Spy;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ConfigurationListComponent],
      providers: [{ provide: ConfigurationService, useValue: configurationServiceStub }]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfigurationListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    getConfigurationsSpy = spyOn(spy, 'getConfigurations');
    loadConfigurationsSpy = spyOn(component, 'loadConfigurations');
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call loadConfigurations on init', () => {
    component.ngOnInit();
    expect(loadConfigurationsSpy).toHaveBeenCalledTimes(1);
  });

  it('should call ConfigurationService#getConfigurations on call to loadConfigurations and set values from the observable ', () => {
    component.loadConfigurations();
    expect(loadConfigurationsSpy).toHaveBeenCalledTimes(1);
    expect(component.keys).toEqual(Object.keys(configurationObject));
    expect(component.values).toEqual(Object.values(configurationObject));
  });
});
