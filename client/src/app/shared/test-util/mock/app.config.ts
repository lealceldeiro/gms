import { IAppConfig } from 'src/app/core/model/config/app-config.model';

/**
 * Sample app configuration to be used in the tests.
 */
const mockData = {
    env: { name: '-', metaName: '-'},
    apiServer: { url: '-', loginUrl: '-' },
};

/**
 * Mock class.
 */
export class MockAppConfig {
  /**
   * App configuration.
   */
  static settings: IAppConfig = mockData;
}
