/**
 * POC for Starz Play Arabia (June 2018)
 */
package poc.controller;

import java.util.Map;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableMap;

import poc.model.Feed;
import poc.service.CensoringLevel;
import poc.service.FilterService;
import poc.service.MediaService;
import poc.validation.EmptyMap;

/**
 * 
 * @author <cv.jags@gmail.com>
 *
 */
@RestController
@Validated
@RequestMapping("/media")
public class MediaController {

	private final MediaService defaultMediaService;
	private final FilterService<Feed> censoringFilterService;

	public MediaController(MediaService defaultMediaService, FilterService<Feed> censoringFilterService) {
		this.defaultMediaService = defaultMediaService;
		this.censoringFilterService = censoringFilterService;
	}

	@GetMapping(produces="application/json")
	public Feed noFilter(@RequestParam @EmptyMap(message="{invalid.params}") Map<String, String> allRequestParams) {
		return fetchMedia();
	}

	@GetMapping(params = "filter=censoring", produces="application/json")
	public Feed filterCensoring(@RequestParam("level") CensoringLevel level) {
		return filterMedia(level);
	}

	private Feed fetchMedia() {
		return defaultMediaService.fetch();
	}

	private Feed filterMedia(CensoringLevel level) {
		return censoringFilterService.filter(buildCensoringMap(level), fetchMedia());
	}

	private Map<String, String> buildCensoringMap(CensoringLevel level) {
		return ImmutableMap.<String, String>builder().put("level", level.name()).build();
	}
}
