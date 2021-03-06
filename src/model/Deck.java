package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Deck {
    private String name;
    private ArrayList<UsableItem> items;
    private Hero hero = null;
    private ArrayList<Minion> minions;
    private ArrayList<Spell> spells;
    private ArrayList<Card> cards;
    private HashMap<String, UsableItem> itemsHashMap;
    public static final int MAX_CARD_NUMBER = 20;
    public static final int MAX_ITEM_NUMBER = 3;
    private Card nextCard = null;

    {
        cards = new ArrayList<>();
        itemsHashMap = new HashMap<>();
        minions = new ArrayList<>();
        spells = new ArrayList<>();
        items = new ArrayList<>();
    }

    public Deck copy() {
        Deck deck = new Deck(this.name);
        for (UsableItem item : this.items) {
            deck.items.add(item.copy());
            deck.itemsHashMap.put(item.name, item.copy());
        }
        for (Minion minion : minions) {
            deck.minions.add(minion.copy());
        }
        for (Spell spell : spells){
            deck.spells.add(spell.copy());
        }
        deck.hero = hero == null ? null : hero.copy();
        return deck;
    }

    public String show() {
        String output = "";
        if (hero != null)
            output += "Heroes :\n" + "\t\t1 : " + hero.toString(false) + "\n";
        else
            output += "Heroes :\n";
        output += "Items :\n";
        for (int i = 0; i < items.size(); i++)
            output = output + "\t\t" + (i + 1) + " : " + items.get(i).toString(false) + "\n";
        output = output + "Cards :\n";
        for (int i = 0; i < spells.size(); i++)
            output = output + "\t\t" + (i + 1) + " : " + spells.get(i).toString(false) + "\n";
        for (int i = 0; i < minions.size(); i++)
            output = output + "\t\t" + (i + 1 + spells.size()) + " : " + minions.get(i).toString(false) + "\n";
        return output;
    }

    public Deck(String name) {
        this.name = name;
    }

    public void addItemToDeck(UsableItem item) { // only used for computer Decks
//        itemsHashMap.put(item.getItemID(), item);
        items.add(item);
    }

    public void addSpellToDeck(Spell spell) { // only used for computer Decks
        spells.add(spell);
        cards.add(spell);
    }

    public void addMinionToDeck(Minion minion) { // only used for computer Decks
        minions.add(minion);
        cards.add(minion);
    }

    void putTheCardBackInTheQueue(Card card) {
        if (card instanceof Spell)
            spells.add((Spell) card);
        if (card instanceof Minion)
            minions.add((Minion) card);
        if (card instanceof Hero)
            setHero((Hero) card);
        //  cardsHashMap.put(card.name,card);
    }

    public Card findCardByCollectionID(String id) {
        if (hero != null && hero.getCollectionID().equals(id))
            return hero;
        for (Spell spell : spells)
            if (spell.getCollectionID().equals(id))
                return spell;
        for (Minion minion : minions)
            if (minion.getCollectionID().equals(id))
                return minion;
        return null;
    }

    public Card getNextCard() {
        return nextCard;
    }

    public void refreshNextCard(){
        ArrayList<Card> possibleCards = new ArrayList<>();
        possibleCards.addAll(spells);
        possibleCards.addAll(minions);
        if(possibleCards.size() == 0){
            nextCard = null;
            return;
        }
        Random random = new Random();
        int i = Math.abs(random.nextInt() % possibleCards.size());
        nextCard = possibleCards.get(i);

    }

    public Card findCardByID(String id) {
        if (hero != null && hero.getCollectionID().equals(id))
            return hero;
        for (int i = 0; i < minions.size(); i++)
            if (minions.get(i).getCollectionID().equals(id))
                return minions.get(i);
        for (int i = 0; i < spells.size(); i++)
            if (spells.get(i).getCollectionID().equals(id))
                return spells.get(i);
        return null;
    }

    //getters

    public String getName() {
        return name;
    }

    public Hero getHero() {
        return hero;
    }

    public ArrayList<UsableItem> getItems() {
        return items;
    }

    public ArrayList<Minion> getMinions() {
        return minions;
    }

    public ArrayList<Spell> getSpells() {
        return spells;
    }

    public HashMap<String, UsableItem> getItemsHashMap() {
        return itemsHashMap;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    //getters

    //setters
    public void setHero(Hero hero) {
        this.hero = hero;
    }
    //setters
}
