package edu.smith.cs.csc212.p4;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SpookyMansion, the game.
 * @author jfoley
 *
 */
public class SpookyMansion implements GameWorld {
	private Map<String, Place> places = new HashMap<>();
	
	/**
	 * Where should the player start?
	 */
	@Override
	public String getStart() {
		return "entranceHall";
	}
	/**
	 * What items does the player have in the beginning?
	 */
	@Override
	public List<String> getItems() {
		List<String> item = Arrays.asList();
		return item;

	}

	/**
	 * This constructor builds our SpookyMansion game.
	 */
	public SpookyMansion() {
		Place entranceHall = insert(
				Place.create("entranceHall", "You are in the grand entrance hall of a large building.\n"
						+ "The front door is locked. How did you get here?"));
		entranceHall.addExit(new Exit("basement", "There are stairs leading down.", false));
		entranceHall.addExit(new Exit("attic", "There are stairs leading up.", false));
		entranceHall.addExit(new Exit("kitchen", "There is a red door.", false));
		entranceHall.addItem("Water");
		
		Place room1 = insert(Place.create("room1", 
				"Sorry, no exit here. You could stay a while if you like though, it gets a little lonely."));
		room1.addExit(new Exit("basement", "Go back.", false));
		
		Place basement = insert(
				Place.create("basement", "You have found the basement of the mansion.\n" + 
		                           "It is darker down here.\n" +
						"You get the sense a secret is nearby, but you only see the stairs you came from."
						));
		basement.addExit(new Exit("entranceHall", "There are stairs leading up.", false));
		basement.addExit(new Exit("room1", "There is a black door ahead.", false));
		basement.addExit(new SecretExit("secretRoom", "A secret door appears, after you?",true));
		basement.addItem("Map"); //to Test the addItem method

		Place attic = insert(Place.create("attic",
				"Something rustles in the rafters as you enter the attic. Creepy.\n" + "It's big up here."));
		attic.addExit(new Exit("entranceHall", "There are stairs leading down.", false));
		attic.addExit(new Exit("attic2", "There is more through an archway", false));
		
		Place room = insert(Place.create("room", 
				"Welcome to the attic bedroom. You look tired, maybe you should get some rest."));
		room.addExit(new Exit("attic2", "Go back.", false));

		Place attic2 = insert(Place.create("attic2", "There's definitely a bat in here somewhere.\n"
				+ "This part of the attic is brighter, so maybe you're safe here."));
		attic2.addExit(new Exit("attic", "There is more back through the archway", false));
		attic2.addExit(new Exit("room", "There is a door ahead.", false ));
		
		Place kitchen = insert(Place.create("kitchen", "You've found the kitchen. You smell old food and some kind of animal."));
		kitchen.addExit(new Exit("entranceHall", "There is a red door.", false));
		kitchen.addExit(new Exit("dumbwaiter", "There is a dumbwaiter.", false));
		
		Place dumbwaiter = insert(Place.create("dumbwaiter", "You crawl into the dumbwaiter. What are you doing?"));
		dumbwaiter.addExit(new Exit("secretRoom", "Take it to the bottom.", false));
		dumbwaiter.addExit(new Exit("kitchen", "Take it to the middle-level.", false));
		dumbwaiter.addExit(new Exit("attic2", "Take it up to the top.", false));
		
		Place secretRoom = insert(Place.create("secretRoom", "You have found the secret room."));
		secretRoom.addExit(new Exit("hallway0", "There is a long hallway.", false));
		secretRoom.addExit(new Exit("basement", "There is a black door", false));
		
		int hallwayDepth = 4;
		int lastHallwayPart = hallwayDepth - 1;
		for (int i=0; i<hallwayDepth; i++) {
			Place hallwayPart = insert(Place.create("hallway"+i, "This is a very long hallway. On the wall is a number: " + (hallwayDepth - i)));
			if (i == 0) {
				hallwayPart.addExit(new Exit("secretRoom", "Go back.", false));
			} else {
				hallwayPart.addExit(new Exit("hallway"+(i-1), "Go back.", false));
			}
			if (i != lastHallwayPart) {
				hallwayPart.addExit(new Exit("hallway"+(i+1), "Go forward.", false));
			} else {
				hallwayPart.addExit(new Exit("crypt", "There is darkness ahead.", false));
			}
		}
		
		Place crypt = insert(Place.terminal("crypt", "You have found the crypt.\n"
				+"It is scary here, but there is an exit to outside.\n"+
				"Maybe you'll be safe out there."));
		
		// Make sure your graph makes sense!
		checkAllExitsGoSomewhere();
	}

	/**
	 * This helper method saves us a lot of typing. We always want to map from p.id
	 * to p.
	 * 
	 * @param p - the place.
	 * @return the place you gave us, so that you can store it in a variable.
	 */
	private Place insert(Place p) {
		places.put(p.getId(), p);
		return p;
	}

	/**
	 * I like this method for checking to make sure that my graph makes sense!
	 */
	private void checkAllExitsGoSomewhere() {
		boolean missing = false;
		// For every place:
		for (Place p : places.values()) {
			// For every exit from that place:
			for (Exit x : p.getVisibleExits()) {
				// That exit goes to somewhere that exists!
				if (!places.containsKey(x.getTarget())) {
					// Don't leave immediately, but check everything all at once.
					missing = true;
					// Print every exit with a missing place:
					System.err.println("Found exit pointing at " + x.getTarget() + " which does not exist as a place.");
				}
			}
		}
		
		// Now that we've checked every exit for every place, crash if we printed any errors.
		if (missing) {
			throw new RuntimeException("You have some exits to nowhere!");
		}
	}

	/**
	 * Get a Place object by name.
	 */
	public Place getPlace(String id, List<String> items) {
		return this.places.get(id);		
	}
	
}
