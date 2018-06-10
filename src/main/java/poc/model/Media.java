/**
 * POC for Starz Play Arabia (June 2018)
 */
package poc.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

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
@XmlAccessorType(XmlAccessType.FIELD)
public class Media {
	private String id;
	private String title;
	private String guid;
	private String ownerId;
	private Long availableDate;
	private Long expirationDate;
	private List<Object> ratings;
	private List<Content> content;
	private String restrictionId;
	private String availabilityState;
}
