/**
 * Keys used to specify certain configuration provided by the backend configuration resource.
 */
export enum ConfigurationKey {
  /**
   * Indicates whether the application will handle multiple owned entities or not (enterprises, businesses,etc)
   */
  IS_MULTI_ENTITY_APP_IN_SERVER = 'IS_MULTI_ENTITY_APP_IN_SERVER',
  /**
   * Indicates whether new users registration will be allowed or not via user sign-up
   */
  IS_USER_REGISTRATION_ALLOWED_IN_SERVER = 'IS_USER_REGISTRATION_ALLOWED_IN_SERVER',
}
