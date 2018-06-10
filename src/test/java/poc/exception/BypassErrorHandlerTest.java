/**
 * POC for Starz Play Arabia (June 2018)
 */
package poc.exception;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * 
 * @author <cv.jags@gmail.com>
 *
 */
@ExtendWith(SpringExtension.class)
public class BypassErrorHandlerTest {

	private static final int STATUS_CODE = 999;
	private static final String BODY = "Body Text";

	@MockBean 
	private ClientHttpResponse response;
	
	private BypassErrorHandler handler = new BypassErrorHandler();
	
	@Test
	@DisplayName("Throws ExternalSystemException with response data")
	void handleErrorThrowsExternalSystemExceptionWithResponseData() throws IOException {
		when(response.getRawStatusCode()).thenReturn(STATUS_CODE);
		when(response.getBody()).thenReturn(buildInputStream(BODY));
		try {
			
			handler.handleError(response);
			
			fail();
		}catch(ExternalSystemException e) {
			assertAll("Exception on bad request", 
					() -> assertEquals(STATUS_CODE, e.getStatusCode()),
					() -> assertEquals(BODY, e.getReason()));
		}
	}

	private InputStream buildInputStream(String body) {
		return new ByteArrayInputStream(body.getBytes());
	}
	
}
