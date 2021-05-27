package photos;

import java.io.Serializable;

public class Tag implements Comparable<Object>, Serializable {
	
	/**
	 * The name of the tag
	 */
	public String tagName;
	
	/**
	 * The value of the tag
	 */
	public String tagValue;
	
	/**
	 * Constructs a tag based on its name and value
	 * @param tagName - the name of the tag
	 * @param tagValue - the value of the tag
	 */
	public Tag(String tagName, String tagValue) {
		this.tagName=tagName;
		this.tagValue=tagValue;
	}
	
	/**
	 * Compares two tags based on their names and values
	 * @param o - the photo to be compared to this one
	 * @return 0 if the two tags are equal, -1 if the other object is not a tag or 1 otherwise
	 */
	public int compareTo(Object o) {
		if(! (o instanceof Photo))
			return -1;
		if(this.tagName.equals(((Tag)o).tagName)&&this.tagValue.equals(((Tag)o).tagValue))
			return 0;
		else return 1;
	}
	
	/**
	 * Checks if two tags are equal according to their names and values
	 * @param o - the tag to be compared to this one
	 * @return whether or not the two tags are equal
	 */
	public boolean equals(Object o) {
		if(! (o instanceof Tag))
			return false;
		return (this.tagName.equals(((Tag)o).tagName)&&this.tagValue.equals(((Tag)o).tagValue));
	}
	/**
	 * Checks if this tag can be completed using the paramater
	 * @param o - the tag to be compared to this one
	 * @return whether or not the paramater can be extended to the tag
	 */
	public boolean partOf(Object o){
		if(! (o instanceof Tag))
			return false;
		return (this.tagName.equals(((Tag)o).tagName)&&(this.tagValue.indexOf(((Tag)o).tagValue))==0);

	}
}
