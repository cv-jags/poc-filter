/**
 * POC for Starz Play Arabia (June 2018)
 */
package poc.service;

import poc.model.Feed;

/**
 * 
 * @author <cv.jags@gmail.com>
 *
 */
public interface MediaService {

	/**
	 * Retrieve feed data from a service.
	 * 
	 * @return the feed from the data service.
	 */
	Feed fetch();

}
