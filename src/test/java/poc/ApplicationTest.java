/**
 * POC for Starz Play Arabia (June 2018)
 */
package poc;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import poc.controller.MediaController;
import poc.data.MediaClient;
import poc.exception.BypassErrorHandler;
import poc.exception.MediaExceptionHandler;
import poc.service.CensoringFilterService;
import poc.service.DefaultMediaService;

/**
 *
 * @author <cv.jags@gmail.com>
 *
 */
public class ApplicationTest {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(Application.class))
			.withPropertyValues("data.base_uri", "data.base_uri", "data.path_uri", "data.path_uri");

	@Test
	@DisplayName("Context has instances for all defined beans as singleton")
	public void contextHasInstancedBeansAsSingleton() {
		this.contextRunner.run((context) -> {
			assertThat(context).hasSingleBean(MediaController.class).hasSingleBean(MediaClient.class)
					.hasSingleBean(BypassErrorHandler.class).hasSingleBean(MediaExceptionHandler.class)
					.hasSingleBean(CensoringFilterService.class).hasSingleBean(DefaultMediaService.class)
					.hasSingleBean(RestTemplate.class).hasSingleBean(ObjectMapper.class);
		});
	}
}
