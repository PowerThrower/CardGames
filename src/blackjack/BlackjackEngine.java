package blackjack;

import deckSetup.*;
import java.io.*;
import java.util.*;

/* This class is a generic implementation of the classic card game of Blackjack, also known as 21.
 * Given a deck in the form of a .txt file, a single round can be played on standard rules. Players
 * and an AI dealer are dealt cards from the deck, with each group of cards known as that player's or
 * dealer's hand, and are assigned scores based on the values of the cards in their hands. The dealer's
 * second card is hidden, unless the value of that card and the dealer's known card is 21, known as a
 * blackjack, in which case the round ends and a player must have also gotten a blackjack to tie.
 * players attempt to get as close as possible to a score of 21 without going over by deciding when
 * Otherwise, each player from 1 onward is given a turn to decide whether or not to draw cards to add to
 * their hand to try to get as close to a score of 21 as possible without going over or overdrawing, an
 * automatic loss. Once the players have finished their turns, the dealer reveals their second card and
 * draws until they have a score greater than 18 or overdraw, at which point players are considered to have won,
 * tied, or lost to the dealer depending on blackjacks, scores, and who has overdrawn.
 * In this implementation, players are allowed to split pairs of cards only when the first two cards they
 * receive have the same class. The dealer is not allowed to split. A tie with the dealer is considered a
 * tie for that player.
 * Simulated betting and graphics are currently not available. */
public class BlackjackEngine extends GameEngine {
	
	// These ArrayLists hold the deck provided, with the latter being the one cards are drawn from.
	private static ArrayList<Card> referenceDeck;
	protected static ArrayList<Card> mainDeck;
	
	// These variables keep track of the number of players and statistics that affect their score and potential to win/lose.
	protected static int numPlayers;
	protected static int maxPlayers;
	protected static int[] scores;
	protected static int[] acesUnaccountedFor;
	protected static Map<Integer, ArrayList<Card>> cards;
	protected static boolean[] isDone;
	protected static boolean[] blackjack;
	
	// initializeDecks imports and assigns a provided deck to the arrayLists for use in the game.
	public void initializeDecks() {
		referenceDeck = Deck.chooseDeck();
		mainDeck = (ArrayList<Card>) referenceDeck.clone();
	}
	
	/* maxPlayerCalc attempts to limit the number of maximum players such that the players and the dealer
	 * can play a single game with the provided deck without running out of cards, returning the smallest
	 * maximum number of players possible for the deck. */
	public int maxPlayerCalc() {
		
		/* For a deck to be usable for blackjack, the number of cards and their collective value must allow
		 * for at least one player as well as the dealer to draw cards up to or past a score of 21. */
		int numCards = 0;
		int deckSumValue = 0;
		
		for (Card i : referenceDeck) {
			numCards++;
			deckSumValue += assignCardScore(i);
		}
		
		/* For blackjack, every player must be able to draw at least four cards, in the event of a split,
		 * while the dealer must be able to draw at least three cards. */
		int maxPlayersByCards = (numCards - 3)/4;
		
		// For blackjack, every player and the dealer must be able to at least reach 21.
		int maxPlayersBySumValue = deckSumValue/21;
		
		// The smaller of the two maximums determines the limit and is the value that is returned.
		if (maxPlayersByCards <= maxPlayersBySumValue) {
			return maxPlayersByCards;
		} else {
			return maxPlayersBySumValue;
		}
	}
	
