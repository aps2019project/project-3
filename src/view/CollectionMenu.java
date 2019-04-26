package view;

import model.Account;
import model.Collection;
import model.Player;
import presenter.CollectionMenuProcess;

import java.io.IOException;
import java.util.Scanner;

public class CollectionMenu {
    private  boolean isInCollectionMenu = true;
    private  Account currentAccount;
    private CollectionMenuProcess collectionMenuProcess;
    public CollectionMenu(Account account){
        this.currentAccount= account;
        collectionMenuProcess = new CollectionMenuProcess();
        collectionMenuProcess.setAccount(currentAccount);
    }
    public void run() throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (!isInCollectionMenu)
                break;
            String command = scanner.nextLine();
            CollectionMenuProcess.commandParts = command.split("[ ]");
            int commandType = presenter.CollectionMenuProcess.findPatternIndex(command);
            if (commandType == -1)
                System.out.println("invalid input");
            else
                handleErrors(collectionMenuProcess.DoCommands[commandType].doIt());
        }
        scanner.close(); // ?
    }

    public static String scan() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private static void handleErrors(int messageID) {
        switch (messageID) {
            case 1:
                CollectionMenu.showMessage("a deck with this name already exists");
                break;
            case 2:
                CollectionMenu.showMessage("card doesn't exist");
                break;
            case 3:
                CollectionMenu.showMessage("no card with this id was found in the collection");
                break;
            case 4:
                CollectionMenu.showMessage("card is already in the deck");
                break;
            case 5:
                CollectionMenu.showMessage("number of the cards in the deck must not exceed 20");
                break;
            case 6:
                CollectionMenu.showMessage("there's a hero in the deck already");
                break;
            case 7:
                CollectionMenu.showMessage("card doesn't exist in the deck");
                break;
            case 8:
                CollectionMenu.showMessage("Deck is invalid");
                break;
            case 9:
                CollectionMenu.showMessage("deck not found");
                break;
            case 10:
                CollectionMenu.showMessage("Card/Item not found");
                break;
        }
    }

    //setters
    public void setIsInCollectionMenu(boolean isInLoginMenu) {
        this.isInCollectionMenu = isInLoginMenu;
    }
    //setters

    public static int help() {
        showMessage("exit");
        showMessage("show");
        showMessage("search [card name | item name]");
        showMessage("save");
        showMessage("create deck [deck name]");
        showMessage("delete deck [deck name]");
        showMessage("add [card id | item id | hero id] to deck [deck name]");
        showMessage("remove [card id | item id | hero id] from deck [deck name]");
        showMessage("validate deck [deck name]");
        showMessage("select deck [deck name]");
        showMessage("show all decks");
        showMessage("show deck [deck name]");
        return 0;
    }

    public static void showMessage(String message) {
        System.out.println(message);
    }
}