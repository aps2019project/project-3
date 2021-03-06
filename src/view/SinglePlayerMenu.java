package view;

import model.*;
import presenter.CollectionMenuProcess;
import presenter.SinglePlayerMenuProcess;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class SinglePlayerMenu {
    private boolean isInSinglePlayerMenu;
    private BattleInit battleInit;
    private SinglePlayerMenuProcess singlePlayerMenuProcess;
    private boolean hasRun = false;
    private Account account;

    public SinglePlayerMenu(BattleInit battleInit, Account account) {
        isInSinglePlayerMenu = true;
        this.battleInit = battleInit;
        singlePlayerMenuProcess = new SinglePlayerMenuProcess(this);
        singlePlayerMenuProcess.setAccount(account);
        this.setAccount(account);
    }

    public void run() throws IOException {
        Scanner scanner = new Scanner(System.in);
        help();
        while (true) {
            if (!isInSinglePlayerMenu)
                break;
            String command = scanner.nextLine();
            singlePlayerMenuProcess.commandParts = command.split("\\s+");
            int commandType = SinglePlayerMenuProcess.findPatternIndex(command);
            if (commandType == -1)
                showMessage("invalid input");
            else if (singlePlayerMenuProcess.DoCommands[commandType].doIt() == 4) {
                boolean inSelectUserLoop = false;
                showHeroes();
                showMessage("insert your desired hero number, as your opponent : ");
                command = scanner.nextLine();
                Hero hero = getDesiredHero(command);
                while (hero == null) {
                    showMessage("invalid input, you must enter a number from 1 to 10.");
                    command = scanner.nextLine();
                    if (command.toLowerCase().matches("help"))
                        showMessage("insert your desired hero number, as your opponent : ");
                    if (command.toLowerCase().matches("exit"))
                        break;
                    hero = getDesiredHero(command);
                }
                if (hero != null)
                    inSelectUserLoop = true;
                boolean deckChosen = false;
                Deck deck = new Deck("");
                if(hero!=null)
                {
                    CollectionMenuProcess.showAllDecks(account);
                    showMessage("Choose a deck to fight with, by typing its name : ");
                    command = scanner.nextLine();
                     deck = account.getCollection().getDeckHashMap().get(command);
                    while (deck == null){
                        showMessage("invalid deck. please just type the desired deck's name.");
                    if (command.toLowerCase().matches("help"))
                        showMessage("Choose a deck to fight with, by typing its name : ");
                    if (command.toLowerCase().matches("exit"))
                        break;
                    command = scanner.nextLine();
                    deck = account.getCollection().getDeckHashMap().get(command);
                    }
                    if(deck!=null)
                        deckChosen = true;
                }
                //  if ()
                //show decks
                //showList(account.getCollection().getDeckHashMap());

                inner_Loop:
                while (inSelectUserLoop && deckChosen) {
                    command = scanner.nextLine();
                    switch (customGameMenu(command)) {
                        case 1:
                            customGameHelp();
                            break;
                        case 2:
                            break inner_Loop;
                        case 3:
//                            if (singlePlayerMenuProcess.customGame(command,hero) == 4)
//                                showMessage("invalid deck");
                            break;
                        default:
                            showMessage("invalid input");
                    }
                }
            }
        }
        scanner.close();
    }

    private Hero getDesiredHero(String number) {
        switch (number) {
            case "1":
                return Hero.getHeroByName("Div e Sefid");
            case "2":
                return Hero.getHeroByName("Simorgh");
            case "3":
                return Hero.getHeroByName("Seven Headed Dragon");
            case "4":
                return Hero.getHeroByName("Rakhsh");
            case "5":
                return Hero.getHeroByName("Zahhak");
            case "6":
                return Hero.getHeroByName("Kaave");
            case "7":
                return Hero.getHeroByName("Aarash");
            case "8":
                return Hero.getHeroByName("Afsaane");
            case "9":
                return Hero.getHeroByName("Esfandiar");
            case "10":
                return Hero.getHeroByName("Rostam");
            default:
                return null;
        }

    }

    private void showHeroes() {
        showMessage("1. Div e Sefid");
        showMessage("2. Simorgh");
        showMessage("3. Seven Headed Dragon");
        showMessage("4. Rakhsh");
        showMessage("5. Zahhak");
        showMessage("6. Kaave");
        showMessage("7. Aarash");
        showMessage("8. Afsaane");
        showMessage("9. Esfandiar");
        showMessage("10. Rosatm");
    }

    private void showList(HashMap<String, Deck> deckHashMap) {
        for (Deck deck : deckHashMap.values())
            System.out.println(" - " + deck.getName());
    }

    private static int customGameMenu(String command) {
        if (command.equals("help")) return 1;
        else if (command.equals("exit")) return 2;
        else if (command.matches("start game [a-zA-Z0-9._]+ \\d[ \\d+]*")) return 3;
        else return -1;
    }

    public static int help() {
        showMessage("1. Story");
        showMessage("2. Custom game");
        showMessage("3. Exit");
        showMessage("4. Help");
        return 0;
    }

    private static void customGameHelp() {
        showMessage("Start game [deck name] [mode] [number of flags]->(only for the 3rd mode)");
    }

    public static void showMessage(String message) {
        System.out.println(message);
    }

    //getters
    public BattleInit getBattleInit() {
        return battleInit;
    }

    public SinglePlayerMenuProcess getSinglePlayerMenuProcess() {
        return singlePlayerMenuProcess;
    }

    //getters

    //setters
    public void setInSinglePlayerMenu(boolean isInSinglePlayerMenu) {
        this.isInSinglePlayerMenu = isInSinglePlayerMenu;
    }

    public void setHasRun(boolean hasRun) {
        this.hasRun = hasRun;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    private void showDecks() {
    }
    //setters
}
