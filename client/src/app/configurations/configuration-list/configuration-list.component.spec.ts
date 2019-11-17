import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { of } from 'rxjs';

import { getRandomNumber } from '../../shared/test-util/functions.util';
import { ConfigurationService } from '../shared/configuration.service';
import { ConfigurationListComponent } from './configuration-list.component';

describe('ConfigurationListComponent', () => {
  const configurationObject: { [key: string]: string } = {};
  const numOfProps = getRandomNumber();
  let component: ConfigurationListComponent;
  let fixture: ComponentFixture<ConfigurationListComponent>;
  let configurationServiceSpy: jasmine.SpyObj<ConfigurationService>;

  for (let i = 0; i < numOfProps; i++) {
    configurationObject['randomKey-' + getRandomNumber() + '-' + i] = 'randomValue-' + getRandomNumber() + '-' + i;
  }

  beforeEach(async(() => {
    configurationServiceSpy = jasmine.createSpyObj('ConfigurationService', ['getConfigurations', 'getUserConfigurations']);
    configurationServiceSpy.getConfigurations.and.returnValue(of<object>(configurationObject));
    configurationServiceSpy.getUserConfigurations.and.returnValue(of<object>(configurationObject));

    TestBed.configureTestingModule({
      declarations: [ConfigurationListComponent],
      providers: [{ provide: ConfigurationService, useValue: configurationServiceSpy }]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfigurationListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call loadConfigurations on init', () => {
    const loadConfigurationsSpy = spyOn(component, 'loadConfigurations');
    component.ngOnInit();
    expect(loadConfigurationsSpy).toHaveBeenCalledTimes(1);
  });

  it('should call ConfigurationService#getConfigurations on call to loadConfigurations and set values from the observable ', () => {
    expect(configurationServiceSpy.getConfigurations).toHaveBeenCalledTimes(1);
    expect(component.keys.system).toEqual(Object.keys(configurationObject));
    expect(component.values.system).toEqual(Object.values(configurationObject));
  });

  it('should call ConfigurationService#getUserConfigurations on call to loadUserConfigurations and set values from the observable ', () => {
    expect(configurationServiceSpy.getUserConfigurations).toHaveBeenCalledTimes(1);
    expect(component.keys.user).toEqual(Object.keys(configurationObject));
    expect(component.values.user).toEqual(Object.values(configurationObject));
  });
});
