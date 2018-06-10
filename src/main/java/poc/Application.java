/**
 * POC for Starz Play Arabia (June 2018)
 */
package poc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import poc.exception.BypassErrorHandler;

/**
 *
 * @author <cv.jags@gmail.com>
 *
 */
@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Configuration
  public class AppConfiguration {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder, BypassErrorHandler errorHandler) {
      return builder.requestFactory(OkHttp3ClientHttpRequestFactory.class).errorHandler(errorHandler).build();
    }
  }
}
