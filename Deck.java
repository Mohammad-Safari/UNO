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
    private int penalties;
    private boolean skip;
    private boolean reverse;
    private boolean draw;
    private boolean wdraw;

    public Deck(Player[] players) {
        // start time of random object
        rand = new Random();
        // filling players list
        this.players = new ArrayList<Player>();
        for (int i = 0; i < players.length; i++) {
            this.players.add(players[i]);
        }
        // starting player
        current = this.players.listIterator(rand.nextInt(players.length));
        // default direction
        this.direction = RotateDirection.ClockWise;
        // preparing cards and shuffling them
        deck = new Stack<Card>();
        {
            Stack<Card> redColorCards = colorCards(Color.RED);
            Stack<Card> yelColorCards = colorCards(Color.YELLOW);
            Stack<Card> greColorCards = colorCards(Color.GREEN);
            Stack<Card> bluColorCards = colorCards(Color.BLUE);
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
                temp.push(deck.pop());
            }
            counter.push(deck.pop());
            while (!temp.empty()) {
                deck.push(temp.pop());
            }
            checkCounter(nextPlayer());
        }
        penalties = 0;
        skip = false;
        reverse = false;
        draw = false;
        wdraw = false;
    }

    /**
     * 
     * @param color
     * @return a stack of 25 specifically colored card
     */
    public Stack<Card> colorCards(Color color) {
        Stack<Card> Cards = new Stack<Card>();
        for (int j = 1; j <= 9; j++) {
            Cards.push(new NumberCard("" + j, j, color));
        }
        for (int i = 0; i < 2; i++) {
            Cards.push(new ActionCard("Skip", 20, color, Action.SKIP));
            Cards.push(new ActionCard("Reverse", 20, color, Action.REVERSE));
            Cards.push(new ActionCard("Draw2+", 20, color, Action.DRAW));
        }
        for (int j = 0; j <= 9; j++) {
            Cards.push(new NumberCard("" + j, j, color));
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
            Cards.push(new WildCard("Wild Color", 50));
            Cards.push(new WildAction("Wild Draw", 50));
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
        // counting all cards
        for (int i = 0; i < n; i++) {
            sum += (cards[i].size());
        }
        for (int i = 0; i < sum; i++) {
            int j = rand.nextInt(2 * n - 1) % n;
            if (cards[j].empty()) {
                i--;
                continue;
            }

            deck.push(cards[j].pop());
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
                current = players.listIterator(players.size());
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
            if (card.isAction()) {
                // wild draw usage should meet compulsion condition
                if (possibleChoices(player).size() != 0)
                    return false;
            }
            // add some color to our wild card
            prepareWildColor(card);

        } else if (!(counter.peek().doesMatch(card))) {
            return false;
        }
        checkCounter(player);
        drawcheck(player, card);
        counter.push(card);
        return true;
    }

    /**
     * applying rules of last card in counter if an action card
     * 
     * @param player
     * @param newCard
     */
    public void checkCounter(Player player) {
        Card card = counter.peek();
        if (card.isAction()) {
            switch (((ActionCard) card).action) {
                case SKIP:
                    if (!skip) {
                        nextPlayer();
                        skip = true;
                        wdraw = false;
                        draw = false;
                        reverse = false;
                    }
                    return;
                case REVERSE:
                    if (!reverse) {
                        direction = direction.reverse();
                        reverse = true;
                        skip = false;
                        wdraw = false;
                        draw = false;

                    }
                    return;
                case DRAW:
                    // if (!draw) {
                    // if (newCard.isAction())
                    // for (int i = 0; i < 2; i++)
                    // player.drawCard(card);
                    // draw = true;
                    // wdraw = false;
                    // reverse = false;
                    // skip = false;
                    // }
                    return;

                case WDRAW:
                    if (!wdraw) {
                        for (int i = 0; i < 4; i++)
                            player.drawCard(card);
                        wdraw = true;
                        draw = false;
                        reverse = false;
                        skip = false;
                    }
                    return;
            }
        }
    }

    ///////////////////////////////
    public ArrayList<Integer> possibleChoices(Player player) {
        Iterator<Card> cardIter = player.getDeck().iterator();
        ArrayList<Integer> possibleChoices = new ArrayList<Integer>();
        Card card;
        while (cardIter.hasNext()) {
            card = cardIter.next();
            if ((card.isWild() && (!card.isAction())) || (counter.peek().doesMatch(card)))
                possibleChoices.add(player.getDeck().indexOf(card));
        }

        return possibleChoices;
    }

    public ArrayList<Integer> possibleWildDraws(Player player) {
        Iterator<Card> cardIter = player.getDeck().iterator();
        ArrayList<Integer> possibleChoices = new ArrayList<Integer>();
        Card card;
        if (possibleChoices(player).size() == 0)
            while (cardIter.hasNext()) {
                card = cardIter.next();
                if (card.isWild() && card.isAction())
                    possibleChoices.add(player.getDeck().indexOf(card));
            }

        return possibleChoices;
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

    public void displayDeck(Player player) {
        if (!players.contains(player)) {
            System.err.println("player does not exist!");
            return;
        }
        //
        ColorCard card = ((ColorCard) counter.peek());
        Color color = card.getColor();
        // showing player list
        System.out.println(Color.RED.color(1) + "player turn:" + Color.RED.color(0));
        for (Player ply : players) {
            System.out.printf(" %5s(%2d) ",
                    (ply.equals(player)) ? (Color.RED.bold() + ply.name + Color.RED.color(0)) : (ply.name),
                    ply.getDeck().size());
        }
        //
        System.out.println("\n\n" + Color.BLUE.color(1) + "playing direction:" + Color.BLUE.color(0)
                + ((direction == RotateDirection.ClockWise)
                        ? (Color.BLUE.underLine() + " → " + Color.BLUE.color(0) + " ←")
                        : (" → " + Color.BLUE.underLine() + " ← " + Color.BLUE.color(0))));
        //
        System.out.println("\n\033[36mtop card:\033[0m");
        // first line
        System.out.println("\t\t\t╔══════╗");
        // 2nd line
        System.out.println("\t\t\t║" + color.color(1) + "██████" + color.color(0) + "║");
        // 3rd line (card color)
        System.out.println(
                "\t\t\t║" + color.color(1) + (String.format("%4.4s", card.name)) + "  " + color.color(0) + "║");
        // 4th line
        System.out.println("\t\t\t║" + color.color(1)
                + (String.format("%4.4s", (card.isAction()) ? ((ActionCard) card).action.character() : "")) + "  "
                + color.color(0) + "║");
        // last line
        System.out.println("\t\t\t╚══════╝");
        player.playerCards();
    }

    public void drawcheck(Player player, Card newCard) {
        Card card = counter.peek();
        if (card.isAction())
            if (((ActionCard) card).action == Action.DRAW)
                if (!draw) {
                    if (newCard.isAction())
                        if (((ActionCard) newCard).action == Action.DRAW) {
                            penalties += 2;
                        } else {
                            penalties = 0;
                        }
                    for (int i = 0; i < 2 + penalties; i++)
                        player.drawCard(card);
                    draw = true;
                    wdraw = false;
                    reverse = false;
                    skip = false;
                }
    }
}