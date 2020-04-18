import java.util.*;

/**
 * a class of player containing players cards(deck), and score
 * 
 * @author M.Safari
 * @version 1399.01.24
 */
public class Player {
    // player name
    public final String name;
    // players cards
    private List<Card> deck;
    // players score
    private int score;

    public Player(String name) {
        deck = new LinkedList<Card>();
        this.name = name;
        score = 0;
    }

    /**
     * checking existance of card in players deck and sending the card so that main
     * deck checks possibility of putting that card on counter
     * 
     * @param gDeck
     * @param index
     * @return
     */
    public boolean putCard(Deck gDeck, int index) {
        if ((!deck.isEmpty()) || (index >= deck.size() || index < 0)) {
            if (gDeck.addToCounter(this, deck.get(index))) {
                score += deck.get(index).score;
                deck.remove(index);
                return true;
            }
        }
        return false;
    }

    /**
     * taking a card from game deck
     * 
     * @param card
     */
    public void drawCard(Card card) {
        deck.add(card);
    }

    /**
     * checking whether players hands are empty or not
     * 
     * @return
     */
    public boolean isDeckEmpty() {
        return deck.isEmpty();
    }

    /**
     * @return the deck
     */
    public List<Card> getDeck() {
        return deck;
    }
}