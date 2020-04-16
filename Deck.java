import java.util.*;

enum RotateDirection {
    ClockWise, CounterClockWise;

    public RotateDirection reverse() {
        if (this == ClockWise)
            return CounterClockWise;
        else
            return ClockWise;
    }
}

public class Deck {
    private Stack<Card> deck;
    private Stack<Card> counter;
    private List<Player> players;
    private ListIterator<Player> current;
    private RotateDirection direction;
    private Random rand;

    public Deck(Player... players) {
        rand = new Random();
        // filling players list
        this.players = new ArrayList<Player>();
        for (int i = 0; i < players.length; i++) {
            this.players.add(players[i]);
        }
        // preparing cards and shuffling them
        deck = new Stack<Card>();
        {
            Stack<Card> redColorCards = colorCards(Color.RED);
            Stack<Card> yelColorCards = colorCards(Color.YELLOW);
            Stack<Card> greColorCards = colorCards(Color.GREEN);
            Stack<Card> bluColorCards = colorCards(Color.YELLOW);
            Stack<Card> wildCards = wildCards();
            shuffle(redColorCards, yelColorCards, greColorCards, bluColorCards, wildCards);
        }
        // providing cards for players
        CardProvider();
        // first card
        counter = new Stack<Card>();
        {
            Stack<Card> temp = new Stack<Card>();
            while (deck.peek().isWild()) {
                temp.add(deck.pop());
            }
            counter.add(deck.pop());
            while (!temp.empty()) {
                deck.add(temp.pop());
            }
        }
        // default direction
        this.direction = RotateDirection.ClockWise;
        // starting player
        current = this.players.listIterator(rand.nextInt(this.players.size()));
    }

    /**
     * 
     * @param color
     * @return a stack of 25 specifically colored card
     */
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

    /**
     * 
     * @return a stack of 8 wild cards
     */
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

    /**
     * providing 7 cards for each player from deck
     * 
     */
    public void CardProvider() {
        for (int i = 0; i < 7; i++) {
            Iterator<Player> player = players.iterator();
            while (player.hasNext()) {
                player.next().drawCard(deck.pop());
            }

        }
    }

    public Player nextPlayer() {
        if (direction == RotateDirection.ClockWise) {
            if (!current.hasNext())
                current = players.listIterator(0);
            return current.next();
        } else {
            if (!current.hasPrevious())
                current = players.listIterator(players.size() - 1);
            return current.previous();
        }
    }

    public boolean putCard(){}
}