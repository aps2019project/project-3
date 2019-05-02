package model;

import java.util.ArrayList;
import java.util.HashMap;

public class Collection {
    private ArrayList<UsableItem> items = new ArrayList<>();
    private ArrayList<Minion> minions = new ArrayList<>();
    private ArrayList<Spell> spells = new ArrayList<>();
    private ArrayList<Hero> heroes = new ArrayList<>();
    private ArrayList<Deck> decks = new ArrayList<>();
    private HashMap<String, UsableItem> itemsHashMap;
    private HashMap<String, Card> cardsHashMap;
    private HashMap<String, Deck> deckHashMap;
    private Deck selectedDeck = null;

    {
        itemsHashMap = new HashMap<>();
        cardsHashMap = new HashMap<>();
        deckHashMap = new HashMap<>();
    }

    public void setSelectedDeck(String name) {
        selectedDeck = deckHashMap.get(name);
    }

    public void addDeck(Deck deck) {
        decks.add(deck);
        deckHashMap.put(deck.getName(), deck);
    }

    public int deleteDeck(String deckName) {
        if (!getDeckHashMap().containsKey(deckName))
            return 9;
        getDecks().remove(getDeckHashMap().get(deckName));
        getDeckHashMap().remove(deckName);
        return 0;
    }

    public boolean validateDeck(Deck deck) {
        if (deck == null || deck.getCards().size() != 20)
            return false;
        return deck.getHero() != null;
    }

    public void add(String id, Deck deck) {
        //find card and item and hero by id
        //it must be written in presenter and an instance should be held in presenter and the changes
        // should be applied in the copy instance
    }

    public String show(boolean showCost) {
        String output = "Heroes :\n";
        int idx = 0;
        for (int i = 0; i < heroes.size(); i++) {
            if (heroes.get(i) instanceof Hero) {
                output = output + "\t\t" + (idx + 1) + " : " + heroes.get(i).toString(showCost) + "\n";
                idx++;
            }
        }
        output = output + "Items :\n";
        for (int i = 0; i < items.size(); i++)
            output = output + "\t\t" + (i + 1) + " : " + items.get(i).toString(showCost) + "\n";
        idx = 0;
        output = output + "Cards :\n";
        for (int i = 0; i < spells.size(); i++) {
                output = output + "\t\t" + (idx + 1) + " : " + (spells.get(i)).toString(showCost) + "\n";
                idx++;
        }
        for (int i = 0; i < minions.size(); i++) {
            if(i!=minions.size()-1)
            output = output + "\t\t" + (idx + 1) + " : " + (minions.get(i)).toString(showCost) + "\n";
            else
                output = output + "\t\t" + (idx + 1) + " : " + (minions.get(i)).toString(showCost);
            idx++;
        }

        return output;
    }

    //search
    String search(String name) {
        Item item = itemsHashMap.get(name);
        if (item != null)
            return item.getItemID();
        Card card = cardsHashMap.get(name);
        if (card != null)
            return card.getCardID();
        return "-1";
    }

    public UsableItem findItemByID(String itemID) {
        for (UsableItem item : items) {
            if (item.getItemID() != null && item.getItemID().equals(itemID))
                return item;
        }
        return null;
    }

    public UsableItem findItemByName(String itemName) {
        for (UsableItem item : items) {
            if (item.getName().equals(itemName))
                return item;
        }
        return null;
    }

    public Card findCardByID(String cardID) {
        for (Minion minion : minions) {
            if(minion.getCardID()!= null && minion.getCardID().equals(cardID))
                return minion;
        }
        for (Hero hero : heroes) {
            if(hero.getCardID()!=null && hero.getCardID().equals(cardID))
                return hero;
        }
        for (Spell spell : spells) {
            if(spell.getCardID()!=null && spell.getCardID().equals(cardID))
                return spell;
        }
        return null;
    }

    public Card findCardByName(String cardID) {
for (Minion minion : minions) {
            if(minion.getName().equals(cardID))
                return minion;
        }
        for (Hero hero : heroes) {
            if(hero.getName().equals(cardID))
                return hero;
        }
        for (Spell spell : spells) {
            if(spell.getName().equals(cardID))
                return spell;
        }
        return null;
    }
    //search

    //getters
    public HashMap<String, UsableItem> getItemsHashMap() {
        return itemsHashMap;
    }

    public HashMap<String, Card> getCardsHashMap() {
        return cardsHashMap;
    }

    ArrayList<UsableItem> getItems() {
        return items;
    }

    public ArrayList<Minion> getMinions() {
        return minions;
    }

    public ArrayList<Spell> getSpells() {
        return spells;
    }

    public ArrayList<Hero> getHeroes() {
        return heroes;
    }

    public ArrayList<Deck> getDecks() {
        return decks;
    }

    public Deck getSelectedDeck() {
        return selectedDeck;
    }

    public HashMap<String, Deck> getDeckHashMap() {
        return deckHashMap;
    }
    //getters
}
