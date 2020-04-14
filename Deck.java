import java.util.*;

public class Deck {
    private Stack<Card> deck;
    private Stack<Card> counter;
    private Player[] players;

    public Deck(Player... players) {
        //
        this.players = new Player[players.length];
        for (int i = 0; i < players.length; i++) {
            this.players[i] = players[i];
        }
        //
        deck = new Stack<Card>();
        {
            Stack<Card> redColorCards = colorCards(Color.RED);
            Stack<Card> yelColorCards = colorCards(Color.YELLOW);
            Stack<Card> greColorCards = colorCards(Color.GREEN);
            Stack<Card> bluColorCards = colorCards(Color.YELLOW);
            Stack<Card> wildCards = new Stack<Card>();
            shuffle(redColorCards, yelColorCards, greColorCards, bluColorCards, wildCards);
        }
    }

    public Stack<Card> colorCards(Color color) {
        Stack<Card> Cards = new Stack<Card>();
        Cards.add(new ColorCard("0", 0, color));
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 9; j++) {
                Cards.add(new ColorCard("" + j, j, color));
            }
            Cards.add(new ActionCard("Skip", 20, color, Action.SKIP));
            Cards.add(new ActionCard("Reverse", 20, color, Action.REVERSE));
            Cards.add(new ActionCard("Draw2+", 20, color, Action.DRAW));

        }
        return Cards;
    }

    public Stack<Card> wildCards() {
        Stack<Card> Cards = new Stack<Card>();
        for (int i = 0; i < 4; i++) {
            Cards.add(new WildCard("Wild Color", 50));
            Cards.add(new WildAction("Wild Draw", 50));
        }
        return Cards;
    }

    /**
     * shuffling couple of stacks then putting in deck stack
     * 
     * @param cards
     */
    public void shuffle(Stack<Card>... cards) {
        Random rand = new Random();
        int n = cards.length;
        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum += (cards[i].size());
        }
        for (int i = 0; i < sum; i++) {
            int j = rand.nextInt(n);
            if (cards[j].empty()) {
                i--;
                continue;
            }
            deck.add(cards[j].pop());
        }
    }
}