package blackjack;

import deckSetup.*;
import java.io.*;
import java.util.*;

public class BlackjackEngine {
	private static ArrayList<Card> referenceDeck;
	protected static ArrayList<Card> mainDeck;
	
	protected static int[] scores;
	protected static int[] acesUnaccountedFor;
	protected static Map<Integer, ArrayList<Card>> cards;
	
	public void initializeDecks() {
		try {
			referenceDeck = Deck.importDeck("StandardDeck.txt");
		} catch (FileNotFoundException e) {
			System.out.print("File Not Found");
		}
		
		mainDeck = (ArrayList<Card>) referenceDeck.clone();
	}
	
	public int assignScore(Card card) {
		switch(card.getValue()) {
			case TWO:
				return 2;
			case THREE:
				return 3;
			case FOUR:
				return 4;
			case FIVE:
				return 5;
			case SIX:
				return 6;
			case SEVEN:
				return 7;
			case EIGHT:
				return 8;
			case NINE:
				return 9;
			case TEN: case JACK: case QUEEN: case KING:
				return 10;
			case ACE:
				return 11;
			default:
				return 0;
		}
	}
	
	public void deal(int numPlayers) {
		scores = new int[numPlayers];
		acesUnaccountedFor = new int[numPlayers];
		
		cards = new HashMap<Integer, ArrayList<Card>>();
		for (int i = 0; i <= numPlayers; i++) {
			cards.put(i, new ArrayList<Card>());
		}
		
		for (int i = 0; i <= (numPlayers * 2) + 1; i++) {
			Collections.shuffle(mainDeck);
			Card drawn = mainDeck.remove(0);
			scores[i % (numPlayers + 1)] += assignScore(drawn);
			if (drawn.getValue() == CardValue.ACE) {
				acesUnaccountedFor[i % (numPlayers + 1)]++;
			}
			cards.get(i % (numPlayers + 1)).add(drawn);
		}
	}
	
	public void turn() {
		if(scores[0] == 21) {
			System.out.println("Dealer has 21! You lose!");
			return;
		}
		
	}
	
	public static void main(String args[]) {
		
	}
	
}