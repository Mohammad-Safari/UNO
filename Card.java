enum Color {
    RED, YELLOW, GREEN, BLUE;

    public String color(int whereColor) {
        whereColor = (whereColor > 0) ? 3 : ((whereColor < 0) ? 4 : 0);
        if (whereColor == 0)
            return "\033[0m";
        switch (this) {
            case RED:
                return ("\033[" + whereColor + "1m");
            case YELLOW:
                return ("\033[" + whereColor + "2m");
            case GREEN:
                return ("\033[" + whereColor + "3m");
            case BLUE:
                return ("\033[" + whereColor + "4m");
        }
        return null;
    }
}

enum Action {
    SKIP, REVERSE, DRAW, WDRAW
}

/**
 * Card
 */
public class Card {
    public final String name;
    public final int score;

    public Card(String name, int score) {
        this.name = name;
        this.score = score;

    }

    public boolean isWild() {
        if ((this instanceof WildCard) || (this instanceof WildAction))
            return true;
        return false;
    }
}

class ColorCard extends Card {
    protected Color color;

    public ColorCard(String name, int score, Color color) {
        super(name, score);
        this.color = color;
    }

    public ColorCard(String name, int score) {
        super(name, score);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isColored() {
        return (color != null);
    }
}

class ActionCard extends ColorCard {
    public final Action action;

    public ActionCard(String name, int score, Color color, Action action) {
        super(name, score, color);
        this.action = action;
    }

    public ActionCard(String name, int score, Action action) {
        super(name, score);
        this.action = action;
    }
}

class WildCard extends ColorCard {
    public WildCard(String name, int score) {
        super(name, score);
    }
}

class WildAction extends ActionCard {

    public WildAction(String name, int score) {
        super(name, score, Action.WDRAW);
    }
}