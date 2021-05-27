package photos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class Photo implements Comparable<Object>, Serializable {

	/**
	 * The file path associated with a certain photo
	 */
	private String filepath;
	/**
	 * A list of the photo's tags
	 */
	private ArrayList<Tag> tags;
	
	/**
	 * Constructs a photo with its file path and an empty list of tags
	 * @param fp - the file path of the photo
	 */
	public Photo(String fp) {
		this.filepath=fp;
		tags = new ArrayList<Tag>();
	}
	
	/**
	 * Returns the file path to the photo
	 * @return the file path to the photo
	 */
	public String getFilePath() {
		return filepath;
	}
	
	/**
	 * Adds a certain tag to the photo if that tag is not yet present
	 * @param t - the tag to be added to the photo
	 * @return whether or not the tag was successfully added
	 */
	public boolean addTag(Tag t) {
		if(!hasTag(t)) {
			tags.add(t);
			return true;
		}
		return false;
	}
	
	/**
	 * Removes a certain tag from the photo if the tag is on the photo
	 * @param t - the tag to be removed from the photo
	 * @return whether or not the tag was successfully removed
	 */
	public boolean removeTag(Tag t) {
		if(hasTag(t)) {
			tags.remove(tags.indexOf(t));
			return true;
		}
		return false;
	}
	
	/**
	 * Returns the list of tags on the photo
	 * @return the list of tags on the photo
	 */
	public ArrayList<Tag> getTags(){
		return tags;
	}
	
	/**
	 * Checks if the photo has a certain tag
	 * @param t - the tag to be checked
	 * @return whether or not the photo has the tag
	 */
	public boolean hasTag(Tag t) {
		for(int i=0; i<tags.size(); i++) {
			if(tags.get(i).equals(t)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Checks if the photo has a certain tag(with completion)
	 * @param t - the tag to be checked
	 * @return whether or not the photo has the tag
	 */
	public boolean hasTagPart(Tag t) {
		for(int i=0; i<tags.size(); i++) {
			if(tags.get(i).partOf(t)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Compares two photos based on their file paths
	 * @param o - the photo to be compared to this one
	 * @return the comparison between the file paths of two photos or -1 if the object is not a photo
	 */
	public int compareTo(Object o) {
		if(!(o instanceof Photo))
			return -1;
		return filepath.compareTo(((Photo)o).getFilePath());
	}
	
	/**
	 * Checks if two photos are equal using compareTo
	 * @param o - the photo to be compared to this one
	 * @return whether or not the two photos are equal
	 */
	public boolean equals(Object o) {
		return this.compareTo(o)==0;
	}
	
}

