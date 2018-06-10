
/**
 *
 */
package poc.data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import poc.model.Feed;

/**
 *
 * @author <cv.jags@gmail.com>
 *
 */
@Slf4j
@Component
public class MediaClient {

    private final RestTemplate template;
    private final String uri;

    /**
     * Constructor
     *
     * @param template the restTemplate component
     * @param baseUri the base URI for request with schema, host, port and optionally initial path
     */
    public MediaClient(RestTemplate template, @Value("${data.base_uri}") String baseUri,
            @Value("${data.path_uri}") String pathUri) {
        this.template = template;
        this.uri = baseUri + pathUri;
    }

    /**
     * Invoke the data end-point
     *
     * @return the feed returned by the end-point
     */
    public Feed getData() {
        return invokeDataService().getBody();
    }

    private ResponseEntity<Feed> invokeDataService() {
        ResponseEntity<Feed> response = template.getForEntity(uri, Feed.class);
        log.debug("Response - {}", response.toString());
        return response;
    }
}
