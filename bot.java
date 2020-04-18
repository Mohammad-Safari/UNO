import java.util.ArrayList;

public class bot extends Player {
    private static int number = 0;

    public bot() {
        super("bot " + number);
        number++;
    }

    public void chooseCard(Deck deck) {
        ArrayList<Integer> setOne, setTwo;
        setOne = deck.possibleChoices(this);
        setTwo = deck.possibleWildDraws(this);
        int index;
        if (!setOne.isEmpty()) {
            index = setOne.get(0);
            for (int i : setOne) {
                if (this.deck.get(i).score > this.deck.get(index).score)
                    index = i;
            }
            putCard(deck, index);
            return;
        }
        if (!setTwo.isEmpty()) {
            index = setTwo.get(0);
            for (int i : setTwo) {
                if (this.deck.get(i).score > this.deck.get(index).score)
                    index = i;
            }
            putCard(deck, index);
            return;
        }

    }
}