	/* assignCardScore is a helper method that, given a card, returns its Blackjack-assigned value.
	 * in Blackjack, individual cards are assigned values. The value of the ace card is the only one
	 * that various, changing if its original value of 11 results in a hand with a score higher than
	 * 21. That scenario is handled by the scoreHandler method, and otherwise the value of the ace
	 * is assumed to always be 11. */
	public int assignCardScore(Card card) {
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
			//For now, only standard playing cards are provided values.
			default:
				return 0;
		}
	}
	
	/* deal handles the drawing of cards from the main deck, creating a HashMap
	 * connecting the dealer and players by number to their respective hands of cards. */
	public void deal() {
		/* 1 is added to numPlayers to account for the dealer, whose statistics are stored
		 * at index 0 in the resulting arrays. That value is then doubled to provide ample
		 * space for additional 'players' that will hold the second hands caused by actual
		 * players splitting their original hands should they be able to and decide to. */
		scores = new int[(numPlayers + 1) * 2];
		acesUnaccountedFor = new int[(numPlayers + 1) * 2];
		isDone = new boolean[(numPlayers + 1) * 2];
		blackjack = new boolean[(numPlayers + 1) * 2];
		
		cards = new HashMap<Integer, ArrayList<Card>>();
		for (int i = 0; i <= numPlayers; i++) {
			cards.put(i, new ArrayList<Card>());
		}
		
		// drawFromDeck handles the actual assignment of cards into the HashMap.
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
	
	/* drawFromDeck is a helper function that, given a player/dealer and whether
	 * or not it should be hidden, assigns that card to their deck and updates the
	 * score as appropriate. In Blackjack, the dealer hides the second card they draw. */
	public void drawFromDeck(int playerNum, boolean hideCard) {
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
			scores[playerNum] += assignCardScore(drawn);
		}
		if (drawn.getValue() == CardClass.ACE) {
			acesUnaccountedFor[playerNum]++;
		}
		cards.get(playerNum).add(drawn);
	}
	
	// dealerReveal is a helper function that allows the dealer to reveal their hidden card, updating the score as appropriate.
	public void dealerReveal(boolean start) {
		Card hidden = cards.get(0).get(1);
		if (scores[0] + assignCardScore(hidden) == 21) {
			/* In the event the dealer has a blackjack due to the second card,
			 * they reveal the card, at which point the game immediately ends
			 * and the final results are determined. */
			System.out.println("The dealer reveals their second card! It is a " + hidden.getValue() + " of " + hidden.getSuit() + "!");
			System.out.println("The dealer has a blackjack!");
			scores[0] += assignCardScore(hidden);
			blackjack[0] = true;
		} else if (!start) {
			// Otherwise, the dealer waits until every other player is done before revealing their second card.
			System.out.println("The dealer reveals their second card! It is a " + hidden.getValue() + " of " + hidden.getSuit() + "!");
			scores[0] += assignCardScore(hidden);
		} else {
			return;
		}
	}
	
	// scoreHandler deals with situations that may end the player's/dealer's turn.
	public void scoreHandler(int playerNum) {
		/* If a player, not the dealer, gets a score of 21, their turn is automatically
		 * ended to prevent them from overdrawing, which would be an immediate loss. */
		if (scores[playerNum] == 21 && playerNum != 0) {
			System.out.println("Player " + playerNum + " has a score of 21! They automatically stand!");
			isDone[playerNum] = true;
			return;
		}
		/* If a player or the dealer overdraws, or gets a score over 21, they
		 * automatically lose, regardless of who else overdraws. */
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
		/* If the player overdraws but has an ace that is unaccounted for, the value of that
		 * ace will be automatically reduced from 11 to 1, and the player's turn will continue. */
		else if (scores[playerNum] > 21){
			System.out.println("Player " + playerNum + " has overdrawn but has an Ace valued at 11! Reducing value to 1!");
			scores[playerNum] -= 10;
			acesUnaccountedFor[playerNum]--;
			return;
		}
		// If none of the above occur, return to the player's/dealer's turn.
		else {
			return;
		}
	}
	
	/* evaluateEnd determines who won, lost, or tied at the end of the game, when all players
	 * and the dealer have either stopped drawing, gotten a score of 21, or gone over 21. */
	public void evaluateEnd() {
		for (int i = 1; i <= numPlayers * 2; i++) {
			if (cards.get(i) == null) {
				continue;
			}
			
			// This accounts for split hands, which are stored in the HashMap at values where i is greater than the number of players.
			int playerNum;
			if (i <= numPlayers) {
				playerNum = i;
			}
			else {
				playerNum = i % numPlayers;
			}
			
			/* If the dealer got a blackjack at the start of the game, the player must have also
			 * gotten a blackjack to tie, or else they automatically lose to the dealer. */
			if (blackjack[0]) {
				if (blackjack[i]) {
					System.out.println("Both the dealer and Player " + playerNum + " have blackjacks! It's a tie!");
				} else
					System.out.println("Player " + playerNum + " does not have a blackjack! They lose!");
			}
			// If only the player and not the dealer got a blackjack, the player automatically wins.
			else if (blackjack[i]) {
				System.out.println("Player " + playerNum + " wins!");
			}
			/* Otherwise, the player wins if they have a higher score than the dealer, ties if
			 * the scores are the same, and loses otherwise. */
			else if (scores[0] <= 21 && scores[i] <= 21) {
				if (scores[0] > scores[i]) {
					System.out.println("Player " + playerNum + " has a lower score than the dealer! Player " + playerNum + " loses!");
				}
				else if (scores[0] == scores[i]) {
					System.out.println("Player " + playerNum + " ties with the dealer!");
				}
				else {
					System.out.println("Player " + playerNum + " wins!");
				}
			// In the event that the dealer has overdrawn, the player wins if they have not overdrawn, and loses otherwise.
			} else {
				if (scores[i] <= 21) {
					System.out.println("Player " + playerNum + " wins!");
				} else
					System.out.println("Player " + playerNum + " loses!");
			}
		}
	}
	
	/* round handles the actual playing of the game, a single round where cards are dealt, each player
	 * must decide how they play, the dealer plays, and the end results are determined. */
	public void round() {
		/* At the start, after cards have been dealt, the dealer's score based on their visible card
		 * and each player's score is provided, with blackjacks being announced. */
		System.out.println("The dealer has a known score of " + scores[0] + "!");
		for (int i = 1; i <= numPlayers; i++) {
			System.out.println("Player " + i + " has a score of " + scores[i] + "!");
			if (scores[i] == 21) {
				System.out.println("Player " + i + " has a blackjack! They automatically hold!");
				blackjack[i] = true;
				isDone[i] = true;
			}
		}
		/* If the dealer's second card results in them having a blackjack, the second card is revealed,
		 * the round is ended, and the final results are determined. */
		dealerReveal(true);
		if (scores[0] == 21) {
			evaluateEnd();
			return;
		}
		
		// Otherwise, the first player begins their turn.
		else {
			for (int i = 1; i <= numPlayers; i++) {
				boolean isSplit = false;
				Scanner input = new Scanner(System.in);
				
				// If the player has been dealt two cards with the same value, they are given the option to split their hand.
				if (cards.get(i).get(0).getValue() == cards.get(i).get(1).getValue()) {
					System.out.println("Player " + i + " has two cards of the same class! Would you like to Split, Yes or No?");
					String split = input.nextLine();
					/* If the player says yes, one card remains in the original hand while the other is
					 * assigned to a new hand, with a player index equivalent to the player's index plus
					 * the number of players, and corresponding values are updated as necessary. */
					if (split.toLowerCase().equals("yes")) {
						isSplit = true;
						cards.put(i + numPlayers, new ArrayList<Card>());
						scores[i] -= assignCardScore(cards.get(i).get(0));
						scores[i + numPlayers] += assignCardScore(cards.get(i).get(0));
						if (cards.get(i).get(0).getValue() == CardClass.ACE) {
							acesUnaccountedFor[i]--;
							acesUnaccountedFor[i + numPlayers]++;
						}
						cards.get(i + numPlayers).add(cards.get(i).remove(0));
						// After a split has been performed, the player's first hand automatically receives a new card.
						drawFromDeck(i, false);
					}
				}
				
				// Regardless of whether a split occurs or not, the player's turn begins.
				System.out.println("Player " + i + "'s turn!");
				// The player is not done with their turn until they choose to stop drawing cards, reach a score of 21, or overdraw.
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
				
				/* In the event that the player split their hand, the second hand is handled
				 * immediately after the player is done with their first hand. */
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
			
			/* Once every player's turn has been completed, the dealer's turn begins.
			 * The dealer reveals their second card and draws until they have a score of at least 18 or go over 21. */
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
			
			// Once the dealer's turn ends, the round ends and the results are determined.
			evaluateEnd();
		}
	}
	
	// game is the core method of Blackjack Engine, handling the setup and running of the round .
	public void game() {
		// If the deck cannot be used to play Blackjack, the player is prompted to try another deck and the program immediately ends.
		initializeDecks();
		maxPlayers = maxPlayerCalc();
		if (maxPlayers <= 0) {
			System.out.println("Sorry, this deck cannot be used to play blackjack. Please try another deck.");
			return;
		}
		
		// Otherwise, the player is asked how many players they want, between 1 and the maximum number of players.
		numPlayers = 0;
		Scanner input = new Scanner(System.in);
		
		do {
		System.out.println("For Blackjack, the deck provided can support at most " + maxPlayers + " Players. How many players would you like?");
		System.out.println("Please select an answer between 1 and " + maxPlayers + ".");
		String answer = input.nextLine();
			try {
				numPlayers = Integer.parseInt(answer);
			} catch (NumberFormatException e) {
				System.out.println("Invalid input.");
			}
		} while (numPlayers <= 0 || numPlayers > maxPlayers);
		
		/* Once the deck and number of players have been set, then the cards are dealt from the deck to the players
		 * and the round is played via the appropriate methods calls. */
		deal();
		round();
		input.close();
	}
	
	// For running the game as a separate application
	public static void main(String args[]) {
		BlackjackEngine blackjack = new BlackjackEngine();
		blackjack.game();
	}
}