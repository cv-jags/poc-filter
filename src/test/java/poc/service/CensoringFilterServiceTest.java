/**
 * POC for Starz Play Arabia (June 2018)
 */
package poc.service;

import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import poc.model.Feed;
import poc.model.FeedEntry;
import poc.model.Media;

/**
 * 
 * @author <cv.jags@gmail.com>
 *
 */
public class CensoringFilterServiceTest {

	private static final String GUID = "GUID";
	private static final String GUIDC = "GUIDC";
	private static final String UNCENSORED = "uncensored";
	private static final String CENSORED = "censored";
	
	private CensoringFilterService service = new CensoringFilterService();
	
	@Test
	@DisplayName("Filter throws assertion error when feed is null")
	void filterFailsWhenFeedIsNull() {
		Assertions.assertThrows(AssertionError.class, () -> service.filter(Maps.newHashMap(), null));
	}
	
	@Test
	@DisplayName("Filter throws assertion error when parameter map is null")
	void filterFailsWhenMapIsNull() {
		Assertions.assertThrows(AssertionError.class, () -> service.filter(null, buildEmptyFeed()));
	}
	
	@Test
	@DisplayName("Filter throws assertion error when parameter map is don't include level parameter")
	void filterFailsWhenMapDoesntIncludeLevel() {
		Assertions.assertThrows(AssertionError.class, () -> service.filter(Collections.emptyMap(), buildEmptyFeed()));
	}
	
	@Test
	@DisplayName("Filter returns the same feed when feed entries attribute is null")
	void filterDoesNothingWhenEntriesIsNull() {
		
		Feed result = service.filter(buildFilterMap(CENSORED), buildEmptyFeed());

		Assertions.assertEquals(buildEmptyFeed(), result);
	}
	
	@Test
	@DisplayName("Filter returns the same feed when feed entries attribute is empty")
	void filterDoesNothingWhenEntriesIsEmpty() {
		
		Feed result = service.filter(buildFilterMap(CENSORED), buildFeed());

		Assertions.assertEquals(buildFeed(), result);
	}
	
	@Test
	@DisplayName("Filter returns the same feed when media from entry is null")
	void filterDoesNothingWhenFeedEntryIsCensoredAndMediaIsNull() {
		
		Feed result = service.filter(buildFilterMap(CENSORED), buildFeed(buildEmptyFeedEntry(CENSORED)));

		Assertions.assertEquals(buildFeed(buildFeedEntry(CENSORED)), result);
	}
	
	@Test
	@DisplayName("Filter returns the same feed when media from entry is empty")
	void filterDoesNothingWhenFeedEntryIsCensoredAndMediaIsEmpty() {
		Feed feed = buildFeed(buildFeedEntry(CENSORED));

		Feed result = service.filter(buildFilterMap(CENSORED), feed);

		Assertions.assertEquals(feed, result);
	}
	
	@Test
	@DisplayName("Filter filters uncensored media when entry is censored and level is censored")
	void filterFiltersUncensored() {
		
		Feed result = service.filter(buildFilterMap(CENSORED), buildFeed(buildFeedEntry(CENSORED, buildCensoredMediaList(GUID))));
		
		Assertions.assertAll("Unensored filter",
				() -> Assertions.assertEquals(1, result.getEntries().get(0).getMedia().size()),
				() -> Assertions.assertEquals(GUIDC, result.getEntries().get(0).getMedia().get(0).getGuid()));
	}
	
	@Test
	@DisplayName("Filter filters censored media when entry is censored and level is uncensored")
	void filterFiltersCensored() {
		Feed result = service.filter(buildFilterMap(UNCENSORED), buildFeed(buildFeedEntry(CENSORED, buildCensoredMediaList(GUID))));
		
		Assertions.assertAll("Censored filter",
				() -> Assertions.assertEquals(1, result.getEntries().get(0).getMedia().size()),
				() -> Assertions.assertEquals(GUID, result.getEntries().get(0).getMedia().get(0).getGuid()));
	}

	@Test
	@DisplayName("Filter returns same feed when entry content classification is null")
	void filterDoesNothingWhenContentClassificationIsNull() {
		
		Feed result = service.filter(buildFilterMap(CENSORED), buildFeed(buildFeedEntry(null, buildCensoredMediaList(GUID))));

		Assertions.assertAll("No filter on null content",
				() -> Assertions.assertEquals(2, result.getEntries().get(0).getMedia().size()),
				() -> Assertions.assertEquals(GUIDC, result.getEntries().get(0).getMedia().get(0).getGuid()),
				() -> Assertions.assertEquals(GUID, result.getEntries().get(0).getMedia().get(1).getGuid()));
	}
	
	@Test
	@DisplayName("Filter returns same feed when entry content classification is empty")
	void filterDoesNothingWhenContentClassificationIsEmpty() {

		Feed result = service.filter(buildFilterMap(CENSORED), buildFeed(buildFeedEntry("", buildCensoredMediaList(GUID))));

		Assertions.assertAll("No filter on empty content",
				() -> Assertions.assertEquals(2, result.getEntries().get(0).getMedia().size()),
				() -> Assertions.assertEquals(GUIDC, result.getEntries().get(0).getMedia().get(0).getGuid()),
				() -> Assertions.assertEquals(GUID, result.getEntries().get(0).getMedia().get(1).getGuid()));
	}
	
	@Test
	@DisplayName("Filter returns same feed when entry content classification is uncesored")
	void filterDoesNothingWhenContentClassificationIsNotCensored() {

		Feed result = service.filter(buildFilterMap(CENSORED), buildFeed(buildFeedEntry(UNCENSORED, buildCensoredMediaList(GUID))));

		Assertions.assertAll("No filter on uncensored content",
				() -> Assertions.assertEquals(2, result.getEntries().get(0).getMedia().size()),
				() -> Assertions.assertEquals(GUIDC, result.getEntries().get(0).getMedia().get(0).getGuid()),
				() -> Assertions.assertEquals(GUID, result.getEntries().get(0).getMedia().get(1).getGuid()));
	}
	
	private Map<String, String> buildFilterMap(String level) {
		return ImmutableMap.<String, String>builder().put("level", level).build();
	}

	private Feed buildEmptyFeed() {
		return Feed.builder().build();
	}
	
	private Feed buildFeed(FeedEntry... entries) {
		return Feed.builder().title("feed fixture").entries(Lists.newArrayList(entries)).entryCount(entries.length)
				.startIndex(1).build();
	}

	private FeedEntry buildEmptyFeedEntry(String contentClass) {
		return FeedEntry.builder().peg$contentClassification(contentClass).build();
	}
	
	private FeedEntry buildFeedEntry(String contentClass, Media... media) {
		return FeedEntry.builder().peg$contentClassification(contentClass).media(Lists.newArrayList(media)).build();
	}
	
	private Media[] buildCensoredMediaList(String guid){
		return new Media[] {buildCensoredMedia(guid), buildMedia(guid)};
	}
	
	private Media buildCensoredMedia(String guid) {
		return buildMedia(guid + "C");
	}

	private Media buildMedia(String guid) {
		return Media.builder().guid(guid).build();
	}
}
