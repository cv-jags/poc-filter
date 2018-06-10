/**
 * POC for Starz Play Arabia (June 2018)
 */
package poc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import poc.data.MediaClient;
import poc.model.Feed;

/**
 * 
 * @author <cv.jags@gmail.com>
 *
 */
@ExtendWith(SpringExtension.class)
public class DefaultMediaServiceTest {

	@MockBean
	private MediaClient mediaClient;

	DefaultMediaService service;

	@BeforeEach
	public void setup() {
		service = new DefaultMediaService(mediaClient);
	}
	
	@Test
	@DisplayName("Fetch retuns client getData result null")
	void fetchReturnsClientGetDataResultNull() {
		when(mediaClient.getData()).thenReturn(null);
		
		assertNull(service.fetch());
	}

	@Test
	@DisplayName("Fetch retuns client getData result feed")
	void fetchReturnsClientGetDataResultFeed() {
		Feed originalFeed = Feed.builder().title("title").build();
		when(mediaClient.getData()).thenReturn(originalFeed);
		
		Feed feed = service.fetch();
		
		assertEquals(originalFeed, feed);
	}
}
