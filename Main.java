import java.util.ArrayList;
import java.util.Scanner;

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
        {
            for (int i = 0; i < hum; i++) {
                System.out.println("please enter human player number" + i + 1 + ":");
                String name = sc.next();
                players[i] = new Player(name.length() > 5 ? name.substring(0, 4) : name);
            }
            for (int i = 0; i < bot; i++)
                players[hum + i] = new bot();
        }
        // players = new Player[3];
        // players[0] = new Player("Amir");
        // players[1] = new Player("Mohammad");
        // players[2] = new Player("Saeed");
        Deck gDeck = new Deck(players);

        // holding current player
        Player player = players[0];
        do {
            player = gDeck.nextPlayer();
            if (gDeck.checkPenalty(player))
                continue;
            gDeck.displayDeck(player);
            Thread.sleep(500);
            //
            ArrayList<Integer> setOne, setTwo;
            setOne = gDeck.possibleChoices(player);
            setTwo = gDeck.possibleWildDraws(player);
            //
            if (setOne.size() == 0 && setTwo.size() == 0) {
                System.out.println("you must draw a card!");
                Thread.sleep(1000);
                gDeck.giveCard(player);
                player.playerCards();
                setOne = gDeck.possibleChoices(player);
                setTwo = gDeck.possibleWildDraws(player);
            }
            //
            System.out.println("please enter the card number to put:");
            System.out.println("possible choices: ");
            for (int ind : setOne) {
                System.out.print(ind + 1 + " ");
            }
            for (int ind : setTwo) {
                System.out.print(ind + 1 + " ");
            }
            System.out.println();
            if (setOne.size() == 0 && setTwo.size() == 0) {
                System.out.println("nothing possible!");
                continue;
            }
            // which card to put
            // bot
            if (player instanceof bot) {
                ((bot) player).chooseCard(gDeck);
                continue;
            }
            // human
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
        // game ends
        sc.close();
        // calculating score
        for (Player ply : players)
            if (!(player.equals(ply)))
                player.setScore(player.getScore() + ply.getScore());
        System.out.println("player " + player.name + " won\nscore: " + player.getScore());
    }
}