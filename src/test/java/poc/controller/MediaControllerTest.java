/**
 * POC for Starz Play Arabia (June 2018)
 */
package poc.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import poc.model.Feed;
import poc.model.FeedEntry;
import poc.service.FilterService;
import poc.service.MediaService;

/**
 * 
 * @author <cv.jags@gmail.com>
 *
 */
@WebMvcTest(MediaController.class)
@ExtendWith(SpringExtension.class)
public class MediaControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private MediaService mediaService;

	@MockBean
	private FilterService<Feed> filterService;

	@MockBean
	private RestTemplate restTemplate;

	@Test
	@DisplayName("Media endpoint returns 404 when accesing differen than media")
	void mediaEndpointReturns404WhenBadPath() throws Exception {

		ResultActions action = mvc.perform(get("/no-media").param("paramName", "paramValue"));

		action.andExpect(status().isNotFound()); // .andExpect(withContentTypeJsonUtf8());
	}
	
	@Test
	@DisplayName("Media endpoint returns unfiltered Feed when no parameter")
	void mediaEndpointReturnsFeedWhenNoParams() throws Exception {
		Mockito.when(mediaService.fetch()).thenReturn(buildFeed(buildEntry("guid1"), buildEntry("guid2")));

		ResultActions action = mvc.perform(buildMediaRequest());

		action.andExpect(status().isOk()).andExpect(withContentTypeJsonUtf8())
				.andExpect(jsonPath("$.entryCount").value(2));
	}

	@Test
	@DisplayName("Media endpoint returns 400 when there isn't censoring filter parameter")
	void mediaEndpointReturns400WhenNoFilterParams() throws Exception {

		ResultActions action = mvc.perform(buildMediaRequest().param("paramName", "paramValue"));

		action.andExpect(status().isBadRequest()).andExpect(withContentTypeJsonUtf8());
	}

	@Test
	@DisplayName("Media endpoint returns 400 when there is filter parameter but is not censoring")
	void mediaEndpointReturns400WhenFilterIsNotCensoring() throws Exception {

		ResultActions action = mvc.perform(buildMediaRequest().param("filter", "allPublics"));

		action.andExpect(status().isBadRequest()).andExpect(withContentTypeJsonUtf8());
	}

	@Test
	@DisplayName("Media endpoint returns 400 when there is filter parameter but is not censoring and a valid level parameter")
	void mediaEndpointReturns400WhenFilterIsNotCensoringAndLevel() throws Exception {

		ResultActions action = mvc
				.perform(buildMediaRequest().param("filter", "allPublics").param("level", "uncensored"));

		action.andExpect(status().isBadRequest()).andExpect(withContentTypeJsonUtf8());
	}

	@Test
	@DisplayName("Media endpoint returns 400 when there is only filter parameter")
	void mediaEndpointReturns400WhenOnlyFilterParameter() throws Exception {

		ResultActions action = mvc.perform(buildMediaRequest().param("filter", "censoring"));

		action.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("Media endpoint returns 400 when there are filter censoring parameter and invalid level parameter")
	void mediaEndpointReturns400WhenFilterIsCensoringAndNotValidLevel() throws Exception {

		ResultActions action = mvc
				.perform(buildMediaRequest().param("filter", "censoring").param("level", "un-censored"));

		action.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("Media endpoint returns censored filter when there are filter censoring and level censored parameter")
	void mediaEndpointReturns200WhenFilterIsCensoringAndValidLevel() throws Exception {
		Feed feed = buildFeed(buildEntry("guid1"), buildEntry("guid2"));
		Mockito.when(mediaService.fetch()).thenReturn(feed);
		Map<String, String> map = ImmutableMap.<String, String>builder().put("level", "censored").build();
		Feed filteredFeed = buildFeed(buildEntry("guid1"));
		Mockito.when(filterService.filter(map, feed)).thenReturn(filteredFeed);

		ResultActions action = mvc
				.perform(buildMediaRequest().param("filter", "censoring").param("level", "censored"));

		action.andExpect(status().isOk()).andExpect(withContentTypeJsonUtf8())
				.andExpect(jsonPath("$.entryCount").value(1))
				.andExpect(jsonPath("$.entries[0].guid").value("guid1"));
	}
	
	@Test
	@DisplayName("Media endpoint returns uncensored filter when there are filter censoring and level uncensored parameters")
	void mediaEndpointReturns200WhenFilterIsCensoringAndLevelUncensored() throws Exception {
		Feed feed = buildFeed(buildEntry("guid1"), buildEntry("guid2"));
		Mockito.when(mediaService.fetch()).thenReturn(feed);
		Map<String, String> map = ImmutableMap.<String, String>builder().put("level", "uncensored").build();
		Feed filteredFeed = buildFeed(buildEntry("guid2"));
		Mockito.when(filterService.filter(map, feed)).thenReturn(filteredFeed);

		ResultActions action = mvc
				.perform(buildMediaRequest().param("filter", "censoring").param("level", "uncensored"));

		action.andExpect(status().isOk()).andExpect(withContentTypeJsonUtf8())
				.andExpect(jsonPath("$.entryCount").value(1))
				.andExpect(jsonPath("$.entries[0].guid").value("guid2"));
	}
	
	@Test
	@DisplayName("Media endpoint returns uncensored filter when there are filter censoring, level uncensored and other parameters")
	void mediaEndpointReturns200WhenFilterIsCensoringAndLevelUncensoredAndOtherParam() throws Exception {
		Feed feed = buildFeed(buildEntry("guid1"), buildEntry("guid2"));
		Mockito.when(mediaService.fetch()).thenReturn(feed);
		Map<String, String> map = ImmutableMap.<String, String>builder().put("level", "uncensored").build();
		Feed filteredFeed = buildFeed(buildEntry("guid2"));
		Mockito.when(filterService.filter(map, feed)).thenReturn(filteredFeed);

		ResultActions action = mvc
				.perform(buildMediaRequest().param("filter", "censoring").param("level", "uncensored").param("paramName", "paramValue"));

		action.andExpect(status().isOk()).andExpect(withContentTypeJsonUtf8())
				.andExpect(jsonPath("$.entryCount").value(1))
				.andExpect(jsonPath("$.entries[0].guid").value("guid2"));
	}

	private MockHttpServletRequestBuilder buildMediaRequest() {
		return get("/media");
	}

	private FeedEntry buildEntry(String guid) {
		return FeedEntry.builder().guid(guid).build();
	}

	private Feed buildFeed(FeedEntry... entries) {
		return Feed.builder().title("feed fixture").entries(Lists.newArrayList(entries)).entryCount(entries.length)
				.startIndex(1).build();
	}

	private ResultMatcher withContentTypeJsonUtf8() {
		return content().contentType("application/json;charset=UTF-8");
	}

}
