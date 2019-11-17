import { Entity } from '../model/entity.model';

/**
 * Domain object which represents a user.
 */
export class User extends Entity {
  /**
   * User's username
   */
  username = '';

  /**
   * User's email
   */
  email = '';

  /**
   * User's name
   */
  name = '';

  /**
   * User's last name.
   */
  lastName = '';

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
