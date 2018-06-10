/**
 * POC for Starz Play Arabia (June 2018)
 */
package poc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

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

    @Primary
    @Bean
    public ObjectMapper createCustomObjectMapper() {
      AnnotationIntrospector aiJaxb = new JaxbAnnotationIntrospector(TypeFactory.defaultInstance());
      AnnotationIntrospector aiJackson = new JacksonAnnotationIntrospector();
      return new ObjectMapper().setAnnotationIntrospector(AnnotationIntrospector.pair(aiJaxb, aiJackson))
          .registerModule(new Jdk8Module()).enable(SerializationFeature.INDENT_OUTPUT);
    }
  }
}
