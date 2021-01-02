package model;

public class Player {
    private String card1;
    private String card2;
    private String identity;
    private String category; // god, villager
    private int poisonRecord = 0;
    private boolean isNeutralized = false;

    public boolean isNeutralized() {
        return this.isNeutralized;
    }

    public void setNeutralized(boolean neutralized) {
        this.isNeutralized = neutralized;
    }

    public int getPoisonRecord() {
        return poisonRecord;
    }

    public void setPoisonRecord(int poisonRecord) {
        this.poisonRecord = poisonRecord;
    }

    public void increasePoison() {
        this.poisonRecord++;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCard1() {
        return card1;
    }

    public void setCard1(String card1) {
        this.card1 = card1;
    }

    public String getCard2() {
        return card2;
    }

    public void setCard2(String card2) {
        this.card2 = card2;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}
