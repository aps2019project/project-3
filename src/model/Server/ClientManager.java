package model.Server;

import com.google.gson.Gson;
import model.*;
import model.Message.LoginBasedCommand;
import model.Message.Message;
import model.Message.ScoreBoardCommand.ScoreBoardCommand;
import model.Message.ShopCommand.UpdateShop.UpdateWholeShop;
import model.Message.Utils;
import model.Reader;
import model.client.Client;
import presenter.LoginMenuProcess;
import sun.security.krb5.internal.TGSRep;

import javax.print.DocFlavor;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ClientManager extends Thread {
    private String authCode = "";

    public ClientManager(Server server, Socket socketOnServerSide) {
        this.socketOnServerSide = socketOnServerSide;
        this.server = server;
    }

    private static HashMap<String, ClientManager> clientManagers = new HashMap<>();
    private Server server;
    private Socket socketOnServerSide;

    //    //command manager
    @Override
    public void run() {
        try {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socketOnServerSide.getOutputStream());
            objectOutputStream.flush();
            ObjectInputStream objectInputStream = new ObjectInputStream(socketOnServerSide.getInputStream());
            while (!interrupted()) {
                Message message = (Message) objectInputStream.readObject();
                if (message instanceof LoginBasedCommand) {
                    System.out.println("-------------");
                    System.out.println(message.getUserName());
                    System.out.println("--------------");
                    handleLoginBasedCommands(objectOutputStream, (LoginBasedCommand) message);
                }
                if (message instanceof Utils) {
                    System.out.println("---- util ----");
                    handleUtilsBasedCommand(objectOutputStream, (Utils) message);
                }
                if (message instanceof ScoreBoardCommand) {
                    System.out.println("----scorebaord-----");
                    handleScoreBoardCommands(objectOutputStream, (ScoreBoardCommand) message);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void handleScoreBoardCommands(ObjectOutputStream objectOutputStream, ScoreBoardCommand scoreBoardCommand) {
        try {

            objectOutputStream.writeObject(new ScoreBoardCommand("1",server.getAccountNames(),server.getUsersOnlineStatus(),server.getNumberOfWins()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //login based

    private void handleUtilsBasedCommand(ObjectOutputStream objectOutputStream, Utils utils) {
        if (utils.isLogout()) {
            server.getOnlineAccounts().remove(utils.getAuthCode());
            server.getClients().remove(utils.getAuthCode());
            server.getAuthcodes().remove(utils.getAuthCode());
            server.showOnlineClients();
        }
    }

    private void handleLoginBasedCommands(ObjectOutputStream objectOutputStream, LoginBasedCommand message) throws IOException {
        LoginBasedCommand loginBasedCommand = message;
        String userName = loginBasedCommand.getUserName();
        String password = loginBasedCommand.getPassword();
        if (loginBasedCommand.isCreateAccount())
            createAccountServerSide(objectOutputStream, userName, password);
        else {
            loginOnServerSide(objectOutputStream, userName, password);
        }
    }


    private void loginOnServerSide(ObjectOutputStream objectOutputStream, String userName, String password) throws IOException {
        int loginErrorNumber = server.getLoginErrorNumber(userName, password);
        if (server.isLoginValid(userName, password)) {
            authCode = server.generateAuthCode(userName,this);
            objectOutputStream.writeObject(new LoginBasedCommand(userName, password, true, authCode, true, loginErrorNumber, Account.getAccountByUserName(userName)));
            ArrayList<ArrayList<String >> cards = Shop.getCards();
            ArrayList<ArrayList<String >> collectionCards = Collection.getCollectionCards(Server.getOnlineAccounts().get(authCode));
            HashMap<String, int[]> movableCardsPowers = Shop.getMovableCardsPowers();
            HashMap<String, Integer> costs = Shop.getCosts();
            HashMap<String, Integer> numbers = Shop.getNumbers();
            HashSet<String > costumCards = Shop.getCostumeCards();
            objectOutputStream.writeObject(new UpdateWholeShop(costumCards,cards, collectionCards, movableCardsPowers, costs, numbers));
        } else {
            objectOutputStream.writeObject(new LoginBasedCommand("", "", false, "", true, loginErrorNumber, Account.getAccountByUserName(userName)));
        }
    }

    private void createAccountServerSide(ObjectOutputStream objectOutputStream, String userName, String password) throws IOException {
        if (server.isAccountAvailabale(userName))
            objectOutputStream.writeObject(new LoginBasedCommand("", "", false, "", false, 0, null));
        else {
            LoginMenuProcess.createAccount(userName, password);
            objectOutputStream.writeObject(new LoginBasedCommand(userName, password, true, "", false, 0, Account.getAccountByUserName(userName)));
        }
    }

    //login based

}

