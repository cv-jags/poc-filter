/**
 * POC for Starz Play Arabia (June 2018)
 */
package poc.exception;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;

/**
 * 
 * @author <cv.jags@gmail.com>
 *
 */
@Component
public class BypassErrorHandler extends DefaultResponseErrorHandler {

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		throw new ExternalSystemException(response.getRawStatusCode(), inputStreamToString(response.getBody()));
	}

	private String inputStreamToString(InputStream is) {
		try (java.util.Scanner s = new java.util.Scanner(is)) {
			return s.useDelimiter("\\A").hasNext() ? s.next() : "";
		}
	}
}