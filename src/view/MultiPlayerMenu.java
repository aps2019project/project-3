package view;

import model.Account;
import presenter.MultiPlayerMenuProcess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MultiPlayerMenu {
    private boolean isInMultiPlayerMenu;
    private BattleInit battleInit;
    private MultiPlayerMenuProcess multiPlayerMenuProcess;
    private boolean hasRun = false;
    private Account account;
    private ArrayList<Account> users = Account.getAccounts();

    public MultiPlayerMenu(BattleInit battleInit, Account account) {
        isInMultiPlayerMenu = true;
        this.battleInit = battleInit;
        multiPlayerMenuProcess = new MultiPlayerMenuProcess(this);
        multiPlayerMenuProcess.setAccount(account);
        this.setAccount(account);
    }

    public void run() throws IOException {
        Scanner scanner = new Scanner(System.in);
        printUsers();
        help();
        while (true) {
            if (!isInMultiPlayerMenu)
                break;
            String command = scanner.nextLine();
            multiPlayerMenuProcess.commandParts = command.split("[ ]");
            int commandType = MultiPlayerMenuProcess.findPatternIndex(command);
            if (commandType == -1)
                System.out.println("invalid input");
            else
                switch (multiPlayerMenuProcess.DoCommands[commandType].doIt()) {
                    case 1:
                        showMessage("selected deck for second player is invalid");
                        break;
                    case 2:
                        showMessage("username is invalid");
                        break;
                    case 3:
                        printGameModes();
                        System.out.print("\n");
                        customGameHelp();
                        inner_Loop:
                        while (true) {
                            command = scanner.nextLine();
                            switch (customGameMenu(command)) {
                                case 1:
                                    customGameHelp();
                                    break;
                                case 2:
                                    break inner_Loop;
                                case 3:
                                    MultiPlayerMenuProcess.customGame(command);
                                    break;
                                case -1:
                                    showMessage("invalid input");
                            }
                        }
//                    case 3:
//                        inner_Loop:
//                        while (true) {
//                            command = scanner.nextLine();
//                            switch (customGameMenu(command)) {
//                                case 1:
//                                    customGameHelp();
//                                    break;
//                                case 2:
//                                    break inner_Loop;
//                                case 3:
//                                    MultiPlayerMenuProcess.customGame(command);
//                                    break;
//                                default:
//                                    showMessage("invalid input");
//                            }
//                        }
//                        break;
                    case 4:

                }
        }
        scanner.close();
    }

    private int customGameMenu(String command) {
        if (command.matches("[hH]elp")) return 1;
        else if (command.matches("[eE]xit")) return 2;
        else if (command.toLowerCase().matches("[sS]tart multi[pP]layer game [\\d][ \\d]*")) return 3;
        else return -1;
    }

    private void customGameHelp() {
        showMessage("Start multiPlayer game [mode] [number of flags]->(only for the 3rd mode)");
        showMessage("Exit");
        showMessage("Help");
    }

    private void printUsers() {
        showMessage("Choose a user to to start the match with :");
        for (Account account : users)
            if (!account.getUserName().equals(this.account.getUserName()))
                showMessage(" - " + account.getUserName());
    }

    public static int help() {
        showMessage("Select user [user name]");
        //showMessage("Start multiPlayer game [mode] [number of flags]->(for the 3rd mode only)");
        showMessage("Exit");
        showMessage("Help");
        return 0;
    }

    public static void showMessage(String message) {
        System.out.println(message);
    }

    public static void printGameModes(){
        showMessage("1. Heroes fight");
        showMessage("2. Capture the flag");
        showMessage("3. Capture most flags");
    }

    //getters
    public BattleInit getBattleInit() {
        return battleInit;
    }
    //getters

    //setters
    public void setInMultiPlayerMenu(boolean isInMultiPlayerMenu) {
        this.isInMultiPlayerMenu = isInMultiPlayerMenu;
    }

    public void setHasRun(boolean hasRun) {
        this.hasRun = hasRun;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
    //setters
}
