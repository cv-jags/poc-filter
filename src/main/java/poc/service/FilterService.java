/**
 * POC for Starz Play Arabia (June 2018)
 */
package poc.service;

import java.util.Map;

/**
 * 
 * @author <cv.jags@gmail.com>
 *
 */
public interface FilterService<T> {

	/**
	 * Apply a parametric filter to a source.
	 * 
	 * @param params
	 *            the filter parametric expressed as key/value.
	 * @param source
	 *            the source to apply the filter (could be modified).
	 * @return the result of applying the filter with params to source.
	 */
	T filter(Map<String, String> params, T source);
}
