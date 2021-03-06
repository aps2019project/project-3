package model;

import java.util.ArrayList;

public class Spell extends Card {
    private static ArrayList<Spell> spells = new ArrayList<>();
    private Impact primaryImpact;
    private Impact secondaryImpact;

    public Spell(Impact primaryImpact, Impact secondaryImpact) {
        this.primaryImpact = primaryImpact;
        this.secondaryImpact = secondaryImpact;
    }

    @Override
    public String toString(boolean showCost) {
        String output = "Type : Spell - Name : " + name + " - MP : " + manaCost + " - Desc : " + description;
        if (showCost) output = output + " - Sell Cost : " + getCost();
        //output += "\n";
        return output;
    }

    public boolean isCastingValid(Player castingPlayer, Cell cell) {
        ArrayList<Cell> cells = primaryImpact.getImpactAreaClass().getValidCells(castingPlayer);
        for (Cell wantedCells: cells
             ) {
            if(cell.getCellCoordination().getY() == wantedCells.getCellCoordination().getY() && cell.getCellCoordination().getX() == wantedCells.getCellCoordination().getX())
                return true;
        }
        return false;
    }

    public void castCard(Cell cell) {
        if (isCastingValid(this.player, cell))
            primaryImpact.doImpact(this.player,cell.getMovableCard(),cell,cell);
        if (secondaryImpact != null )
            secondaryImpact.doImpact(this.player,cell.getMovableCard(),cell,cell);
        player.getHand().removeCardFromHand(this);
        player.setMana(player.getMana() - this.manaCost);
    }

    public Spell copy(){
        Impact primaryImpactCopy = primaryImpact == null?null:primaryImpact.copy();
        Impact secondaryImpactCopy = secondaryImpact == null? null:secondaryImpact.copy();
        Spell spell = new Spell(primaryImpactCopy,secondaryImpactCopy);
        spell.cell = cell;
        setCardFieldsForCopy(spell);
        spell.collectionID = this.collectionID;
        spell.manaCost = this.manaCost;
        spell.name = this.name;
        spell.cell = this.cell;
        spell.cost = this.cost;
        spell.match = this.match;
        spell.player = this.player;
        spell.cardID = this.cardID;
        spell.description = this.description;
        spell.collectionNumber = this.collectionNumber;
        return spell;
    }

    public ArrayList<Cell> getValidCoordination(){

        return primaryImpact.getImpactAreaClass().getValidCells(player);
    }

    //getters
    public Impact getPrimaryImpact() {
        return primaryImpact;
    }

    public Impact getSecondaryImpact() {
        return secondaryImpact;
    }

    public static Spell getSpellByName(String name) {
        for (Spell spell : spells)
            if (spell.getName().equals(name))
                return spell.copy();
        return null;
    }


    //getters

    //setters
    public void setName(String name) {
        this.name = name;
    }

    public void setManaCost(int manaCost) {
        this.manaCost = manaCost;
    }

    public void setPrimaryImpact(Impact primaryImpact) {
        this.primaryImpact = primaryImpact;
    }

    public void setSecondaryImpact(Impact secondaryImpact) {
        this.secondaryImpact = secondaryImpact;
    }

    public static void addToSpells(Spell spell) {
        spells.add(spell);
    }


    //setters
}
