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
}
