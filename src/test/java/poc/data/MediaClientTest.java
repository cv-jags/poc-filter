/**
 * POC for Starz Play Arabia (June 2018)
 */
package poc.data;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import poc.exception.ExternalSystemException;
import poc.model.Feed;

/**
 *
 * @author <cv.jags@gmail.com>
 *
 */
@ExtendWith(SpringExtension.class)
public class MediaClientTest {

	private static final String TITLE = "title";
	private static final String BASE_URI = "http://baseUri";
	private static final String SERVICE_PATH = "/data/path";
	private static final String URI = BASE_URI + SERVICE_PATH;

	@MockBean
	private RestTemplate restTemplate;

	private MediaClient client;

	@BeforeEach
	void setup() {
		client = new MediaClient(restTemplate, BASE_URI, SERVICE_PATH);
	}

	@Test
	@DisplayName("Returns the same result from restTemplate")
	void getDataReturnsResultFromRestTemplate() {
		Feed responseFeed = buildFeed();
		when(restTemplate.getForEntity(URI, Feed.class)).thenReturn(ResponseEntity.ok().body(responseFeed));

		Feed feed = client.getData();

		assertEquals(responseFeed, feed);
	}

	@Test
	@DisplayName("Don't capture or modify thrown exceptions by template")
	void getDataReturnsThrowsSameExceptionAsRestTemplate() {
		ExternalSystemException exception = new ExternalSystemException(404, "Not found");
		when(restTemplate.getForEntity(URI, Feed.class)).thenThrow(exception);
		try {

			client.getData();

			fail();
		} catch (ExternalSystemException e) {
			assertAll("Exception on bad request",
					() -> assertEquals(404, e.getStatusCode()),
					() -> assertEquals("Not found", e.getReason()));
		}
	}

	private Feed buildFeed() {
		return Feed.builder().title(TITLE).build();
	}
}
