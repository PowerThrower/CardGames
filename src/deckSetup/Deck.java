package deckSetup;

import java.io.*;
import java.util.*;

public class Deck {
	public static ArrayList<Card> importDeck(String setupFile) throws FileNotFoundException {
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
				Card card = new Card(CardSuit.valueOf(suitOfCards[0]), CardValue.valueOf(value));
				gameDeck.add(card);
			}
		}
		return gameDeck;
	}
	
	public static ArrayList<Card> generateDeck(int numCards) {
		return null;
	}
}
