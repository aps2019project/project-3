package model;

import java.util.ArrayList;

enum GameMode {HeroesFight, CaptureTheFlag, CaptureMostFlags}

class Match {
    private boolean singleMode;
    private model.GameMode gameMode;
    Table table;
    private Player player1;
    private Player player2;
    private ArrayList<Card> graveYardPlayer1;
    private ArrayList<Card> graveYardPlayer2;
    private int turn = 0;
    private Integer AILevel = 0;

    {
        graveYardPlayer1 = new ArrayList<>();
        graveYardPlayer2 = new ArrayList<>();
    }

    public Player currentTurnPlayer() {
        if (turn == 0)
            return player1;
        return player2;
    }

    public Player notCurrentTurnPlayer(){
        if(turn == 0)
            return player2;
        return player1;
    }

    public Table getTable() {
        return table;
    }
    public Player getPlayer1(){
        return player1;
    }
    public Player getPlayer2(){
        return player2;
    }

    public Integer getAILevel() {
        return AILevel;
    }

    public void switchTurn() {
        turn ^= 1;
    }

    public void moveToGraveYard(Card card, Player player) {
        if (player.getUserName().equals(player1.getUserName())) {
            graveYardPlayer1.add(card);
            //
        } else {
            graveYardPlayer2.add(card);
            //
        }
    }

    public void play(Player player) {
        //
    }

    public Player giveOtherPlayer(Player player){
        if(player.equals(player1))
            return player2;
        return player1;
    }
}
