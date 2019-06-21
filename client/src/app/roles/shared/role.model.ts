/**
 * Domain object which represents a role.
 */

export class Role {

  /**
   * Label to which the role can be referred to.
   */
  label = '';

  /**
   * Identifier.
   */
  id = -1;

  /**
   * A description of what is this role for.
   */
  description = '';

  /**
   * Whether the role is enabled or not.
   */
  enabled = false;
}
