package testing;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.*;
import org.junit.jupiter.api.*;

import deckSetup.*;

public class DeckGenerationTest {
	private static ArrayList<Card> deckStandard;
	
	@BeforeAll
	public static void setUp() {
		try {
			deckStandard = Deck.importDeck("StandardDeck.txt");
		} catch (FileNotFoundException e) {
			System.out.print("File Not Found");
		}
	}
	
	@Test
	public void testCardGeneration() {
		Card[] arrayDeck = new Card[deckStandard.size()];
		arrayDeck = deckStandard.toArray(arrayDeck);
		
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
