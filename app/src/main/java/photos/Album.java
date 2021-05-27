package photos;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The class representing a photo album
 * @author Kunal Jangam and Max Pandolpho
 */

public class Album implements Comparable<Object>, Serializable {
	
	/**
	 * The name of the album
	 */
	private String name;
	
	/**
	 * The list of photos in the album
	 */
	private ArrayList<Photo> photos;

	/**
	 * Constructs an album with its name, creator and an empty list of photos
	 * @param name - the name of the album
	 */
	public Album(String name) {
		this.name=name;
		photos=new ArrayList<Photo>();
	}
	
	/**
	 * Returns the name of the album
	 * @return the name of the album
	 */
	public String getName() {
		return name;
	}
	

	/**
	 * Sets the name of the album to a new name
	 * @param newName - the new name for the album
	 */
	public void setName(String newName) {
		name=newName;
	}
	
	/**
	 * Returns the list of photos in the album
	 * @return the list of photos in the album
	 */
	public ArrayList<Photo> getPhotos(){
		return photos;
	}
	
	/**
	 * Sets the list of photos to a new list of photos
	 * @param p - the new list of photos for the album
	 */
	public void setPhotos(ArrayList<Photo> p) {
		photos=p;
	}
	
	/**
	 * Returns the number of photos in the album
	 * @return the number of photos in the album
	 */
	public int getNumberOfPhotos() {
		return photos.size();
	}
	
	/**
	 * Checks if a photo is in the album or not
	 * @param p - the photo to be checked
	 * @return whether or not the photo is in the album
	 */
	public boolean hasPhoto(Photo p) {
		for(int i=0; i<photos.size(); i++) {
			if(photos.get(i).equals(p))
				return true;
		}
		return false;
	}
	
	/**
	 * Adds a photo to the album if it is not yet in the album
	 * @param p - the photo to be added to the album
	 * @return whether or not the photo was able to be added
	 */
	public boolean addPhoto(Photo p) {
		if(hasPhoto(p))
			return false;
		photos.add(p);
		return true;
	}
	
	/**
	 * Removes a photo from the album if it is in the album
	 * @param p - the photo to be removed from the album
	 * @return whether or not the photo was removed from the album
	 */
	public boolean removePhoto(Photo p) {
		if(hasPhoto(p)) {
			photos.remove(photos.indexOf(p));
			return true;
		}
			return false;
		
	}
	
	/**
	 * Updates a photo with new information
	 * @param p - the photo to be updated
	 */
	public void updatePhoto(Photo p) {
		if(hasPhoto(p)) {
			photos.set(photos.indexOf(p), p);
		}
	}
	
	/**
	 * Copies a photo from this album to another album if that photo is in this album and can be added to the other album
	 * @param p - the photo to be copied
	 * @param a - the album where the photo is to be copied to
	 * @return whether or not the photo was successfully copied
	 */
	public boolean copyPhoto(Photo p, Album a) {
		if(hasPhoto(p)) {
			if (a.addPhoto(p)){
				return true;
			}
		}
			return false;
	}
	
	/**
	 * Moves a photo from this album to another album if that photo is in this album and can be added to the other album
	 * @param p - the photo to be moved
	 * @param a - the album where the photo is to be moved to
	 * @return whether or not the photo was successfully moved
	 */
	public boolean movePhoto(Photo p, Album a) {
		if(hasPhoto(p)) {
			if(a.addPhoto(p)) {
				photos.remove(photos.indexOf(p));
				return true;
			}
		}
			return false;
	}
	
	/**
	 * Compares the albums based on whether or not they are equal
	 * @param o - the object this album is meant to be compared to
	 * @return 0 if the two albums are equal and -1 otherwise
	 */
	public int compareTo(Object o) {
		if(equals(o))
			return 0;
		return -1;
	}
	
	/**
	 * Checks if two albums have the same name and creator
	 * @param o - the object this album is meant to be compared to
	 * @return whether or not the two albums are equal
	 */
	public boolean equals(Object o) {
		if(!(o instanceof Album))
			return false;
		Album a = (Album) o;
		return name.equals(a.getName());
	}
	
	/**
	 * Returns the name of the album as the string representation of it
	 * @return the name of the album
	 */
	public String toString() {
		return getName();
	}
}
