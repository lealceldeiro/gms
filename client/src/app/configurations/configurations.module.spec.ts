import { ConfigurationsModule } from './configurations.module';

describe('PermissionModule', () => {
  let configurationsModule: ConfigurationsModule;

  beforeEach(() => {
    configurationsModule = new ConfigurationsModule();
  });

  it('should create an instance', () => {
    expect(configurationsModule).toBeTruthy();
  });
});
