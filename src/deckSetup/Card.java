package deckSetup;

// This class represents a single card in a deck as an object, with an enumerated suit and card class
public class Card {
	protected CardSuit suit;
	protected CardClass value;
	
	public Card(CardSuit suit, CardClass value) {
		this.suit = suit;
		this.value = value;
	}
	
	public CardSuit getSuit() {
		return suit;
	}
	
	public CardClass getValue() {
		return value;
	}
}
