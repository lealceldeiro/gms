/**
 * Exposes the information provided by paginated data responses regarding to Users.
 */
import { User } from '../../../session/user.model';
import { PaginatedDataModel } from '../model';

export interface UserPdModel extends PaginatedDataModel {

  /**
   * Embedded array of users.
   */
  _embedded: {
    /**
     * Array of users.
     */
    user: [
      /**
       * User entity data.
       */
      User
      ]
  };

}
