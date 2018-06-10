/**
 * POC for Starz Play Arabia (June 2018)
 */
package poc.model;

import java.util.List;
import java.util.Map;

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
public class FeedEntry {
	private String id;
	private String guid;
	private Long updated;
	private String title;
	private Object description;
	private Long added;
	private Boolean approved;
	private List<Credit> credits;
	private Map<String, String> descriptionLocalized;
	private Object displayGenre;
	private Object editorialRating;
	private List<Image> imageMediaIds;
	private Object isAdult;
	private List<String> languages;
	private String longDescription;
	private Map<String, String> longDescriptionLocalized;
	private String programType;
	private List<Object> ratings;
	private Object seriesEpisodeNumber;
	private Object seriesId;
	private String shortDescription;
	private Map<String, String> shortDescriptionLocalized;
	private List<String> tagIds;
	private List<Tag> tags;
	private Map<String, Thumbnail> thumbnails;
	private Map<String, String> titleLocalized;
	private Object tvSeasonEpisodeNumber;
	private Object tvSeasonId;
	private Object tvSeasonNumber;
	private int year;
	private List<Media> media;
	private String peg$ExclusiveFrench;
	private String peg$ISOcountryOfOrigin;
	private int peg$arAgeRating;
	private String peg$arContentRating;
	private String peg$availableInSection;
	private String peg$contentClassification;
	private String peg$contractName;
	private String peg$countryOfOrigin;
	private String peg$priorityLevel;
	private String peg$priorityLevelActionandAdventure;
	private String peg$priorityLevelArabic;
	private String peg$priorityLevelChildrenandFamily;
	private String peg$priorityLevelComedy;
	private String peg$priorityLevelDisney;
	private String peg$priorityLevelDisneyKids;
	private String peg$priorityLevelDrama;
	private String peg$priorityLevelKids;
	private String peg$priorityLevelKidsAction;
	private String peg$priorityLevelKidsFunandAdventure;
	private String peg$priorityLevelKidsMagicandDreams;
	private String peg$priorityLevelKidsPreschool;
	private String peg$priorityLevelRomance;
	private String peg$priorityLevelThriller;
	private String peg$productCode;
	private String peg$programMediaAvailability;
	private String peg$testDefaultValue;
}
