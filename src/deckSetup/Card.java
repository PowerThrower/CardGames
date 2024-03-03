package deckSetup;

public class Card {
	protected CardSuit suit;
	protected CardValue value;
	
	public Card(CardSuit suit, CardValue value) {
		this.suit = suit;
		this.value = value;
	}
	
	public CardSuit getSuit() {
		return suit;
	}
	
	public CardValue getValue() {
		return value;
	}
}
