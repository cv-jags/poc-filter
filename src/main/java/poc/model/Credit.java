/**
 * POC for Starz Play Arabia (June 2018)
 */
package poc.model;

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
public class Credit {
	private String characterName;
	private String creditType;
	private Boolean isInactive;
	private Integer order;
	private String personId;
	private String personName;
}
