package view;

import presenter.LoginMenuProcess;

import java.io.IOException;
import java.util.Scanner;

public class LoginMenu {
    private static boolean isInLoginMenu = true;

    static void run() throws IOException {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String command = scanner.nextLine();
            String[] commandParts = command.split("[ ]");
            LoginMenuProcess.commandParts = commandParts;
            if (!isInLoginMenu)
                break;
            int commandType = presenter.LoginMenuProcess.findPatternIndex(command, commandParts);
            if (commandType == -1)
                System.out.println("invalid input");
            else
                handleErrors(presenter.LoginMenuProcess.DoCommands[commandType].doIt());
        }
    }

    public static String scan() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private static void handleErrors(int messageID) {
        switch (messageID) {
                case 1:
                    LoginMenu.showMessage("an account with this username already exists");
                    break;
                case 2:
                    LoginMenu.showMessage("incorrect password");
                    break;
                case 3:
                    LoginMenu.showMessage("no account with this username found");
                    break;
            }
        }

    //setters
    public static void setIsInLoginMenu(boolean isInLoginMenu) {
        LoginMenu.isInLoginMenu = isInLoginMenu;
    }
    //setters

    public static int help() {
        showMessage("create account [user name]");
        showMessage("login [user name]");
        showMessage("show leaderBoard");
        showMessage("save");
        showMessage("logout");
        return 0;
    }

    public static void showMessage(String message) {
        System.out.println(message);
    }
}