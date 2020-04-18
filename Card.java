/**
 * possible types of color
 */
enum Color {
    RED, YELLOW, GREEN, BLUE, WILD;

    public String color(int whereColor) {
        if (whereColor == 0)
            return "\033[0m";
        whereColor = (whereColor > 0) ? 3 : ((whereColor < 0) ? 4 : 0);
        switch (this) {
            case RED:
                return ("\033[" + whereColor + "1m");
            case GREEN:
                return ("\033[" + whereColor + "2m");
            case YELLOW:
                return ("\033[" + whereColor + "3m");
            case BLUE:
                return ("\033[" + whereColor + "4m");
        }
        return "";
    }

    public String bold() {
        return ("\033[1m" + this.color(1));
    }

    public String underLine() {
        return ("\033[4m" + this.bold());
    }
}

/**
 * possible types of actions
 */
enum Action {
    SKIP, REVERSE, DRAW, WDRAW;

    public String character() {
        switch (this) {
            case SKIP:
                return "⛔";
            case REVERSE:
                return "⇆";
            case DRAW:
                return "2+";
            case WDRAW:
                return "☣";
            default:
                return "NON";
        }
    }
}

/**
 * Card base class
 * 
 * @author M.Safari
 * @version 1399.01.24
 */
abstract public class Card {
    public final String name;
    public final int score;

    public Card(String name, int score) {
        this.name = name;
        this.score = score;

    }

    public boolean isNumber() {
        return (this instanceof NumberCard);
    }

    public boolean isAction() {
        return (this instanceof ActionCard);
    }

    /**
     * checking whether the ard is sort of wild or not
     */
    public boolean isWild() {
        return (this instanceof WildCard || this instanceof WildAction);
    }

    /**
     * matching test with given card!
     * 
     * @param card
     * @return
     */
    public abstract boolean doesMatch(Card card);
}

/**
 * base class of colored card
 */
abstract class ColorCard extends Card {
    protected Color color;

    public ColorCard(String name, int score, Color color) {
        super(name, score);
        this.color = color;
    }

    /**
     * only for wild cards
     * 
     * @param color
     */
    public void setColor(Color color) {
        if (this.color == Color.WILD)
            this.color = color;
    }

    /**
     * checking whether card is colored or not
     */
    public boolean isColored() {
        return (color != Color.WILD);
    }

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * 
     * @param card
     * @return
     */
    public boolean sameColor(Card card) {
        if (card instanceof ColorCard)
            if (isColored())
                return (this.color == ((ColorCard) card).getColor());
        return false;
    }

    abstract public boolean doesMatch(Card card);
}

/**
 * color card with number
 */
class NumberCard extends ColorCard {
    public final int number;

    public NumberCard(String name, int number, Color color) {
        super(name, number, color);
        this.number = number;
    }

    @Override
    public boolean doesMatch(Card card) {
        if (card instanceof ColorCard)
            if (this.sameColor(card))
                return true;
        if (card instanceof NumberCard)
            if (this.number == ((NumberCard) card).number)
                return true;
        if (card.isWild() && ((ColorCard) card).isColored())
            return true;
        return false;
    }
}

/**
 * color card with action
 */
class ActionCard extends ColorCard {
    public final Action action;

    public ActionCard(String name, int score, Color color, Action action) {
        super(name, score, color);
        this.action = action;
    }

    @Override
    public boolean doesMatch(Card card) {
        if (card instanceof ColorCard)
            if (this.sameColor(card))
                return true;
        if (card instanceof ActionCard)
            if (this.action == ((ActionCard) card).action)
                return true;
        if (card.isWild() && ((ColorCard) card).isColored())
            return true;
        return false;
    }
}

/**
 * simple wild card
 */
class WildCard extends ColorCard {
    public WildCard(String name, int score) {
        super(name, score, Color.WILD);
    }

    @Override
    public boolean doesMatch(Card card) {
        if (card instanceof ColorCard)
            if (this.isColored() && this.sameColor(card))
                return true;
        return false;
    }
}

/**
 * wild draw card
 */
class WildAction extends ActionCard {

    public WildAction(String name, int score) {
        super(name, score, Color.WILD, Action.WDRAW);
    }

    @Override
    public boolean doesMatch(Card card) {
        if (card instanceof ColorCard)
            if (this.isColored() && this.sameColor(card))
                return true;
        return false;
    }
}