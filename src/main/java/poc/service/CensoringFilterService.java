/**
 * POC for Starz Play Arabia (June 2018)
 */
package poc.service;

import static java.util.stream.Collectors.toList;
import static poc.service.CensoringLevel.censored;
import static poc.service.CensoringLevel.valueOf;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import poc.model.Feed;
import poc.model.FeedEntry;
import poc.model.Media;

/**
 * 
 * @author <cv.jags@gmail.com>
 *
 */
@Service
public class CensoringFilterService implements FilterService<Feed> {

	@Override
	public Feed filter(Map<String, String> params, Feed feed) {
		assert (feed != null && params != null);
		assert (params.containsKey("level"));
		return applyFilter(valueOf(params.get("level")), feed);
	}

	private Feed applyFilter(CensoringLevel level, Feed feed) {
		for (FeedEntry entry : withSafeList(feed.getEntries())) {
			applyFilterToEntry(level, entry);
		}
		return feed;
	}

	private void applyFilterToEntry(final CensoringLevel level, FeedEntry entry) {
		if (isCensoredContent(entry)) {
			entry.setMedia(filterMedia(level, entry));
		}
	}

	private boolean isCensoredContent(FeedEntry entry) {
		return "censored".equalsIgnoreCase(entry.getPeg$contentClassification());
	}

	private List<Media> filterMedia(final CensoringLevel level, FeedEntry entry) {
		return withSafeList(entry.getMedia()).stream().filter(m -> isMediaInLevel(level, m)).collect(toList());
	}

	private boolean isMediaInLevel(CensoringLevel level, Media media) {
		return censored.equals(level) == isCensored(media);
	}

	private boolean isCensored(Media media) {
		return media.getGuid().endsWith("C");
	}

	private <T> List<T> withSafeList(List<T> list) {
		return list != null ? list : Collections.emptyList();
	}
}
