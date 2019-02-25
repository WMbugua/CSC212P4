package edu.smith.cs.csc212.p4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This represents a place in our text adventure.
 * @author jfoley
 *
 */
public class Place {
	/**
	 * This is a list of places we can get to from this place.
	 */
	private List<Exit> exits;
	/**
	 * This is a list of the secret exits in this place
	 */
	private List<SecretExit> secretExits;
	/**
	 * A list of the items in this place
	 */
	private List<String> items;
	/**
	 * This is the identifier of the place.
	 */
	private String id;
	/**
	 * What to tell the user about this place.
	 */
	private String description;
	/**
	 * Whether reaching this place ends the game.
	 */
	private boolean terminal;
	/**
	 * The current time in the place
	 */
	private GameTime gameTime;
	
	/**
	 * Internal only constructor for Place. Use {@link #create(String, String)} or {@link #terminal(String, String)} instead.
	 * @param id - the internal id of this place.
	 * @param description - the user-facing description of the place.
	 * @param terminal - whether this place ends the game.
	 */
	private Place(String id, String description, boolean terminal) {
		this.id = id;
		this.description = description;
		this.exits = new ArrayList<>();
		this.terminal = terminal;
		this.secretExits = new ArrayList<>();
		this.items = new ArrayList<>();
		this.gameTime = new GameTime(0);
	}
	
	/**
	 * Create an exit for the user to navigate to another Place.
	 * @param exit - the description and target of the other Place.
	 */
	public void addExit(Exit exit) {
		this.exits.add(exit);
		if (exit.isSecret()) {
			secretExits.add((SecretExit) exit);
		}
	}
	/**
	 * Create items in place
	 * @param item - name of item in the place
	 */
	public void addItem(String item) {
		this.items.add(item);
	}
	
	/**
	 * For gameplay, whether this place ends the game.
	 * @return true if this is the end.
	 */
	public boolean isTerminalState() {
		return this.terminal;
	}
	
	/**
	 * The internal id of this place, for referring to it in {@link Exit} objects.
	 * @return the id.
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * The narrative description of this place.
	 * @return what we show to a player about this place.
	 */
	public String getDescription() {
		return this.description;
	}
	/**
	 * Add the items and GameTime in the place to the description
	 */
	public void printDescription() {
		System.out.println(this.description + "The room has "+ this.items + " The time is "+ this.gameTime.getHour());
	}
	/**
	 * Get a view of all the exits from this place
	 * @return a list of all the exits in this place
	 */
     public List<Exit> getAllExits(){
    	 return Collections.unmodifiableList(exits);
     }
	/**
	 * Get a view of the visible exits from this Place, for navigation.
	 * @return all the visible exits from this place.
	 */
	public List<Exit> getVisibleExits() {
		  List<Exit> output = new ArrayList<>();
		  for (Exit e : this.exits) {
		    if (e.isSecret()) {
		      // don't show to player	
		    } else {
		      output.add(e);
		    }
		  }
		  return output;
		}
	/**
	 * Checks if the exit is a secret exit
	 */
	public void search() {
		  for (Exit e : this.exits) {
		    // search makes it not secret any more if it's a SecretExit!
		    e.search();
		  }
		}
	/**
	 * Get a view of the secret exits in this place
	 * @return all the secret exits from this place
	 */
	public List<SecretExit> getSecretExits(){
		return Collections.unmodifiableList(secretExits);
	}
	
	/**
	 * This is a terminal location (good or bad).
	 * @param id - this is the id of the place (for creating {@link Exit} objects that go here).
	 * @param description - this is the description of the place.
	 * @return the Place object.
	 */
	public static Place terminal(String id, String description) {
		return new Place(id, description, true);
	}
	
	/**
	 * Create a place with an id and description.
	 * @param id - this is the id of the place (for creating {@link Exit} objects that go here).
	 * @param description - this is what we show to the user.
	 * @return the new Place object (add exits to it).
	 */
	public static Place create(String id, String description) {
		return new Place(id, description, false);
	}
	/**
	 * Shows list of items in a location
	 * @return all the items in this place
	 */
	public List<String> getItems(){
		return this.items;
		
	}
	/**
	 * Implements what we need to put Place in a HashSet or HashMap.
	 */
	public int hashCode() {
		return this.id.hashCode();
	}
	
	/**
	 * Give a string for debugging what place is what.
	 */
	public String toString() {
		return "Place("+this.id+" with "+this.exits.size()+" exits.)";
	}
	
	/**
	 * Whether this is the same place as another.
	 */
	public boolean equals(Object other) {
		if (other instanceof Place) {
			return this.id.equals(((Place) other).id);
		}
		return false;
	}
	
}
