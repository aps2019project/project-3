package model;

import java.util.ArrayList;

enum GameMode {HeroesFight, CaptureTheFlag, CaptureMostFlags}

public class Match {
    private boolean singleMode;
    //    private model.GameMode gameMode;
    private int gameMode;
    Table table;
    private Player player1 = new Player();
    private Player player2 = new Player();
    public ArrayList<Card> player1_graveyard;
    public ArrayList<Card> player2_graveyard;
    private int turn = 0;
    private int numberOfFlags = -1;
    private Integer AILevel = 0;

    {
        player1_graveyard = new ArrayList<>();
        player2_graveyard = new ArrayList<>();
    }

    public Match(boolean singleMode, int gameMode) {
        this.singleMode = singleMode;
        this.gameMode = gameMode;
        this.table = new Table();
    }

    public void moveToGraveYard(Card card, Player player) {
        if (player.getUserName().equals(player1.getUserName())) {
            player1_graveyard.add(card);
            //
        } else {
            player2_graveyard.add(card);
            //
        }
    }

    public void play(Player player) {
        //
    }


    public void setup(Account account, String deckName, int numberOfFlags, Deck deck) {
        player1.setAccount(account);
        player1.match = this;
        player1.setDeck(player1.getAccount().getCollection().getDeckHashMap().get(deckName).copy());
        player2.setAccount(new Account("Computer","Computer"));
        player2.getAccount().setCollection(new Collection());
        player2.getAccount().getCollection().setSelectedDeck(deck);
        player2.setDeck(deck.copy());
        player2.setAI(true);
        player2.match = this;
        this.numberOfFlags = numberOfFlags;
    }

    public void setup(Account account1, Account account2, String deckName, int numberOfFlags) {
        player1.setAccount(account1);
        player1.match = this;
        player2.setAccount(account2);
        player2.match = this;
        player1.setDeck(account1.getCollection().getDeckHashMap().get(deckName).copy());
        player2.setDeck(account2.getCollection().getSelectedDeck().copy());
        this.numberOfFlags = numberOfFlags;
    }

    public void setFlags(int numberOfFlags){
        if(numberOfFlags == -1 || numberOfFlags == 0)
            return;
        //todo : set flags for third mode
    }
    //turn based manager
    public Player currentTurnPlayer() {
        if (turn == 0)
            return player1;
        return player2;
    }

    public Player notCurrentTurnPlayer() {
        if (turn == 0)
            return player2;
        return player1;
    }

    public void switchTurn() {
        turn ^= 1;
    }
    //turn based manager

    //getters
    Player getOtherPlayer(Player player) {
        if (player.equals(player1))
            return player2;
        return player1;
    }

    public Table getTable() {
        return table;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    Integer getAILevel() {
        return AILevel;
    }

    public int getGameMode() {
        return gameMode;
    }
    //getters

}
