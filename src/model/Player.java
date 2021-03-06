package model;

import java.util.ArrayList;

public class Player {
    private Account account;
    private Deck deck; // playing deck
    private Collection collection; // playing collection
    private long money;
    private Hand hand;
    private int mana;
    Match match;
    private boolean isAI;
    private int heldTheFlagNumberOfTurns = 0;
    private ArrayList<CollectibleItem> collectibleItems = new ArrayList<>();
    private ArrayList<Flag> flags = new ArrayList<>();
    {
        hand = new Hand();
    }

    public boolean isAI() {
        return isAI;
    }

    public void fillHand() {
        if (deck.getNextCard() == null)
            deck.refreshNextCard();
        for (int i = 0; i < 5; i++) {
            if (hand.getCards().get(i) == null) {
                Card card = deck.getNextCard();
                hand.getCards().set(i,card);
                deck.refreshNextCard();
                if (card instanceof Spell)
                    deck.getSpells().remove(card);
                if (card instanceof Minion)
                    deck.getMinions().remove(card);
            }
        }
    }


    public boolean equals(Player player) {
        return player.getAccount().getUserName().equals(this.getAccount().getUserName());
    }

    Hero findPlayerHero() {
        return deck.getHero();
    }

    //getters
    public int getMana() {
        return mana;
    }

    public Hand getHand() {
        return hand;
    }

    public Match getMatch() {
        return match;
    }

    public long getMoney() {
        return money;
    }

    public Collection getCollection() {
        return this.collection;
    }

    public String getUserName() {
        return this.account.getUserName();
    }

    public Account getAccount() {
        return account;
    }

    public Deck getDeck() {
        return deck;
    }

    public ArrayList<CollectibleItem> getCollectibleItems() {
        return collectibleItems;
    }

    public ArrayList<Flag> getFlags() {
        return flags;
    }

    public int getHeldTheFlagNumberOfTurns() {
        return heldTheFlagNumberOfTurns;
    }

    //getters

    //setters
    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setAI(boolean AI) {
        isAI = AI;
    }

    public void increaseHeldFlag(){
        heldTheFlagNumberOfTurns++;
    }

    //setters

}