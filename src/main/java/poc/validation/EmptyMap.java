/**
 * POC for Starz Play Arabia (June 2018)
 */
package poc.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 
 * @author <cv.jags@gmail.com>
 *
 */
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = EmptyMapValidator.class)
public @interface EmptyMap {
   String message() default "empty.map";

   Class<?>[] groups() default {};

   Class<? extends Payload>[] payload() default {}; 
}

