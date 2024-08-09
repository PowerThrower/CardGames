package blackjack;

import deckSetup.*;
import java.io.*;
import java.util.*;

public class BlackjackEngine {
	private static ArrayList<Card> referenceDeck;
	protected static ArrayList<Card> mainDeck;
	
	protected static int numPlayers;
	protected static int maxPlayers;
	protected static int[] scores;
	protected static int[] acesUnaccountedFor;
	protected static Map<Integer, ArrayList<Card>> cards;
	protected static boolean[] isDone;
	protected static boolean[] blackjack;
	
	public static void initializeDecks() {
		try {
			referenceDeck = Deck.importDeck("SplitDeck.txt");
		} catch (FileNotFoundException e) {
			System.out.print("File Not Found");
		}
		
		mainDeck = (ArrayList<Card>) referenceDeck.clone();
	}
	
	
	public static int maxPlayerCalc() {
		int numCards = 0;
		int deckSumValue = 0;
		for (Card i : referenceDeck) {
			numCards++;
			deckSumValue += assignScore(i);
		}
		int maxPlayersByCards = (numCards/2) - 1;
		int maxPlayersBySumValue = (deckSumValue/21) - 1;
		
		if (maxPlayersByCards <= maxPlayersBySumValue) {
			return maxPlayersByCards;
		} else {
			return maxPlayersBySumValue;
		}
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
		scores = new int[(numPlayers + 1) * 2];
		acesUnaccountedFor = new int[(numPlayers + 1) * 2];
		isDone = new boolean[(numPlayers + 1) * 2];
		blackjack = new boolean[(numPlayers + 1) * 2];
		
		cards = new HashMap<Integer, ArrayList<Card>>();
		for (int i = 0; i <= numPlayers; i++) {
			cards.put(i, new ArrayList<Card>());
		}
		
		Collections.shuffle(mainDeck);
		for (int i = 0; i <= 1; i++) {
			for (int j = 1; j <= numPlayers; j++) {
				drawFromDeck(j, false);
			}
			if (i == 0) {
				drawFromDeck(0, false);
			}
			else {
				drawFromDeck(0, true);
			}
		}
	}
	
	public static void drawFromDeck(int playerNum, boolean hideCard) {
		Card drawn = mainDeck.remove(0);
		if (playerNum != 0) {
			System.out.println("Player " + (playerNum % numPlayers) + " has drawn a " + drawn.getValue() + " of " + drawn.getSuit() + "!");
		}
		else if (!hideCard) {
			System.out.println("The dealer has drawn a " + drawn.getValue() + " of " + drawn.getSuit() + "!");
		} else {
			System.out.println("The dealer has drawn their second card! You cannot see what it is!");
		}
		
		if (!hideCard) {
			scores[playerNum] += assignScore(drawn);
		}
		if (drawn.getValue() == CardValue.ACE) {
			acesUnaccountedFor[playerNum]++;
		}
		cards.get(playerNum).add(drawn);
		
		
		
	}
	
	public static void dealerReveal(boolean start) {
		Card hidden = cards.get(0).get(1);
		if (scores[0] + assignScore(hidden) == 21) {
			System.out.println("The dealer reveals their second card! It is a " + hidden.getValue() + " of " + hidden.getSuit() + "!");
			System.out.println("The dealer has a blackjack!");
			scores[0] += assignScore(hidden);
			blackjack[0] = true;
		} else if (!start) {
			System.out.println("The dealer reveals their second card! It is a " + hidden.getValue() + " of " + hidden.getSuit() + "!");
			scores[0] += assignScore(hidden);
		} else {
			return;
		}
	}
	
	public static void scoreHandler(int playerNum) {
		if (scores[playerNum] == 21 && playerNum != 0) {
			System.out.println("Player " + playerNum + " has a score of 21! They automatically stand!");
			isDone[playerNum] = true;
			return;
		}
		else if (scores[playerNum] > 21 && acesUnaccountedFor[playerNum] == 0) {
			if (playerNum == 0) {
				System.out.println("The dealer has overdrawn!");
				isDone[playerNum] = true;
				return;
			}
			else {
				System.out.println("Player " + playerNum + " has overdrawn! They lose!");
				isDone[playerNum] = true;
				return;
			}
		}
		else if (scores[playerNum] > 21){
			System.out.println("Player " + playerNum + " has overdrawn but has an Ace valued at 11! Reducing value to 1!");
			scores[playerNum] -= 10;
			acesUnaccountedFor[playerNum]--;
			return;
		}
		else {
			return;
		}
	}
	
