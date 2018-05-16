import { User } from '../../../session/user.model';
import { PaginatedDataModel } from '../model';

/**
 * Exposes the information provided by paginated data responses regarding to Users.
 */
export interface UserPdModel extends PaginatedDataModel {

  /**
   * Embedded array of users.
   */
  _embedded: {
    /**
     * Array of users.
     */
    user: [User]
  };

}
