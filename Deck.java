import java.util.*;

/**
 * two only directions
 */
enum RotateDirection {
    ClockWise, CounterClockWise;

    public RotateDirection reverse() {
        if (this == ClockWise)
            return CounterClockWise;
        else
            return ClockWise;
    }
}

/**
 * game main deck where all card are created and shuffled, all players registerd
 * and cards are provided to players, and the counter where players return their
 * cards
 * 
 * @author M.Safari
 * @version 1399.01.25
 */
public class Deck {
    // first stack fromwhere players draw card
    private Stack<Card> deck;
    // second stack where cards are returned to game deck
    private Stack<Card> counter;
    // a list of players
    private List<Player> players;
    // a list iterator(with the capability of going forward and back) for going
    // through players arraylist
    private ListIterator<Player> current;
    // direction of game in each turn
    private RotateDirection direction;
    // a random object for making random numbers
    private Random rand;

    public Deck(Player... players) {
        // start time of random object
        rand = new Random();
        // filling players list
        this.players = new ArrayList<Player>();
        for (int i = 0; i < players.length; i++) {
            this.players.add(players[i]);
        }
        // starting player
        current = this.players.listIterator(rand.nextInt(this.players.size()));
        // default direction
        this.direction = RotateDirection.ClockWise;
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
        // first card in counter
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
            Iterator<Player> player = players.listIterator(0);
            while (player.hasNext()) {
                player.next().drawCard(deck.pop());
            }

        }
    }

    /**
     * circling the iterator of players list(moving through players list like a
     * circle)
     * 
     * @return
     */
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

    /**
     * adding card to players deck from main deck(deck stack)
     * 
     * @param player
     * @param card
     */
    public void giveCard(Player player) {
        if (!deck.isEmpty())
            player.drawCard(deck.pop());
    }

    /**
     * placing players card in game deck(counter stack)
     * 
     * @param player
     * @param card
     * @return
     */
    public boolean addToCounter(Player player, Card card) {
        // if type is wild card we should check whether it is Wild draw or not
        if (card.isWild()) {
            if (!card.isAction()) {
                // wild draw usage should meet compulsion condition
                if (possibleChoices(player) != null)
                    return false;
            }
            // add some color to our wild card
            prepareWildColor(card);

        } else if (!(counter.peek().doesMatch(card))) {
            return false;
        }
        checkCounter(card);
        counter.add(card);
        return true;
    }

    ///////////////////////////////
    public void checkCounter(Card newCard) {
        Card card = counter.peek();
        if (card.isAction()) {
            if (((ActionCard) card).action == Action.DRAW) {
            } else if (((ActionCard) card).action == Action.WDRAW) {
            }
        }
    }

    ///////////////////////////////
    public int[] possibleChoices(Player player) {

        return null;
    }
    ///////////////////////////////

    public void prepareWildColor(Card card) {
        Scanner sc = new Scanner(System.in);
        Color color = Color.RED;
        System.out.println("\t\tchoose color:\n\t\t1-red\n\t\t2-yellow\n\t\t3-green\n\t\t4-blue");
        do {
            int c = 0;
            c = sc.nextInt();
            if (c > 0 || c <= 4) {
                switch (c) {
                    case 1:
                        color = Color.RED;
                        break;
                    case 2:
                        color = Color.YELLOW;
                        break;
                    case 3:
                        color = Color.GREEN;
                        break;
                    case 4:
                        color = Color.BLUE;
                        break;
                }
                break;
            } else {
                System.err.println("please enter proper number!");
            }
        } while (true);
        ((ColorCard) card).setColor(color);
        sc.close();
    }

}