/**
 * POC for Starz Play Arabia (June 2018)
 */
package poc.model;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 * @author <cv.jags@gmail.com>
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Feed {
	@XmlElement(name = "$xmlns", required = true)
	private Map<String, String> xmlns;
	private Integer startIndex;
	private Integer itemsPerPage;
	private Integer entryCount;
	private String title;
	private List<FeedEntry> entries;
}