import java.util.ArrayList;
import java.util.Scanner;

/**
 * the class which creates a game of uno and control it and get main input
 * 
 * @author M.Safari
 * @version 1399.01.30
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        // title
        System.out.printf("\033[1;4;33;4m\t\t\t\t\t\t    UNO    \033[0m\n\n", "");
        System.out.println("\033[1;4;31;47m\t\t\t\t\t\tBy M.Safari\033[0m\n");
        Thread.sleep(2000);
        // getting game players
        Scanner sc = new Scanner(System.in);
        Player[] players;
        // geting human player number
        System.out.println("enter human player number:");
        int hum = 1;
        do {
            hum = sc.nextInt();
            if (hum >= 0 || hum < 6) {
                break;
            }
            System.err.println("please enter an number between 0 and 5(including both)!");
        } while (true);
        // getting bot player number
        System.out.println("enter bot player number:");
        int bot = 1;
        do {
            bot = sc.nextInt();
            if (bot >= 0 || bot < 6) {
                break;
            }
            System.err.println("please enter an number between 0 and 5(including both)!");
        } while (true);
        players = new Player[bot + hum];
        // set name for each player(bot players are named automatically)
        {
            for (int i = 0; i < hum; i++) {
                System.out.println("please enter human player number" + i + 1 + ":");
                String name = sc.next();
                players[i] = new Player(name.length() > 5 ? name.substring(0, 4) : name);
            }
            for (int i = 0; i < bot; i++)
                players[hum + i] = new bot();
        }

        // manual instantiating
        // players = new Player[3];
        // players[0] = new Player("Amir");
        // players[1] = new Player("Mohammad");
        // players[2] = new Player("Saeed");

        // creating main deck of cards and its methods
        Deck gDeck = new Deck(players);

        // holding current player
        // player 0 is not important it select player randomly as deck is created
        Player player = players[0];
        do {
            // player will be selected randomly and will go through list in order according
            // to direction which is clockwise default
            player = gDeck.nextPlayer();
            if (gDeck.checkPenalty(player))
                continue;
            gDeck.displayDeck(player);
            Thread.sleep(500);
            // what is showen is rough players deck
            ArrayList<Integer> setOne, setTwo;
            setOne = gDeck.possibleChoices(player);
            setTwo = gDeck.possibleWildDraws(player);
            // these two set clarifies players possible chosen cards
            // first set is number card action card and wild color card choices
            // the second one keeps wild draw choice only if first set is empty
            if (setOne.size() == 0 && setTwo.size() == 0) {
                // if no cards can be pushed to counter just draw a card as another chance
                System.out.println("you must draw a card!");
                Thread.sleep(1000);
                gDeck.giveCard(player);
                // showing players deck again
                player.playerCards();
                // possible choices should be gained, again
                setOne = gDeck.possibleChoices(player);
                setTwo = gDeck.possibleWildDraws(player);
            }
            // player would choose a card if only he could
            System.out.println("please enter the card number to put:");
            System.out.println("possible choices: ");
            for (int ind : setOne) {
                System.out.print(ind + 1 + " ");
            }
            for (int ind : setTwo) {
                System.out.print(ind + 1 + " ");
            }
            System.out.println();
            // when nothing to present
            if (setOne.size() == 0 && setTwo.size() == 0) {
                System.out.println("nothing possible!");
                continue;
            }
            // which card to put
            // bot decides according to score
            if (player instanceof bot) {
                ((bot) player).chooseCard(gDeck);
                continue;
            }
            // human decides acoording to ??? :)
            int index;
            do {
                index = sc.nextInt() - 1;
                if (index >= 0 && index < player.getDeck().size())
                    if (player.putCard(gDeck, index))
                        break;
                    else
                        System.err.println("can not put this card");
                else {
                    System.err.println("enter number in range!");
                }
            } while (true);

        } while (!player.isDeckEmpty());
        // game ends if current player could empty his/her/it(!) deck!
        sc.close();
        // calculating score
        for (Player ply : players)
            if (!(player.equals(ply)))
                player.setScore(player.getScore() + ply.getScore());
        System.out.println("player " + player.name + " won\nscore: " + player.getScore());
    }
}