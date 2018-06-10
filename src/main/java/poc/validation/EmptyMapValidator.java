/**
 * POC for Starz Play Arabia (June 2018)
 */
package poc.validation;

import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 
 * @author <cv.jags@gmail.com>
 *
 */
public class EmptyMapValidator implements ConstraintValidator<EmptyMap, Map<?, ?>> {

	@Override
	public void initialize(final EmptyMap annotation) {
		return;
	}

	@Override
	public boolean isValid(final Map<?, ?> map, final ConstraintValidatorContext context) {
		return map == null || map.size() == 0;
	}
}