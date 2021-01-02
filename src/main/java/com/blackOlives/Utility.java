package com.blackOlives;

import model.CardMap;
import model.Deck;
import model.Player;

import java.util.ArrayList;
import java.util.List;

public class Utility {

    public List<Player> assignPlayers(boolean isSevenPlayers) {
        int numberOfPlayers = isSevenPlayers?7:6;
        boolean playable = false;
        List<Player> playerList = null;
        while (!playable) {
            playerList = initializePlayerList(numberOfPlayers);
            playable = true;
            Deck initialDeck = new Deck(isSevenPlayers);
            List<String> shuffledCards = initialDeck.shuffle(numberOfPlayers*2);
            CardMap cardMap = new CardMap();
            int goodPeople = 0;
            int offset = 0;
            for (Player p: playerList) {
                p.setCard1(shuffledCards.get(offset));
                p.setCard2(shuffledCards.get(1+offset));
                int acidity = cardMap.getMap().get(p.getCard1()) + cardMap.getMap().get(p.getCard2());
                if (acidity >= 0) {
                    p.setIdentity("G");
                    if (acidity==0) {
                        p.setCategory("villager");
                    } else {
                        p.setCategory("god");
                    }
                    goodPeople++;
                } else {
                    p.setIdentity("B");
                    p.setCategory("B");
                }
                if ((p.getCard1().equalsIgnoreCase("killer") && p.getCard2().equalsIgnoreCase("police")) ||
                        (p.getCard1().equalsIgnoreCase("police") && p.getCard2().equalsIgnoreCase("killer"))) {
                    p.setNeutralized(true);
                }
                if (acidity == -7) {
                    // two killers cannot be one person
                    playable = false;
                    break;
                }
                if (goodPeople == numberOfPlayers-1) {
                    playable = false;
                    break;
                }
                offset+=2;
            }
        }
        return playerList;
    }

    public List<Player> initializePlayerList(int numberOfPlayers) {
        List<Player> playerList = new ArrayList<>();
        for (int i=0; i<numberOfPlayers; i++) {
            playerList.add(new Player());
        }
        return playerList;
    }

    public void printPlayerList(List<Player> playerList) {
        for (Player player: playerList) {
            System.out.println("Player card one: " + player.getCard1() + " | Player card two: " + player.getCard2() +
                    " | Player Identity: " + player.getIdentity());
        }
    }
}
