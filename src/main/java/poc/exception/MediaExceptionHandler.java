/**
 * POC for Starz Play Arabia (June 2018)
 */
package poc.exception;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author <cv.jags@gmail.com>
 *
 */
@ControllerAdvice
@Component
@Slf4j
public class MediaExceptionHandler {

	private static final String INVALID_PARAMETER = "Invalid parameter";
	private static final String MISSING_PARAMETER = "Missing parameter";

	@ExceptionHandler
	public ResponseEntity<String> handleExternalSystemException(ExternalSystemException exception) {
		logException(exception);
		return ResponseEntity.status(exception.getStatusCode()).contentType(MediaType.TEXT_PLAIN)
				.body(exception.getReason());
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public List<ValidationError> handleMissingParams(MissingServletRequestParameterException exception) {
		logException("Validation exception: ", exception);
		return Lists.newArrayList(toValidationError(exception));
	}

	@ExceptionHandler
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public List<ValidationError> handleArgumentTypeMismatch(MethodArgumentTypeMismatchException exception) {
		logException("Validation exception: ", exception);
		return Lists.newArrayList(toValidationError(exception));
	}

	@ExceptionHandler
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public List<ValidationError> handleContraintViolation(ConstraintViolationException exception) {
		logException("Validation exception: ", exception);
		return exception.getConstraintViolations().stream().map(this::toValidationError).collect(Collectors.toList());
	}

	private void logException(Exception exception) {
		logException("", exception);
	}

	private void logException(String msg, Exception exception) {
		if (log.isDebugEnabled()) {
			log.debug(msg, exception);
		} else {
			log.error(msg + exception.getMessage());
		}
	}

	private <T> ValidationError toValidationError(ConstraintViolation<T> cv) {
		return ValidationError.builder().reason(cv.getMessage()).elements(cv.getExecutableParameters()).build();
	}

	private ValidationError toValidationError(MissingServletRequestParameterException e) {
		return ValidationError.builder().reason(MISSING_PARAMETER)
				.elements(buildElements(e.getParameterName(), e.getParameterType())).build();
	}

	private ValidationError toValidationError(MethodArgumentTypeMismatchException e) {
		return ValidationError.builder().reason(INVALID_PARAMETER).elements(buildElements(e.getName(), e.getValue()))
				.build();
	}

	private Object[] buildElements(String name, Object value) {
		return new Object[] { name + "=" + value };
	}
}
