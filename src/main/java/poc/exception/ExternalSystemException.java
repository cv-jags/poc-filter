/**
 * POC for Starz Play Arabia (June 2018)
 */
package poc.exception;

import static java.lang.String.format;

import lombok.Getter;

/**
 * 
 * @author <cv.jags@gmail.com>
 *
 */
@Getter
public class ExternalSystemException extends RuntimeException {

	private static final long serialVersionUID = 8327986801095535375L;
	
	private final int statusCode;
	private final String reason;
	
	public ExternalSystemException(int statusCode, String reason) {
		super(format("External system error: %s (%d)", reason, statusCode));
		this.statusCode = statusCode;
		this.reason = reason;
	}
}
