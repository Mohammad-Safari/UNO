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

    public void playerCards() {
        // first line
        System.out.println();
        for (Card card : deck)
            System.out.print("╔══════╗ ");
        // 2nd line
        System.out.println();
        for (Card card : deck) {
            Color color = ((ColorCard) card).getColor();
            System.out.print("║" + color.color(1) + "██████" + color.color(0) + "║ ");
        }
        // 3rd line (card color)
        System.out.println();
        for (Card card : deck) {
            Color color = ((ColorCard) card).getColor();
            System.out.print(
                    "║" + color.color(1) + (String.format("%4.4s", card.name)) + "  " + color.color(0) + "║ ");
        }
        // 4th line
        System.out.println();
        for (Card card : deck) {
            Color color = ((ColorCard) card).getColor();
            System.out.print("║" + color.color(1)
                    + (String.format("%4.4s", (card.isAction()) ? ((ActionCard) card).action.character() : "")) + "  "
                    + color.color(0) + "║ ");
        }
        // last line
        System.out.println();
        for (Card card : deck) {
            Color color = ((ColorCard) card).getColor();
            System.out.print("╚══════╝ ");
        }
        System.out.println();
    }
}