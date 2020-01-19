/**
 * Interface for Angular app configuration settings
 */
export interface IAppConfig {
  /**
   * Environment where the app is running.
   */
  env: {
    /**
     * Environment name.
     */
    name: string;
    /**
     * Name to be used for the application.
     */
    metaName: string;
  };
  /**
   * API server information
   */
  apiServer: {
    /**
     * API URL
     */
    url: string;
    /**
     * Relative url for requesting the login service.
     */
    loginUrl: string;
  };
  /**
   * Front-end security information.
   */
  security: {
    /**
     * Hash generation information.
     */
    hash: {
      /**
       * Values used to generate hash values.
       */
      key: ISecurityKey
    }
  };
}

/**
 * Holds the security keys.
 */
export interface ISecurityKey {
    /**
     * Key under which the value `loggedIn` is stored in the StorageService.
     */
    loggedIn: string;
    /**
     * Key under which the value `notLoggedIn` is stored in the StorageService.
     */
    notLoggedIn: string;
    /**
     * Key under which the value security parameters such as `access_token` and `refresh_token` are stored in the
     * StorageService.
     */
    loginData: string;
    /**
     * Key under which the session user's data is stored in the StorageService.
     */
    user: string;
    /**
     * Key under which the access token is stored.
     */
    accessToken: string;
    /**
     * Key under which the refresh token is stored.
     */
    refreshToken: string;
    /**
     * Key under which the header to be sent carrying the access token is stored.
     */
    headerToBeSent: string;
    /**
     * Key under which the token type is stored.
     */
    tokenType: string;
    /**
     * Key under which the "remember me" is stored.
     */
    rememberMe: string;
}
