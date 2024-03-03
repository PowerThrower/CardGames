package testing;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.*;
import org.junit.jupiter.api.*;

import deckSetup.*;

public class DeckGenerationTest {
	private static Deck deckStandard;
	
	@BeforeAll
	public static void setUp() {
		try {
			deckStandard = new Deck("StandardDeck.txt");
		} catch (FileNotFoundException e) {
			System.out.print("File Not Found");
		}
	}
	
	@Test
	public void testCardGeneration() {
		ArrayList<Card> gameDeck = deckStandard.getDeck();
		Card[] arrayDeck = new Card[gameDeck.size()];
		arrayDeck = gameDeck.toArray(arrayDeck);
		
		System.out.print(arrayDeck.length);
		assert(arrayDeck.length == 52);
		assert(arrayDeck[0].getSuit() == CardSuit.HEARTS);
		assert(arrayDeck[0].getValue() == CardValue.TWO);
		assert(arrayDeck[5].getSuit() == CardSuit.HEARTS);
		assert(arrayDeck[5].getValue() == CardValue.SEVEN);
		assert(arrayDeck[13].getSuit() == CardSuit.SPADES);
		assert(arrayDeck[13].getValue() == CardValue.TWO);
		assert(arrayDeck[27].getSuit() == CardSuit.DIAMONDS);
		assert(arrayDeck[27].getValue() == CardValue.THREE);
		assert(arrayDeck[51].getSuit() == CardSuit.CLUBS);
		assert(arrayDeck[51].getValue() == CardValue.ACE);
	}
}