	public static void evaluateEnd() {
		for (int i = 1; i <= numPlayers * 2; i++) {
			if (cards.get(i) == null) {
				continue;
			}
			
			int playerNum;
			if (i <= numPlayers) {
				playerNum = i;
			}
			else {
				playerNum = i % numPlayers;
			}
			
			if (blackjack[0]) {
				if (blackjack[i]) {
					System.out.println("Both the dealer and Player " + playerNum + " have blackjacks! It's a tie!");
				} else
					System.out.println("Player " + playerNum + " does not have a blackjack! They lose!");
			} else if (blackjack[i]) {
				System.out.println("Player " + playerNum + " wins!");
			} else if (scores[0] <= 21 && scores[i] <= 21) {
				if (scores[0] > scores[i]) {
					System.out.println("Player " + playerNum + " has a lower score than the dealer! Player " + playerNum + " loses!");
				}
				else if (scores[0] == scores[i]) {
					System.out.println("Player " + playerNum + " ties with the dealer!");
				}
				else {
					System.out.println("Player " + playerNum + " wins!");
				}
			} else {
				if (scores[i] <= 21) {
					System.out.println("Player " + playerNum + " wins!");
				} else
					System.out.println("Player " + playerNum + " loses!");
			}
		}
	}
	
	public static void round() {
		System.out.println("The dealer has a known score of " + scores[0] + "!");
		for (int i = 1; i <= numPlayers; i++) {
			System.out.println("Player " + i + " has a score of " + scores[i] + "!");
			if (scores[i] == 21) {
				System.out.println("Player " + i + " has a blackjack! They automatically hold!");
				blackjack[i] = true;
				isDone[i] = true;
			}
		}
		dealerReveal(true);
	
		if (scores[0] == 21) {
			evaluateEnd();
			return;
		}
		else {
			for (int i = 1; i <= numPlayers; i++) {
				boolean isSplit = false;
				Scanner input = new Scanner(System.in);
				
				if (cards.get(i).get(0).getValue() == cards.get(i).get(1).getValue()) {
					System.out.println("Player " + i + " has two cards of the same class! Would you like to Split, Yes or No?");
					String split = input.nextLine();
					if (split.toLowerCase().equals("yes")) {
						isSplit = true;
						cards.put(i + numPlayers, new ArrayList<Card>());
						scores[i] -= assignScore(cards.get(i).get(0));
						scores[i + numPlayers] += assignScore(cards.get(i).get(0));
						if (cards.get(i).get(0).getValue() == CardValue.ACE) {
							acesUnaccountedFor[i]--;
							acesUnaccountedFor[i + numPlayers]++;
						}
						cards.get(i + numPlayers).add(cards.get(i).remove(0));
						drawFromDeck(i, false);
					}
				}
				
				System.out.println("Player " + i + "'s turn!");
				while (isDone[i] == false) {
					System.out.println("Player " + i + " has a score of " + scores[i] + "!");
					System.out.println("Stand or Hit?");
					String choice = input.nextLine();
					
					if (choice.toLowerCase().equals("stand")) {
						isDone[i] = true;
					}
					else if (choice.toLowerCase().equals("hit")) {
						drawFromDeck(i, false);
						scoreHandler(i);
					}
					else {
						System.out.println("I'm sorry, I do not understand.");
					}
				}
				
				if (isSplit) {
					System.out.println("Player " + i + "'s second hand!");
					int j = i + numPlayers;
					
					while (isDone[j] == false) {
						System.out.println("Player " + (j - numPlayers) + " has a score of " + scores[j] + "!");
						System.out.println("Stand or Hit?");
						String choice = input.nextLine();
					
						if (choice.toLowerCase().equals("stand")) {
							isDone[j] = true;
						}
						else if (choice.toLowerCase().equals("hit")) {
							drawFromDeck(j, false);
							scoreHandler(j);
						}
						else {
							System.out.println("I'm sorry, I do not understand.");
						}
					}
				}
			}
			
			dealerReveal(false);
			while (isDone[0] == false) {
				System.out.println("The dealer has a score of " + scores[0] + "!");
				if (scores[0] >= 18) {
					System.out.println("The dealer stands!");
					isDone[0] = true;
				}
				else {
					drawFromDeck(0, false);
					scoreHandler(0);
				}
			}
			evaluateEnd();
		}
	}
	
	public static void game() {
		initializeDecks();
		maxPlayers = maxPlayerCalc();
		if (maxPlayers <= 0) {
			System.out.println("Sorry, this deck cannot be used to play blackjack. Please try another deck.");
			return;
		}
		
		numPlayers = 0;
		Scanner input = new Scanner(System.in);
		
		do {
		System.out.println("For Blackjack, the deck provided can support at most " + maxPlayers + " Players. How many players would you like?");
		String answer = input.nextLine();
			try {
				numPlayers = Integer.parseInt(answer);
			} catch (NumberFormatException e) {
				
			} finally {
				System.out.println("Invalid input.");
			}
		} while (numPlayers <= 0 || numPlayers > maxPlayers);
		deal();
		round();
		input.close();
	}
	
	public static void main(String args[]) {
		game();
	}
	
}