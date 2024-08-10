package deckSetup;

// This class represents a single card in a deck as an object, with an enumerated suit and card rank
public class Card {
	protected CardSuit suit;
	protected CardRank rank;
	
	public Card(CardSuit suit, CardRank rank) {
		this.suit = suit;
		this.rank = rank;
	}
	
	public CardSuit getSuit() {
		return suit;
	}
	
	public CardRank getRank() {
		return rank;
	}
}
