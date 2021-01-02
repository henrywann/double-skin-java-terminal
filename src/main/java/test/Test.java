package test;

import model.Deck;
import model.Player;
import com.blackOlives.Utility;

import java.util.List;


public class Test {

    private Utility util = new Utility();

    public void testPlayerList(int testCases, boolean isSevenPlayers) {
        for (int i = 0; i<testCases; i++) {
            List<Player> playerList = util.assignPlayers(isSevenPlayers);
            if (!isPlayerListCorrect(playerList, isSevenPlayers)) {
                System.out.println("Test Failed. " + i + " tests passed.");
                util.printPlayerList(playerList);
                System.exit(-1);
            }
        }
    }

    public void test() {
        Boolean isShuffledCorrectly = true;
        for (int i = 0; i<100000; i++) {
            Deck initialDeck = new Deck(true);
            List<String> shuffledCards = initialDeck.shuffle(7);
            isShuffledCorrectly = isShuffledCorrectly(shuffledCards);
            if (!isShuffledCorrectly) System.exit(-1);
        }
    }

    private Boolean isShuffledCorrectly(List<String> shuffledCards) {
        int v = 0, p = 0, d = 0, g = 0, k = 0, s = 0, km = 0;
        for (String str: shuffledCards) {
            if (str.equalsIgnoreCase("villager")) v++;
            else if (str.equalsIgnoreCase("police")) p++;
            else if (str.equalsIgnoreCase("doctor")) d++;
            else if (str.equalsIgnoreCase("gunSmith")) g++;
            else if (str.equalsIgnoreCase("killer")) k++;
            else if (str.equalsIgnoreCase("silencer")) s++;
            else if (str.equalsIgnoreCase("killerMaster")) km++;
        }
        return (v == 7 && p == 2 && d == 1 && g == 1 && k == 1 && s == 1 && km ==1);
    }

    private Boolean isPlayerListCorrect(List<Player> playerList, boolean isSevenPlayers) {
        int v = 0, p = 0, d = 0, g = 0, k = 0, s = 0, km = 0;
        boolean isNeutralized = false;
        int playerNumber = 0;
        int cur = 0;
        for (Player player: playerList) {
            String card1 = player.getCard1();
            String card2 = player.getCard2();
            if (player.isNeutralized()) {
                isNeutralized = true;
                playerNumber = cur;
                card1 = "villager";
                card2 = "villager";
            }
            switch (card1) {
                case "villager":
                    v++;
                    break;
                case "police":
                    p++;
                    break;
                case "doctor":
                    d++;
                    break;
                case "gunSmith":
                    g++;
                    break;
                case "killer":
                    k++;
                    break;
                case "silencer":
                    s++;
                    break;
                case "killerMaster":
                    km++;
                    break;
            }
            switch (card2) {
                case "villager":
                    v++;
                    break;
                case "police":
                    p++;
                    break;
                case "doctor":
                    d++;
                    break;
                case "gunSmith":
                    g++;
                    break;
                case "killer":
                    k++;
                    break;
                case "silencer":
                    s++;
                    break;
                case "killerMaster":
                    km++;
                    break;
            }
            cur++;
        }
        if (isSevenPlayers) {
            if (((v == 7 && p == 2 && k==1) || (v==9 && p==1 && k==0)) && d == 1 && g == 1 && s == 1 && km ==1) {
                return true;
            } else {
                System.out.println("Is Neutralized: " + isNeutralized);
                System.out.println("Neutralized player number: " + playerNumber);
                return false;
            }
        } else {
            if ((v==5 && p==2 && d==1 && g==1 && k==2 && s==1) || (v==7 && p==1 && d==1 && g==1 && k==1 && s==1)) {
                return true;
            } else {
                System.out.println("Is Neutralized: " + isNeutralized);
                System.out.println("Neutralized player number: " + playerNumber);
                return false;
            }
        }
    }
}
