package deckSetup;

import java.io.*;
import java.util.*;

/* This class allows for a deck of cards to be read in and converted into an ArrayList that can then
 * be used to play any of the card games in the engine package.
 * Currently the ability to generate a deck is unavailable.
 */
public class Deck {
	
	/* chooseDeck handles providing the user the decision between importing a deck as a .txt file or generating a new one,
	 * with importDeck and generateDeck handling the actual deck once the user has made a decision. */
	public static ArrayList<Card> chooseDeck() {
		// Because of how the code is currently structured, choice cannot be explicitly closed without breaking the program.
		Scanner choice = new Scanner(System.in);
		System.out.println("Welcome! Would you like to import a deck or generate a one? Enter Import or Generate");
		
		
		String decision = choice.nextLine();
		// Until the user provides a recognizable answer, the following prompt will repeat.
		do {
			if (decision.toLowerCase().equals("import")) {
				try {
					return importDeck();
				} catch (FileNotFoundException e) {
					System.out.println("Sorry, the file you provided could not be found. Please try again.");
					continue;
				}
			}
			else if (decision.toLowerCase().equals("generate")) {
				return generateDeck();
			}
			else {
				System.out.println("I'm sorry, I do not understand.");
			}
		} while(true);
	}
	
	// importDeck handles prompting the user for and reading the resulting deck file into an ArrayList of Cards
	public static ArrayList<Card> importDeck() throws FileNotFoundException {
		Scanner input = new Scanner(System.in);
		System.out.println("Please provide the name of the file, with the .txt at the end");
		String setupFile = input.nextLine();
		ArrayList<Card> gameDeck = new ArrayList<Card>();
		File setup = new File("data/" + setupFile);
		Scanner scanner = new Scanner(setup);
		while (scanner.hasNextLine()) {
			String data = scanner.nextLine();
			String[] suitOfCards = data.split(", ");
			for (String value : suitOfCards) {
				if (value == suitOfCards[0]) {
					continue;
				}
				Card card = new Card(CardSuit.valueOf(suitOfCards[0]), CardRank.valueOf(value));
				gameDeck.add(card);
			}
		}
		return gameDeck;
	}
	
	// generateDeck handles prompting the user for a number of cards to use to create a correspondingly-sized ArrayList of Cards
	// TODO: Implement the method body
	public static ArrayList<Card> generateDeck() {
		return null;
	}
}
