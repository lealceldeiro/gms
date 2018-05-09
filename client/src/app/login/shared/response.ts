/**
 * Exposes the information must be provided when the login request  is performed.
 */
export interface LoginResponseBody {
  /**
   * Time where the access and refresh tokens were issued at.
   */
  issued_at?: number;

  /**
   * User’s authorities.
   */
  authorities?: string[];

  /**
   * User’s username.
   */
  username?: string;

  /**
   * Access token.
   */
  access_token?: string;

  /**
   * Token scheme to be used.
   */
  token_type?: string;

  /**
   * Name of the header you have to use for sending the access token in every subsequent request.
   */
  header_to_be_sent?: string;

  /**
   * Time (expressed in milliseconds) in which the access token will expire.
   */
  token_expiration_time?: number;

  /**
   * Time of validity of the access token.
   */
  token_expires_in?: number;

  /**
   * Refresh token.
   */
  refresh_token?: string;

  /**
   * Time (expressed in milliseconds) in which the refresh token will expire.
   */
  refresh_token_expiration_time?: number;

  /**
   * Time of validity of the refresh token.
   */
  refresh_token_expires_in?: number;
}
