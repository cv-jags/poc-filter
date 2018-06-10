/**
 * POC for Starz Play Arabia (June 2018)
 */
package poc.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @author <cv.jags@gmail.com>
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@XmlAccessorType(XmlAccessType.FIELD)
public class Thumbnail {
	private String url;
	private Integer width;
	private Integer height;
	private String title;
	private List<String> assetTypes;
}
