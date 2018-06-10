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
public class Content {
	private int bitrate;
	private Double duration;
	private String format;
	private int height;
	private String language;
	private int width;
	private String id;
	private String guid;
	private List<String> assetTypeIds;
	private List<String> assetTypes;
	private String downloadUrl;
	private List<Release> releases;
	private String serverId;
	private String streamingUrl;
	private String protectionScheme;
}
