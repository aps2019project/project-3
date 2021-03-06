package model;

import model.Message.ShopCommand.Trade.TradeRequest;

import java.util.ArrayList;
import java.util.HashMap;

public class Collection {
    private ArrayList<UsableItem> items = new ArrayList<>();
    private  ArrayList<Deck> decks = new ArrayList<>();
    private  ArrayList<Hero> heroes = new ArrayList<>();
    private  ArrayList<Minion> minions = new ArrayList<>();
    private  ArrayList<Spell> spells = new ArrayList<>();
    private HashMap<String, UsableItem> itemsHashMap;
    private HashMap<String, Minion> minionHashMap;
    private HashMap<String, Hero> heroHashMap;
    private HashMap<String, Spell> spellHashMap;
    private HashMap<String, Deck> deckHashMap;
    private Deck selectedDeck = null;


    {
        itemsHashMap = new HashMap<>();
        minionHashMap = new HashMap<>();
        heroHashMap = new HashMap<>();
        spellHashMap = new HashMap<>();
        deckHashMap = new HashMap<>();
    }

    public static ArrayList<ArrayList<String >> getCollectionCards(Account account) {
        ArrayList<ArrayList<String >> collectionCards = new ArrayList<>();
        Collection collection = account.getCollection();
        for (int i = 0; i <4 ; i++)
            collectionCards.add(new ArrayList<>());
        for (Hero shopHero : collection.getHeroes())
            collectionCards.get(0).add(shopHero.getName());
        for (Minion shopMinion : collection.getMinions())
            collectionCards.get(1).add(shopMinion.name);
        for (Spell shopSpell : collection.getSpells())
            collectionCards.get(2).add(shopSpell.name);
        for (UsableItem shopItem : collection.getItems())
            collectionCards.get(3).add(shopItem.name);
        return collectionCards;
    }

    public void setSelectedDeck(String name) {
        selectedDeck = deckHashMap.get(name);
    }

    public void setSelectedDeck(Deck deck){
        selectedDeck = deck;
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
        if (selectedDeck != null && selectedDeck.getName().equals(deckName)) selectedDeck = null;
        return 0;
    }

    public boolean validateDeck(Deck deck) {
        System.out.println("deck size:"+deck.getMinions().size() );
        System.out.println("deck hero:"+deck.getHero().name);
        if (deck == null || (deck.getMinions().size() + deck.getSpells().size()) != 20)
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
            output = output + "\t\t" + (idx + 1) + " : " + heroes.get(i).toString(showCost) + "\n";
            idx++;
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
            if (i != (minions.size() - 1))
                output = output + "\t\t" + (idx + 1) + " : " + (minions.get(i)).toString(showCost) + "\n";
            else
                output = output + "\t\t" + (idx + 1) + " : " + (minions.get(i)).toString(showCost);
            idx++;
        }
        return output;
    }

    //search
//    String search(String name) {
//
//    }

    public UsableItem findItemByCollectionID(String itemID) {
        for (int i = 0; i < items.size(); i++) {
            if(items.get(i).getCollectionID().endsWith(itemID))
                return items.get(i);
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

    public Card findCardByCollectionID(String cardID) {
        if (heroHashMap.get(cardID) != null)
            return heroHashMap.get(cardID);
        if (minionHashMap.get(cardID) != null)
            return minionHashMap.get(cardID);
        if (spellHashMap.get(cardID) != null)
            return spellHashMap.get(cardID);
        return null;
    }

    public Card findCardByName(String name) {
        for (Hero hero : heroes)
            if (hero.getName().equals(name))
                return hero;
        for (Spell spell : spells)
            if (spell.getName().equals(name))
                return spell;
        for (Minion minion : minions)
            if (minion.getName().equals(name))
                return minion;
        return null;
    }



    //search

    public void setDecks(ArrayList<Deck> decks) {
        this.decks = decks;
    }

    public void setDeckHashMap(HashMap<String, Deck> deckHashMap) {
        this.deckHashMap = deckHashMap;
    }

    //getters
    public HashMap<String, UsableItem> getItemsHashMap() {
        return itemsHashMap;
    }

    public ArrayList<UsableItem> getItems() {
        return items;
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

    public ArrayList<Hero> getHeroes() {
        return heroes;
    }

    public ArrayList<Minion> getMinions() {
        return minions;
    }

    public ArrayList<Spell> getSpells() {
        return spells;
    }

    public HashMap<String, Minion> getMinionHashMap() {
        return minionHashMap;
    }

    public HashMap<String, Hero> getHeroHashMap() {
        return heroHashMap;
    }

    public HashMap<String, Spell> getSpellHashMap() {
        return spellHashMap;
    }

    public HashMap<String, Integer> getCardNumbers() {
        HashMap<String , Integer> objects = new HashMap<>();
        for (int i = 0; i< heroes.size(); i++) {
            objects.put(heroes.get(i).name,heroes.get(i).getCollectionNumber());
        }
        for (int i = 0; i< minions.size(); i++) {
            objects.put(minions.get(i).name,minions.get(i).getCollectionNumber());
        }
        for (int i = 0 ; i < spells.size(); i++) {
            objects.put(spells.get(i).name,spells.get(i).getCollectionNumber());
        }
        for (int i = 0; i<items.size(); i++) {
            objects.put(items.get(i).name,items.get(i).getCollectionNumber());
        }
        return objects;
    }


    //getters
}
