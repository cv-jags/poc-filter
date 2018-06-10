/**
 * POC for Starz Play Arabia (June 2018)
 */
package poc.service;

import org.springframework.stereotype.Service;

import poc.data.MediaClient;
import poc.model.Feed;

/**
 * 
 * @author <cv.jags@gmail.com>
 *
 */
@Service
public class DefaultMediaService implements MediaService {

	private final MediaClient client;
	
	public DefaultMediaService(MediaClient client) {
		this.client = client;
	}
	
	@Override
	public Feed fetch() {
		return client.getData();
	}
}
