package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck {

    private List<String> deck;
    public Deck(boolean isSevenPlayers) {
        deck = new ArrayList<String>();
        deck.add("killer");
        deck.add(isSevenPlayers?"killerMaster":"killer");
        deck.add("police");
        deck.add("police");
        deck.add("silencer");
        deck.add("doctor");
        deck.add("gunSmith");
        deck.add("villager");
        deck.add("villager");
        deck.add("villager");
        deck.add("villager");
        deck.add("villager");
        if (isSevenPlayers) {
            deck.add("villager");
            deck.add("villager");
        }
    }

    public List<String> shuffle(int numberOfCards) {
        List<String> result = new ArrayList<>(numberOfCards);
        Random rand = new Random();
        for (int i=numberOfCards; i>0; i--) {
            int index = rand.nextInt(i);
            result.add(this.deck.get(index));
            this.deck.remove(index);
        }
        return result;
    }

    public List<String> getDeck() {
        return deck;
    }

    public void setDeck(List<String> deck) {
        this.deck = deck;
    }
}
