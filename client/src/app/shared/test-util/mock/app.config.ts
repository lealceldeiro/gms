import { IAppConfig } from '../../../core/model/config/app-config.model';
import { getRandomNumber } from '../functions.util';

const randomValue = getRandomNumber();
/**
 * Sample app configuration to be used in the tests.
 */
const mockData = {
    env: { name: `name${randomValue}`, metaName: `metaName${randomValue}`},
    apiServer: { url: `url${randomValue}`, loginUrl: `loginUrl${randomValue}` },
    security: {
      hash: {
        key: {
          loggedIn: `loggedIn${randomValue}`,
          notLoggedIn: `notLoggedIn${randomValue}`,
          loginData: `loginData${randomValue}`,
          user: `user${randomValue}`,
          accessToken: `accessToken${randomValue}`,
          refreshToken: `refreshToken${randomValue}`,
          headerToBeSent: `headerToBeSent${randomValue}`,
          tokenType: `tokenType${randomValue}`,
          rememberMe: `rememberMe${randomValue}`
        }
      }
    }
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
