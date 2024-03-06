package blackjack;

import deckSetup.*;
import java.io.*;
import java.util.*;

public class BlackjackEngine {
	private static ArrayList<Card> referenceDeck;
	protected static ArrayList<Card> mainDeck;
	
	protected static int numPlayers;
	protected static int[] scores;
	protected static int[] acesUnaccountedFor;
	protected static Map<Integer, ArrayList<Card>> cards;
	protected static boolean[] isDone;
	
	public static void initializeDecks() {
		try {
			referenceDeck = Deck.importDeck("StandardDeck.txt");
		} catch (FileNotFoundException e) {
			System.out.print("File Not Found");
		}
		
		mainDeck = (ArrayList<Card>) referenceDeck.clone();
	}
	
	public static int assignScore(Card card) {
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
	
	public static void deal() {
		scores = new int[numPlayers + 1];
		acesUnaccountedFor = new int[numPlayers + 1];
		isDone = new boolean[numPlayers + 1];
		
		cards = new HashMap<Integer, ArrayList<Card>>();
		for (int i = 0; i <= numPlayers; i++) {
			cards.put(i, new ArrayList<Card>());
		}
		
		Collections.shuffle(mainDeck);
		for (int i = 0; i <= 1; i++) {
			for (int j = 0; j <= numPlayers; j++) {
				drawFromDeck(j);
			}
		}
	}
	
	public static void drawFromDeck(int playerNum) {
		Card drawn = mainDeck.remove(0);
		if (playerNum != 0) {
			System.out.println("Player " + playerNum + " has drawn a " + drawn.getValue() + " of " + drawn.getSuit() + "!");
		}
		else {
			System.out.println("The dealer has drawn a " + drawn.getValue() + " of " + drawn.getSuit() + "!");
		}
		
		scores[playerNum] += assignScore(drawn);
		if (drawn.getValue() == CardValue.ACE) {
			acesUnaccountedFor[playerNum]++;
		}
		cards.get(playerNum).add(drawn);
	}
	
	public static void scoreHandler(int playerNum) {
		if (scores[playerNum] == 21) {
			if (playerNum == 0) {
				System.out.println("The dealer has a score 21! They hold!");
				return;
			}
			else {
				System.out.println("Player " + playerNum + " has a score of 21! They hold!");
				isDone[playerNum] = true;
				return;
			}
		}
		else if (scores[playerNum] > 21 && acesUnaccountedFor[playerNum] == 0) {
			if (playerNum == 0) {
				System.out.println("The dealer has overdrawn! All remaining players win!");
				isDone[0] = true;
				return;
			}
			else {
				System.out.println("Player " + playerNum + " has overdrawn! They lose!");
				isDone[playerNum] = true;
				return;
			}
		}
		else if (scores[playerNum] > 21){
			scores[playerNum] -= 10;
			acesUnaccountedFor[playerNum]--;
			return;
		}
		else {
			return;
		}
	}
	
	public static void evaluateEnd() {
		for (int i = 1; i <= numPlayers; i++) {
			if (scores[0] > scores[i]) {
				System.out.println("Player " + i + " has a lower score than the dealer! Player " + i + " loses!");
			}
			else if (scores[0] == scores[i]) {
				System.out.println("Player " + i + " ties with the dealer!");
			}
			else {
				System.out.println("Player " + i + " wins!");
			}
		}
	}
	
	public static void round() {
		if (scores[0] == 21) {
			System.out.println("Too bad! The dealer has 21! Game over!");
			return;
		}
		else {
			System.out.println("The dealer has a score of " + scores[0] + "!");
			for (int i = 1; i <= numPlayers; i++) {
				while (isDone[i] == false) {
					Scanner input = new Scanner(System.in);
					System.out.println("Player " + i + " has a score of " + scores[i] + "!");
					System.out.println("Hold or Draw?");
					String choice = input.nextLine();
					if (choice.toLowerCase().equals("hold")) {
						isDone[i] = true;
					}
					else if (choice.toLowerCase().equals("draw")) {
						drawFromDeck(i);
						scoreHandler(i);
					}
					else {
						System.out.println("I'm sorry, I do not understand.");
					}
				}
			}
			while (isDone[0] == false) {
				System.out.println("The dealer has a score of " + scores[0] + "!");
				if (scores[0] >= 18) {
					System.out.println("The dealer holds!");
					isDone[0] = true;
					evaluateEnd();
				}
				else {
					drawFromDeck(0);
					scoreHandler(0);
				}
			}
		}
	}
	
	public static void game() {
		numPlayers = 0;
		Scanner input = new Scanner(System.in);
		do {
		System.out.println("Number of Players?");
		String answer = input.nextLine();
			try {
				numPlayers = Integer.parseInt(answer);
			} catch (NumberFormatException e) {
				System.out.println("Invalid input.");
			}
		} while (numPlayers == 0);
		initializeDecks();
		deal();
		round();
		input.close();
	}
	
	public static void main(String args[]) {
		game();
	}
	
}