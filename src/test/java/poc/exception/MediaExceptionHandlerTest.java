/**
 * POC for Starz Play Arabia (June 2018)
 */
package poc.exception;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.google.common.collect.Sets;

import poc.service.CensoringLevel;

/**
 * 
 * @author <cv.jags@gmail.com>
 *
 */
@ExtendWith(SpringExtension.class)
public class MediaExceptionHandlerTest {

	private static final int STATUS_CODE = 999;
	private static final String REASON = "reason";
	private static final String REASON_INVALID_PARAM = "Invalid parameter";
	private static final String REASON_MISSING_PARAM = "Missing parameter";

	private MediaExceptionHandler handler = new MediaExceptionHandler();

	@Test
	@DisplayName("Handle ExternalSystemException returns response from attributes")
	void handleExternalSystemExceptionRertunsResponseWithAttributes() throws IOException {
		ExternalSystemException externalException = new ExternalSystemException(STATUS_CODE, REASON);

		ResponseEntity<String> response = handler.handleExternalSystemException(externalException);

		assertAll("Exception on bad request", 
				() -> assertEquals(STATUS_CODE, response.getStatusCodeValue()),
				() -> assertEquals(REASON, response.getBody()),
				() -> assertEquals(MediaType.TEXT_PLAIN, response.getHeaders().getContentType()));
	}

	@Test
	@DisplayName("Handle MissingServletRequestParameterException return response from exception")
	void handleMissingServletRequestParameterExceptionRertunsResponseWithValidationError() throws IOException {
		MissingServletRequestParameterException missingParamException = new MissingServletRequestParameterException(
				"paramName", "paramType");

		List<ValidationError> response = handler.handleMissingParams(missingParamException);

		assertAll("Exception on bad request", 
				() -> assertEquals(1, response.size()),
				() -> assertEquals(REASON_MISSING_PARAM, response.get(0).getReason()),
				() -> assertEquals(1, response.get(0).getElements().length),
				() -> assertEquals("paramName=paramType", response.get(0).getElements()[0]));
	}

	@Test
	@DisplayName("Handle MethodArgumentTypeMismatchException returns response from exception")
	void handleMethodArgumentTypeMismatchExceptionRertunsResponseWithValidationError() throws IOException, NoSuchMethodException, SecurityException {
		MethodParameter param = new MethodParameter(this.getClass().getMethod("aMethod", new Class<?>[] {String.class}), 0);
		MethodArgumentTypeMismatchException typeMismatchException = new MethodArgumentTypeMismatchException("value", CensoringLevel.class, "key", param, null);

		List<ValidationError> response = handler.handleArgumentTypeMismatch(typeMismatchException);

		assertAll("Exception on bad request", 
				() -> assertEquals(1, response.size()),
				() -> assertEquals(REASON_INVALID_PARAM, response.get(0).getReason()),
				() -> assertEquals(1, response.get(0).getElements().length),
				() -> assertEquals("key=value", response.get(0).getElements()[0]));
	}
	
	@Test
	@SuppressWarnings("unchecked")
	@DisplayName("Handle ConstraintViolation returns response from violations")
	void handleConstraintViolationRertunsResponseWithValidationError() throws IOException {
		ConstraintViolation<String> constraintViolation = Mockito.mock(ConstraintViolation.class);
		Mockito.when(constraintViolation.getMessage()).thenReturn(REASON);
		Mockito.when(constraintViolation.getExecutableParameters()).thenReturn(new Object[] { "key=value" });
		Set<ConstraintViolation<String>> set = Sets.newHashSet(constraintViolation);
		ConstraintViolationException constraintViolationException = new ConstraintViolationException(set);

		List<ValidationError> response = handler.handleContraintViolation(constraintViolationException);

		assertAll("Exception on bad request", 
				() -> assertEquals(1, response.size()),
				() -> assertEquals(REASON, response.get(0).getReason()),
				() -> assertEquals(1, response.get(0).getElements().length),
				() -> assertEquals("key=value", response.get(0).getElements()[0]));
	}
	
	public void aMethod(String a) {}
}
