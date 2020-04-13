import java.util.*;

public class Player {
    private List<Card> deck;
    private int score;

    public Player(List<Card> deck) {
        this.deck = deck;
    }

    public boolean putCard(Deck gDeck, int index) {

        return false;
    }

    public void drawCard(Card card) {
        deck.add(card);
    }
}