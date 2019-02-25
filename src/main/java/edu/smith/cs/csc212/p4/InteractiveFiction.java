package edu.smith.cs.csc212.p4;

import java.util.ArrayList;
import java.util.List;

/**
 * This is our main class for P4. It interacts with a GameWorld and handles user-input.
 * @author jfoley
 *
 */
public class InteractiveFiction {

	/**
	 * This is where we play the game.
	 * @param args
	 */
	public static void main(String[] args) {
		// This is a text input source (provides getUserWords() and confirm()).
		TextInput input = TextInput.fromArgs(args);

		// This is the game we're playing.
		GameWorld game = new SpookyMansion();
		
		// This is the current location of the player (initialize as start).
		// Maybe we'll expand this to a Player object.
		String place = game.getStart();
		
		//This is the current list of items the player has
		List<String> items = game.getItems();
		
		//This is the current time for the player
		GameTime gameTime = new GameTime(0);
		
		// Play the game until quitting.
		// This is too hard to express here, so we just use an infinite loop with breaks.
		while (true) {
			// Print the description of where you are.
			Place here = game.getPlace(place, items);
			System.out.println(here.getDescription());
			
			//Every time the player makes a move, increase the time by one hour
			//Print out the time after the description of the room
			gameTime.increaseHour();

			// Game over after print!
			if (here.isTerminalState()) {
				break;
			}

			// Show a user the ways out of this place.
			List<SecretExit> secretExits = here.getSecretExits();
			List<Exit> exits = here.getAllExits();
			
			//List of things the user has
			List<String> things = new ArrayList<>();
			
			//A list of items in the room
			List<String> stuff = here.getItems();
			
			for (int i=0; i<exits.size(); i++) {
			    Exit e = exits.get(i);
			    if (!e.isSecret()) {
			    	System.out.println(" ["+i+"] " + e.getDescription());
			    }
			}
			// Figure out what the user wants to do, for now, only "quit" is special.
			List<String> words = input.getUserWords(">");
			if (words.size() == 0) {
				System.out.println("Must type something!");
				continue;
			} else if (words.size() > 1) {
				System.out.println("Only give me 1 word at a time!");
				continue;
			}
			
			// Get the word they typed as lowercase, and no spaces.
			String action = words.get(0).toLowerCase().trim();
			
			//allow player to quit by typing quit, q or escape
			if (action.equals("quit") || action.equals("q") || action.equals("escape")) {
				if (input.confirm("Are you sure you want to quit?")) {
					break;
				} else {
					continue;
				}
			}
			//if the user types rest, allow them a 2-hour rest, advance the game by two hours
			//By typing rest, they're already increasing thr time by 1 hour
			//So you only need one more
			if (action.equals("rest")) {
				gameTime.increaseHour();
				continue;
			}
			
			//if the user types in take, give them all the items in the place
			if (action.equals("take")) {
				if (!stuff.isEmpty()) {
					things.addAll(stuff);//works here
					stuff.clear();
				}
				else {
					System.out.println("There are no items in this room.");
				}
				continue;
			}
			//If the user asks, show them what they have so far
			if (action.equals("stuff")) {
				if (things.isEmpty()) { //doesn't work here
					System.out.println("You have nothing");
				}
				else {
					System.out.println(things);
				}
				continue;
			}
			
			//if the user types search, show them the secret exits in this place
			if (action.equals("search")) {
				for (int i=0; i<secretExits.size(); i++) {
				    SecretExit e = secretExits.get(i);
				    e.isSecret = false;
				}
				continue;
			}
				
				
			// From here on out, what they typed better be a number!
			Integer exitNum = null;
			try {
				exitNum = Integer.parseInt(action);
			} catch (NumberFormatException nfe) {
				System.out.println("That's not something I understand! Try a number!");
				continue;
			}
			
			if (exitNum < 0 || exitNum > exits.size()) {
				System.out.println("I don't know what to do with that number!");
				continue;
			}
			
			// Move to the room they indicated.
			//If the exit they chose is not in the exits list, check the secret exits
			Exit destination = exits.get(exitNum);
			place = destination.getTarget();
			
			
		}

		// You get here by "quit" or by reaching a Terminal Place.
		System.out.println(">>> GAME OVER <<<");
		//Show them the amount of time they spent playing
		System.out.println("You spent "+ gameTime.timeSpent()+" hours playing.");
	}

}
