/**
 * Exposes the information must be provided when requesting the login endpoint.
 */
export interface LoginRequestBody {

  /**
   * User’s identifier. It can be the email or username.
   */
  usernameOrEmail: string;

  /**
   * User’s password.
   */
  password: string;
}
