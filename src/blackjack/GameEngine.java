package blackjack;

import deckSetup.Card;

/* This class serves as the basis of all card game engines in the package. Any card game must have methods that handle
 * calculating a maximum number of supported players for a given deck, dealing cards to the players, drawing from the
 * deck as necessary, handling relevant rules for a player's score, determining who has won, lost, or tied at the end
 * of the game, and ways to call all of these methods play a round of the game. How each card game implements these
 * methods depends upon the game's rules. */
public abstract class GameEngine {

	public abstract int maxPlayerCalc();
	public abstract void deal();
	public abstract void drawFromDeck(int playerNum, boolean hideCard);
	public abstract void scoreHandler(int playerNum);
	public abstract void evaluateEnd();
	public abstract void round();
	public abstract void game();
}
