/**
 * POC for Starz Play Arabia (June 2018)
 */
package poc.exception;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 
 * @author <cv.jags@gmail.com>
 *
 */
@Getter	
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@XmlRootElement
public class ValidationError {
	private String reason;
	private Object[] elements;
}
