package photos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * The class representing a regular user
 * @author Kunal Jangam and Max Pandolpho
 */

public class User implements Serializable {
	
	/**
	 * A list of the user's photo albums
	 */
	private ArrayList<Album> albums;
	

	/**
	 * Constructs a user with an empty list of albums
	 */
	public User(){
		albums=new ArrayList<Album>();
	}

	/**
	 * Returns a list of the user's albums
	 * @return the list of the user's albums
	 */
	public ArrayList<Album> getAlbums() {
		return albums;
	}
	
	/**
	 * Creates a new photo album in the user's account if the user already has an album with that name
	 * @param a - the photo album to be added to the user's account
	 * @return whether or not the album was able to be added
	 */
	public boolean createAlbum(Album a) {
		if(hasAlbum(a))
			return false;
		albums.add(a);
		return true;
	}
	
	/**
	 * Removes a photo album from the user's account if it is present
	 * @param a - the photo album to be removed from the user's account
	 * @return whether or not the album was able to be removed
	 */
	public boolean removeAlbum(Album a) {
		if(hasAlbum(a)) {
			albums.remove(a);
			return true;
		}
		return false;
	}
	
	/**
	 * Renames an album with a new name if that album is owned by the user and the user has no album with the new name
	 * @param a - the album to be renamed
	 * @param newName - the new name for the album
	 * @return whether or not the album was able to be renamed
	 */
	public boolean renameAlbum(Album a, String newName) {
		if(hasAlbum(a) && !hasAlbum(new Album(newName))) {
			albums.get(albums.indexOf(a)).setName(newName);
			return true;
		}
		return false;
	}
	
	/**
	 * Updates a photo album in the user's account
	 * @param a - the album to be updated
	 */
	public void updateAlbum(Album a) {
		if(hasAlbum(a)) {
			albums.set(albums.indexOf(a), a);
		}
	}

	/**
	 * Checks if the user has an album with a certain album's name
	 * @param album - the album to be checked for if the user has it
	 * @return whether or not the user has an album with this name
	 */
	public boolean hasAlbum(Album album) {
		for(Album a : albums) {
			if(a.equals(album))
				return true;
		}
		return false;
	}

	/**
	 * Search through the user's photo albums for photos with a certain tag
	 * @param t - the tag to be checked for photos that have it
	 * @return the list of photos with the tag
	 */
	public ArrayList<Photo> searchByTag(Tag t){
		ArrayList<Photo> p = new ArrayList<Photo>();
		for(int i=0; i<albums.size(); i++) {
			ArrayList<Photo> photos=albums.get(i).getPhotos();
			for(int x=0; x<photos.size(); x++){
				if(photos.get(x).hasTagPart(t))
					p.add(photos.get(x));
			}
		}
		return p;

	}
	
	/**
	 * Search through the user's photo albums for photos with both or one of two tags, depending on the chosen operator
	 * @param t1 - the first tag
	 * @param t2 - the second tag
	 * @param combinationType - the conjunction "and" or "or" to specify how the two tags should be evaluated
	 * @return the list of photos with tag 1 and/or tag 2, depending on the chosen operator
	 */
	public ArrayList<Photo> searchByCombination(Tag t1, Tag t2, String combinationType){
		ArrayList<Photo> p = new ArrayList<Photo>();
		if(combinationType.equals("and")){
			for(int i=0; i<albums.size(); i++) {
				ArrayList<Photo> photos=albums.get(i).getPhotos();
				for(int x=0; x<photos.size(); x++){
					if(photos.get(x).hasTagPart(t1)
						&&photos.get(x).hasTagPart(t2))
						p.add(photos.get(x));
				}
			}
		}
		else if(combinationType.equals("or")){
			for(int i=0; i<albums.size(); i++) {
				ArrayList<Photo> photos=albums.get(i).getPhotos();
				for(int x=0; x<photos.size(); x++){
					if(photos.get(x).hasTagPart(t1)
						||photos.get(x).hasTagPart(t2))
						p.add(photos.get(x));
				}
			}
		}
		else return null;
		return p;
	}


}