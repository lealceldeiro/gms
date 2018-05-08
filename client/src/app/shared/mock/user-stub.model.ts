/**
 * Domain object which represents a user.
 */
export class UserStub {

  /**
   * User's username
   */
  username = 'sampleUsername';

  /**
   * User's email
   */
  email = 'sample@email.com';

  /**
   * User's name
   */
  name = 'Sample Name';

  /**
   * User's last name.
   */
  lastName = 'Sample Lastname';

  /**
   * User's password.
   */
  password = 'samplePassword123**!.,';

  /**
   * Whether the user is enabled or not.
   */
  enabled = true;

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
