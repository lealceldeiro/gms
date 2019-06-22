import { ConfigurationsModule } from './configurations.module';

describe('ConfigurationsModule', () => {
  let configurationsModule: ConfigurationsModule;

  beforeEach(() => {
    configurationsModule = new ConfigurationsModule();
  });

  it('should create an instance', () => {
    expect(configurationsModule).toBeTruthy();
  });
});
