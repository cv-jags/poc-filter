/**
 * POC for Starz Play Arabia (June 2018)
 */
package poc.it;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl;
import static com.github.tomakehurst.wiremock.client.WireMock.request;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.opentest4j.MultipleFailuresError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

import poc.model.Feed;
import poc.model.FeedEntry;

/**
 *
 * @author <cv.jags@gmail.com>
 *
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = "data.base_uri=http://localhost:8081")
public class FilterMediaApplicationTest {

	private static final Class<FilterMediaApplicationTest> THIS_CLASS = FilterMediaApplicationTest.class;
	private static final int WIREMOCK_PORT = 8081;
	private static final WireMockServer wireMockServer = new WireMockServer(
			options().port(WIREMOCK_PORT).withRootDirectory(THIS_CLASS.getResource("/wiremock").getPath()).containerThreads(15).jettyAcceptors(4));

	@Autowired
	private TestRestTemplate restTemplate;

	@BeforeAll
	static void setupTests() {
		wireMockServer.start();
	}

	@BeforeEach
	void setupTest() {
		wireMockServer.resetToDefaultMappings();
	}

	@AfterAll
	static void cleanAfterTests() {
		wireMockServer.shutdown();
	}

	@Test
	@DisplayName("Media endpoint returns 400 when data server returns 400")
	void mediaEndpointReturns400WhenServerReturns400() {
		dataServerResponse(400, "Bad Request");

		ResponseEntity<String> responseEntity = restTemplate.getForEntity("/media", String.class);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Bad Request", responseEntity.getBody());
	}

	@Test
	@DisplayName("Media endpoint returns 500 when data server returns 500")
	void mediaEndpointReturns500WhenServerReturns500() {
		dataServerResponse(500, "Internal Server Error");

		ResponseEntity<String> responseEntity = restTemplate.getForEntity("/media", String.class);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
		assertEquals("Internal Server Error", responseEntity.getBody());
	}

	@Test
	@DisplayName("Media endpoint returns 400 when filter unknown")
	void mediaEndpointReturnsWhenFilterUnknown() {
		String uri = "/media?filter=adult";

		ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

		assertAll("Invalid request",
		        () -> assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode()));
	}

	@Test
	@DisplayName("Media endpoint returns 400 when filter censoring without level")
	void mediaEndpointReturnsWhenFilterCensoringWithoutLevel() {
		String uri = "/media?filter=censoring";

		ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

		assertAll("Invalid request",
				() -> assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode()));
	}

	@Test
	@DisplayName("Media endpoint returns 400 when filter censoring with unknown level")
	void mediaEndpointReturnsWhenFilterCensoringWithUnknownLevel() {
		String uri = "/media?filter=censoring&level=non-censored";

		ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

		assertAll("Invalid request",
				() -> assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode()));
	}

	@Test
	@DisplayName("Media endpoint returns complete result when no filter parameter")
	void mediaEndpointReturnsWhenNoFilter() {
		String uri = "/media";

		ResponseEntity<Feed> responseEntity = restTemplate.getForEntity(uri, Feed.class);

		assertAll("Unchanged feed", () -> {
			assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
			assertNotNull(responseEntity.getBody());
			assertUnchanged(responseEntity.getBody());
		});
	}

	@Test
	@DisplayName("Media endpoint filters censored media when filter censoring with level uncensored")
	void mediaEndpointReturnsWhenFilterCensoringWithLevelUncensored() {
		String uri = "/media?filter=censoring&level=uncensored";

		ResponseEntity<Feed> responseEntity = restTemplate.getForEntity(uri, Feed.class);

		assertAll("Unchanged feed", () -> {
			assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
			assertNotNull(responseEntity.getBody());
			assertUncensoredFilter(responseEntity.getBody());
		});
	}

	@Test
	@DisplayName("Media endpoint filters uncensored media when filter censoring with level censored")
	void mediaEndpointReturnsWhenFilterCensoringWithLevelCensored() {
		String uri = "/media?filter=censoring&level=censored";

		ResponseEntity<Feed> responseEntity = restTemplate.getForEntity(uri, Feed.class);

		assertAll("Unchanged feed", () -> {
			assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
			assertNotNull(responseEntity.getBody());
			assertCensoredFilter(responseEntity.getBody());
		});
	}

	private void dataServerResponse(int statusCode, String message) {
		for (StubMapping stub : wireMockServer.listAllStubMappings().getMappings()) {
			wireMockServer.removeStub(stub);
		}

		wireMockServer.addStubMapping(request("GET", anyUrl())
				.willReturn(
						aResponse().withStatus(statusCode).withBody(message).withHeader("Content-Type", "text/plain"))
				.build());
	}

	private void assertUnchanged(Feed feed) throws MultipleFailuresError {
		assertAll(() -> {
			assertEquals(Integer.valueOf(4), feed.getEntryCount());
			assertEquals(4, feed.getEntries().size());

			assertUnchangedEntry0(feed.getEntries().get(0));
			assertUnchangedEntry1(feed.getEntries().get(1));
			assertUnchangedEntry2(feed.getEntries().get(2));
			assertUnchangedEntry3(feed.getEntries().get(3));
		});
	}

	private void assertCensoredFilter(Feed feed) throws MultipleFailuresError {
		assertAll(() -> {
			assertEquals(Integer.valueOf(4), feed.getEntryCount());
			assertEquals(4, feed.getEntries().size());

			assertUnchangedEntry0(feed.getEntries().get(0));
			assertUnchangedEntry1(feed.getEntries().get(1));
			assertUnchangedEntry2(feed.getEntries().get(2));
			assertCensoredEntry3(feed.getEntries().get(3));
		});
	}

	private void assertUncensoredFilter(Feed feed) throws MultipleFailuresError {
		assertAll(() -> {
			assertEquals(Integer.valueOf(4), feed.getEntryCount());
			assertEquals(4, feed.getEntries().size());

			assertUnchangedEntry0(feed.getEntries().get(0));
			assertUnchangedEntry1(feed.getEntries().get(1));
			assertUnchangedEntry2(feed.getEntries().get(2));
			assertUncensoredEntry3(feed.getEntries().get(3));
		});
	}

	private void assertUnchangedEntry0(FeedEntry entry0) throws MultipleFailuresError {
		assertAll("Entry 0", () -> assertEquals("OZTHEGREATANDPOWERFULY2013MFR", entry0.getGuid()),
				() -> assertEquals("", entry0.getPeg$contentClassification()),
				() -> assertEquals(1, entry0.getMedia().size()),
				() -> assertEquals("OZTHEGREATANDPOWERFULY2013MFR", entry0.getMedia().get(0).getGuid()),
				() -> assertEquals(11, entry0.getMedia().get(0).getContent().size()));
	}

	private void assertUnchangedEntry1(FeedEntry entry1) throws MultipleFailuresError {
		assertAll("Entry 1", () -> assertEquals("VUALTOTESTEXTRA", entry1.getGuid()),
				() -> assertEquals("Uncensored", entry1.getPeg$contentClassification()),
				() -> assertEquals(1, entry1.getMedia().size()),
				() -> assertEquals("VUALTOTESTEXTRA", entry1.getMedia().get(0).getGuid()),
				() -> assertEquals(12, entry1.getMedia().get(0).getContent().size()));
	}

	private void assertUnchangedEntry2(FeedEntry entry2) throws MultipleFailuresError {
		assertAll("Entry 2", () -> assertEquals("HOWTOTRAINYOURDRAGONY2010M", entry2.getGuid()),
				() -> assertEquals("Uncensored", entry2.getPeg$contentClassification()),
				() -> assertEquals(1, entry2.getMedia().size()),
				() -> assertEquals("HOWTOTRAINYOURDRAGONY2010M", entry2.getMedia().get(0).getGuid()),
				() -> assertEquals(18, entry2.getMedia().get(0).getContent().size()));
	}

	private void assertUnchangedEntry3(FeedEntry entry3) throws MultipleFailuresError {
		assertAll("Entry 3", () -> assertEquals("THINKLIKEAMANY2012M", entry3.getGuid()),
				() -> assertEquals("Censored", entry3.getPeg$contentClassification()),
				() -> assertEquals(2, entry3.getMedia().size()),
				() -> assertEquals("THINKLIKEAMANY2012M", entry3.getMedia().get(0).getGuid()),
				() -> assertEquals(15, entry3.getMedia().get(0).getContent().size()),
				() -> assertEquals("THINKLIKEAMANY2012MC", entry3.getMedia().get(1).getGuid()),
				() -> assertEquals(13, entry3.getMedia().get(1).getContent().size()));
	}

	private void assertCensoredEntry3(FeedEntry entry3) throws MultipleFailuresError {
		assertAll("Entry 3", () -> assertEquals("THINKLIKEAMANY2012M", entry3.getGuid()),
				() -> assertEquals("Censored", entry3.getPeg$contentClassification()),
				() -> assertEquals(1, entry3.getMedia().size()),
				() -> assertEquals("THINKLIKEAMANY2012MC", entry3.getMedia().get(0).getGuid()),
				() -> assertEquals(13, entry3.getMedia().get(0).getContent().size()));
	}

	private void assertUncensoredEntry3(FeedEntry entry3) throws MultipleFailuresError {
		assertAll("Entry 3", () -> assertEquals("THINKLIKEAMANY2012M", entry3.getGuid()),
				() -> assertEquals("Censored", entry3.getPeg$contentClassification()),
				() -> assertEquals(1, entry3.getMedia().size()),
				() -> assertEquals("THINKLIKEAMANY2012M", entry3.getMedia().get(0).getGuid()),
				() -> assertEquals(15, entry3.getMedia().get(0).getContent().size()));
	}
}
