import { SelfModel } from './self.model';

/**
 * Exposes the information provided by the "_links" objects in paginated data response.
 */
export interface LinksModel {

  /**
   * Available services (links?) for the resource itself.
   */
  self: SelfModel;

}
