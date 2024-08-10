package testing;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.*;
import org.junit.jupiter.api.*;

import deckSetup.*;

/* This class serves as a means to test that decks are being properly read and converted into ArrayLists,
 * such that the resulting Cards have predictable and expected suits and ranks.
 */
public class DeckGenerationTest {
	private static ArrayList<Card> deckStandard;
	
	@BeforeAll
	public static void setUp() {
		try {
			deckStandard = Deck.importDeck();
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
		assert(arrayDeck[0].getRank() == CardRank.TWO);
		assert(arrayDeck[5].getSuit() == CardSuit.HEARTS);
		assert(arrayDeck[5].getRank() == CardRank.SEVEN);
		assert(arrayDeck[13].getSuit() == CardSuit.SPADES);
		assert(arrayDeck[13].getRank() == CardRank.TWO);
		assert(arrayDeck[27].getSuit() == CardSuit.DIAMONDS);
		assert(arrayDeck[27].getRank() == CardRank.THREE);
		assert(arrayDeck[51].getSuit() == CardSuit.CLUBS);
		assert(arrayDeck[51].getRank() == CardRank.ACE);
	}
}
