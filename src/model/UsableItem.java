package model;

import java.util.ArrayList;

public class UsableItem extends InfluentialItem {
    private static ArrayList<UsableItem> usableItems = new ArrayList<>();
    private int cost;
    private Deck deck;
    private Match match;
    private int number = 10;
    int collectionNumber = 1;

    @Override
    public UsableItem copy(){
        UsableItem item = new UsableItem();
        item.name = name;
        item.description = description;
        item.itemID = itemID;
        item.dyingWishImpact = dyingWishImpact == null ? null: dyingWishImpact.copy();
        item.summonImpact = summonImpact == null ? null : summonImpact.copy();
        item.primaryImpact = primaryImpact == null ? null : primaryImpact.copy();
        item.secondaryImpact = secondaryImpact == null ? null : secondaryImpact.copy();
        item.cost = cost;
        item.deck = deck == null ?null : deck.copy();
        item.match = match;
        item.collectionID = this.collectionID;
        return item;
    }

    public String toString(boolean showCost) {
        String output = "Name : " + name + " - Desc :" + description;
        if (showCost) output = output + " - Sell Cost : " + getCost();
        //output = output + "\n";
        return output;
    }

    // setter & getter
    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    public Deck getDeck() {
        return deck;
    }

    public Match getMatch() {
        return match;
    }

    public static UsableItem getUsableItemByName(String name) {
        for (UsableItem usableItem : usableItems)
            if (usableItem.getName().equals(name))
                return usableItem.copy();
        return null;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCollectionNumber() {
        return collectionNumber;
    }

    public static void addToUsableItems(UsableItem usableItem) {
        usableItems.add(usableItem);
    }
    // setter & getter
}
