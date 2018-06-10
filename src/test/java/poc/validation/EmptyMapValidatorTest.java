/**
 * POC for Starz Play Arabia (June 2018)
 */
package poc.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.assertj.core.util.Maps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 
 * @author <cv.jags@gmail.com>
 *
 */
public class EmptyMapValidatorTest {
	
	private ConstraintValidatorContext context;
	
	private ConstraintValidator<EmptyMap, Map<?,?>> validator = new EmptyMapValidator();
	
	
	@Test
	@DisplayName("isValid returns true when map is null")
	void isValidReturnsTrueWhenMapIsNull() {
		assertTrue(validator.isValid(null, context));
	}
	
	@Test
	@DisplayName("isValid returns true when map is empty")
	void isValidReturnsTrueWhenMapIsEmpty() {
		assertTrue(validator.isValid(Collections.emptyMap(), context));
	}
	
	@Test
	@DisplayName("isValid returns false when map is not empty")
	void isValidReturnsFalseWhenMapIsNotEmpty() {
		assertFalse(validator.isValid(Maps.newHashMap("key", "value"), context));
	}
}
