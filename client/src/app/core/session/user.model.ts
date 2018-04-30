/**
 * Domain object which represents a user.
 */
export class User {

  /**
   * User's username
   */
  username: string;

  /**
   * User's email
   */
  email: string;

  /**
   * User's name
   */
  name: string;

  /**
   * User's last name.
   */
  lastName: string;

  /**
   * User's password.
   */
  password?: string;

  /**
   * Whether the user is enabled or not.
   */
  enabled = false;

  /**
   * Whether the user has verified the email associated to the account or not.
   */
  emailVerified = false;

  /**
   * Whether the user's account has not expired or has it.
   */
  accountNonExpired = true;

  /**
   * Whether the user's account is not locked or it is.
   */
  accountNonLocked = true;

  /**
   * Whether the user's credentials (combination of username, email, or whatsoever and the password) are not expired or are they.
   */
  credentialsNonExpired = true;

}
