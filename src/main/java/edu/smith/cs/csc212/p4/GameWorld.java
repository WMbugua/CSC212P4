package edu.smith.cs.csc212.p4;

import java.util.List;

/**
 * This is the interface through which our main method in {@link InteractiveFiction} interacts with different games.
 * @author jfoley
 *
 */
public interface GameWorld {
	/**
	 * What is the id of the starting location for this game?
	 * @return the id of a Place.
	 */
	public String getStart();
	
	/**
	 * What items does the user have?
	 * @return the list of items in a place
	 */
	public List<String> getItems();
	/**
	 * What is the Place for a given id in this game?
	 * @param id - the internal name of the Place.
	 * @param items - the list of items in this place
	 * @return the place object.
	 */
	public Place getPlace(String id, List<String> items);
}
