package model;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class MovableCard extends Card {
    private int health;
    protected boolean isAlive = false;
    protected Cell cardCell;
    private int damage;
    private ArrayList<Impact> impactsAppliedToThisOne = new ArrayList<>();
    protected boolean didMoveInThisTurn;
    protected boolean didAttackInThisTurn;
    protected int moveRange = 2;
    protected int minAttackRange;
    protected int maxAttackRange;
    protected boolean isMelee;
    protected boolean isRanged;
    protected boolean isHybrid;
    protected Impact onDefendImpact = null;
    protected Impact onAttackImpact = null;
    protected boolean isComboAttacker;
    public int dispelableHealthChange = 0;
    public int dispelableDamageChange = 0;
    private HashMap<String, MovableCard> previousTargets = new HashMap<>();
    private Item item;

    String getClassType(MovableCard movableCard) {
        if (movableCard.isMelee)
            return "Melee";
        if (movableCard.isHybrid)
            return "Hybrid";
        if (movableCard.isRanged)
            return "Ranged";
        return null;
    }

    //card casting

    @Override
    public void castCard(Cell cell) {
        cell.setMovableCard(this);
        this.cardCell = cell;
        if (!(this instanceof Hero))
            player.getHand().removeCardFromHand(this);
        if (!(this instanceof Hero))
            player.setMana(player.getMana() - this.manaCost);
    }

    @Override
    public boolean isCastingCoordinationValid(Cell cell) {
        return cell.getMovableCard() == null;
    }
    //card casting

    //attack & counterAttack
    public int attack(MovableCard opponent) {
        int returnValue = isAttackValid(opponent);
        if (returnValue == 0) {
            // do attack
            opponent.takeDamage(this.damage);
            didAttackInThisTurn = true;
            if (!Impact.doesHaveAntiHolyBuff(this))
                Impact.holyBuff(opponent, this.damage + this.dispelableDamageChange);
            //do attack
            opponent.counterAttack(this); // gonna need to change it
            manageCasualties();
        }
        return returnValue;
    }

    int isAttackValid(MovableCard opponent) {
        int returnValue = counterAttackAndNormalAttackSameParameters(opponent);
        if (returnValue != 0)
            return returnValue;
        if (isHybrid)
            return 0;
        for (Impact impact : impactsAppliedToThisOne)
            if (impact.isStunBuff())
                return 13;
        return 0;
    }

    private int counterAttackAndNormalAttackSameParameters(MovableCard opponent) {
        int distance = findDistanceBetweenTwoCells(this.cardCell, opponent.cardCell);
        if(isMelee)
            maxAttackRange = 2;
        if (isMelee && !this.cardCell.isTheseCellsAdjacent(opponent.cardCell))
            return 16; // todo : why ??
        else if (distance > maxAttackRange)
            return 14;
        if (this.player.equals(opponent.player))
            return 15;
        return 0;
    }


    protected void counterAttack(MovableCard opponent) {
        if (isCounterAttackValid(opponent)) {
            opponent.takeDamage(this.damage);
            if (!Impact.doesHaveAntiHolyBuff(this))
                Impact.holyBuff(opponent, this.damage + this.dispelableDamageChange);
            manageCasualties();
        }
    }

    private boolean isCounterAttackValid(MovableCard opponent) {
        if (counterAttackAndNormalAttackSameParameters(opponent) == 0) {
            if (isHybrid)
                return true;
            for (Impact impact : impactsAppliedToThisOne)
                if (impact.isDisarmBuff()) {
                    printMessage("Disarmed. Can't CounterAttack");
                    return false;
                }
        } else
            return false;
        return true;
    }

    private boolean isComboAttackValid(ArrayList<MovableCard> attackers, MovableCard target) {
        for (MovableCard movableCard : attackers) {
            if (movableCard == null)
                return false;
            if (!movableCard.isComboAttacker)
                return false;
            if (movableCard.isAttackValid(target) != 0)
                return false;
        }
        return true;
    }

    public void comboAttack(ArrayList<MovableCard> attackers, MovableCard target) {
        if (isComboAttackValid(attackers, target)) {
            attackers.get(0).attack(target);
            for (int i = 1; i < attackers.size(); i++) {
                target.health -= attackers.get(i).damage + attackers.get(i).dispelableDamageChange;
                if (attackers.get(i).onAttackImpact != null)
                    attackers.get(i).onAttackImpact.doImpact(attackers.get(i).player, target, target.cardCell, attackers.get(i).cardCell);
            }
        }
    }
    //attack & counterAttack

    public void goThroughTime() {
        for (Impact impact : impactsAppliedToThisOne) {
            impact.doImpact(this.player, this, this.cardCell, this.cardCell);
            impact.goThroughTime(this);

        }
    }

    public void resetFlags() {
        didMoveInThisTurn = false;
        didAttackInThisTurn = false;
    }

    protected void manageCasualties() {
        if (this.health + dispelableHealthChange <= 0)
            this.isAlive = false;
    }

    //move
    public void move(Cell destination) {
        if (isMoveValid(destination) == 0) {
            didMoveInThisTurn = true;
            this.cardCell.setMovableCard(null);
            if (destination.getItem() != null) {
                if (destination.getItem() instanceof CollectibleItem){
                    this.player.getCollectibleItems().add((CollectibleItem) destination.getItem());
                    destination.getItem().setItemID(createIdForItem((CollectibleItem) destination.getItem()));
                }
                else
                    if(destination.getItem() instanceof Flag)
                        this.player.getFlags().add((Flag) destination.getItem());
                this.item = destination.getItem();
                destination.setItem(null);
            }
            this.cardCell = destination;
            this.cardCell.setMovableCard(this);
            if (!cardCell.cellImpacts.isEmpty()) {
                this.impactsAppliedToThisOne.addAll(cardCell.cellImpacts);
            }
        }
    }

    public int isMoveValid(Cell cell) {
        moveRange = 2;
        if (this.cardCell == cell)
            return 9999; //unhandled
        if (didMoveInThisTurn)
            return 4;
        if (findDistanceBetweenTwoCells(this.cardCell, cell) > this.moveRange)
            return 5;
        if (isOpponentInTheWayOfDesiredDestination(this.cardCell, cell))
            return 6;
        for (Impact impact : impactsAppliedToThisOne)
            if (impact.isStunBuff())
                return 8;
        return 0;
    }

    private boolean isOpponentInTheWayOfDesiredDestination(Cell start, Cell destination) {
        if (start.getCellCoordination().getY() != destination.getCellCoordination().getY())
            if (start.getCellCoordination().getX() != destination.getCellCoordination().getX())
                return false;
        int x = destination.getCellCoordination().getX() - start.getCellCoordination().getX();
        int y = destination.getCellCoordination().getY() - start.getCellCoordination().getY();
        if (x < 0)
            x = -1;
        if (x > 0)
            x = 1;
        if (y < 0)
            y = -1;
        if (y > 0)
            y = 1;
        x += start.getCellCoordination().getX();
        y += start.getCellCoordination().getY();
        MovableCard movableCard = this.player.match.table.getCellByCoordination(x, y).getMovableCard();
        if (movableCard != null)
            return !movableCard.player.equals(this.player);
        return false;
    }
    //move

    private int findDistanceBetweenTwoCells(Cell cell1, Cell cell2) {
        return Math.abs(cell1.getCellCoordination().getX() - cell2.getCellCoordination().getX()) + Math.abs(cell1.getCellCoordination().getY() - cell2.getCellCoordination().getY());
    }

    private void printMessage(String message) {
        System.out.println(message);
    }

    protected void takeDamage(int damage) {
        this.health -= damage;
    }

    //previous targets manager

    void addToTargetedOnes(MovableCard movableCard) {
        previousTargets.put(movableCard.name, movableCard);
    }

    boolean haveAttackedOnThisBefore(MovableCard movableCard) {
        return previousTargets.containsKey(movableCard.name);
    }

    private String createIdForItem(CollectibleItem collectibleItem){
        int index = 1;
        for( int i = 0; i < this.player.getCollectibleItems().size(); i++){
            if(this.player.getCollectibleItems().get(i).getName().equals(collectibleItem.getName()))
                index++;
        }
        return this.player.getAccount().getUserName()+"_"+collectibleItem.getName()+"_"+index;
    }
    //previous targets manager

    //getters

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getMoveRange() {
        return moveRange;
    }

    public int getMinAttackRange() {
        return minAttackRange;
    }

    public int getMaxAttackRange() {
        return maxAttackRange;
    }

    public boolean isMelee() {
        return isMelee;
    }

    public boolean isHybrid() {
        return isHybrid;
    }

    public boolean isRanged() {
        return isRanged;
    }

    public ArrayList<Impact> getImpactsAppliedToThisOne() {
        return impactsAppliedToThisOne;
    }

    public int getDamage() {
        return damage;
    }

    public void setOnDefendImpact(Impact onDefendImpact) {
        this.onDefendImpact = onDefendImpact;
    }

    public void setOnAttackImpact(Impact onAttackImpact) {
        this.onAttackImpact = onAttackImpact;
    }

    public Cell getCardCell() {
        return cardCell;
    }

    public boolean isAlive() {
        return isAlive;
    }

    //getters

    //setters
    public void setCardCell(Cell cardCell) {
        this.cardCell = cardCell;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public void setMelee(boolean melee) {
        isMelee = melee;
    }

    public void setRanged(boolean ranged) {
        isRanged = ranged;
    }

    public void setHybrid(boolean hybrid) {
        isHybrid = hybrid;
    }

    public void setMaxAttackRange(int maxAttackRange) {
        this.maxAttackRange = maxAttackRange;
    }

    public void setComboAttacker(boolean comboAttacker) {
        isComboAttacker = comboAttacker;
    }

    //setters


}
