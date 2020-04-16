import java.util.*;

public class Player {
    public final String name;
    private List<Card> deck;
    private int score;

    public Player(String name) {
        deck = new LinkedList<Card>();
        this.name = name;
    }

    public boolean putCard(Deck gDeck, int index) {

        return false;
    }

    public void drawCard(Card card) {
        deck.add(card);
    }

    public boolean isDeckEmpty() {
        return deck.isEmpty();
    }
}