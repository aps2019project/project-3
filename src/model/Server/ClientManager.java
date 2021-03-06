package model.Server;

import javafx.util.Pair;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.*;
import model.Message.BattleCommand.BattleCommand;
import model.Message.BattleCommand.BattleRequest;
import model.Message.GlobalChatMessage;
import model.Message.LoginBasedCommand;
import model.Message.Message;
import model.Message.SaveCommand.SaveCommand;
import model.Message.ScoreBoardCommand.ScoreBoardCommand;
import model.Message.ShopCommand.Trade.TradeRequest;
import model.Message.ShopCommand.Trade.TradeResponse;
import model.Message.ShopCommand.UpdateShop.UpdateCards;
import model.Message.ShopCommand.UpdateShop.UpdateWholeShop;
import model.Message.Utils;
import presenter.LoginMenuProcess;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ClientManager extends Thread {
    private String authCode = "";
    private String userName = "";
    private Pair<UpdateCards, Integer> lastUpdate = new Pair<>(new UpdateCards(0, new int[2], "", "", 0, false, -1), -1);
    private ArrayList<UpdateCards> updateCards = new ArrayList<>();
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

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
            objectOutputStream = new ObjectOutputStream(socketOnServerSide.getOutputStream());
            objectOutputStream.flush();
            objectInputStream = new ObjectInputStream(socketOnServerSide.getInputStream());
            new Thread(() -> {
                long lastUpdate = System.currentTimeMillis();
                while (!interrupted()) {
                    Server.getLatestUpdates(-1);
                    if (System.currentTimeMillis() - lastUpdate > 1000) {
                        try {
                            updateShop(objectOutputStream);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        lastUpdate = System.currentTimeMillis();
                    }
                }
            }).start();
            while (!interrupted()) {

                Message message = (Message) objectInputStream.readObject();
                if (message instanceof LoginBasedCommand) {
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
                if (message instanceof TradeRequest) {
                    handleTradeProcess(objectOutputStream, message);
                    server.saveCards();
                }
                if (message instanceof GlobalChatMessage) {
                    handleGlobalChatMessage(objectOutputStream, (GlobalChatMessage) message);
                }
                if (message instanceof SaveCommand) {
                    System.out.println("----save----");
                    handleSaveCommand((SaveCommand) message);
                }
                if(message instanceof BattleCommand){
                    System.out.println("-----battleCommand-----");
                    handleBattleCommand(objectOutputStream,(BattleCommand)message);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void handleBattleCommand(ObjectOutputStream objectOutputStream, BattleCommand message) {
        if(message instanceof BattleRequest){
           if(server.getBattleRequestPending().size()>0){
                String match = server.setMatch(authCode,((BattleRequest) message).getMode(), ((BattleRequest) message).getNumberOfFlags());
           }
        }
    }

    private void handleSaveCommand(SaveCommand message) {
        if (message.isDeckSave()) {
            Gson gson = new Gson();
            Type deckArrayListType = new TypeToken<ArrayList<Deck>>() {
            }.getType();
            ArrayList<Deck> decks = gson.fromJson(message.getDecks(), deckArrayListType);
            Deck selectedDeck = gson.fromJson(message.getSelectedDeck(), Deck.class);
            server.setDecks(decks, authCode);
            server.setSelectedDeck(selectedDeck, authCode);
            server.saveAccount(authCode);
        }
    }

    private void handleGlobalChatMessage(ObjectOutputStream objectOutputStream, GlobalChatMessage globalChatMessage) {
        try {
            if (!globalChatMessage.isUpdate())
                server.addToChatMessages(globalChatMessage.getMessage(), globalChatMessage.getAuthCode());
            else {
                objectOutputStream.writeObject(new GlobalChatMessage(server.getChatMessages(), authCode));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleTradeProcess(ObjectOutputStream objectOutputStream, Message message) throws IOException {
        TradeRequest tradeRequest = (TradeRequest) message;
        Account account = Server.getOnlineAccounts().get(message.getAuthCode()); //todo nulls are not handled
        int cost = Shop.getCost(tradeRequest.getObjectName());
        String name = tradeRequest.getObjectName();
        if (tradeRequest.isBuy()) {
            buyProcess(objectOutputStream, tradeRequest, account, cost, name);
        } else if (tradeRequest.isSell()) {
            sellProcess(objectOutputStream, tradeRequest, account, cost, name);
        }
    }

    private void sellProcess(ObjectOutputStream objectOutputStream, TradeRequest tradeRequest, Account account, int cost, String name) throws IOException {
        int result = Shop.sell(account, tradeRequest.getObjectName());
        if (cost == 0)
            result = 8;
        objectOutputStream.writeObject(new TradeResponse(authCode, false, tradeRequest.getObjectName(), result == 0, result, cost));
        if (result == 0) {
            Server.addUpdateOfShop(tradeRequest);
            Shop.changeNumbers(name, +1);
        }
    }

    private void buyProcess(ObjectOutputStream objectOutputStream, TradeRequest tradeRequest, Account account, int cost, String name) throws IOException {
        int result = Shop.buy(account, tradeRequest.getObjectName());
        if (cost == 0)
            result = 8;
        objectOutputStream.writeObject(new TradeResponse(authCode, true, tradeRequest.getObjectName(), result == 0, result, cost));
        if (result == 0) {
            Server.addUpdateOfShop(tradeRequest);
            Shop.changeNumbers(name, -1);
        }
    }

    public void sendToClient(Message message) {
        try {
            objectOutputStream.writeObject(message);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void handleScoreBoardCommands(ObjectOutputStream objectOutputStream, ScoreBoardCommand scoreBoardCommand) {
        try {
            objectOutputStream.writeObject(new ScoreBoardCommand("1", server.getAccountNames(), server.getUsersOnlineStatus(), server.getNumberOfWins()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //login based

    private void handleUtilsBasedCommand(ObjectOutputStream objectOutputStream, Utils utils) {
        if (utils.isLogout()) {
            server.saveAccount(utils.getAuthCode());
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
            authCode = server.generateAuthCode(userName, this);
            this.userName = userName;
            objectOutputStream.writeObject(new LoginBasedCommand(userName, password, true, authCode, true, loginErrorNumber, Account.getAccountByUserName(userName)));
            Account account = Server.getOnlineAccounts().get(authCode);
            ArrayList<ArrayList<String>> cards = Shop.getCards();
            ArrayList<ArrayList<String>> collectionCards = Collection.getCollectionCards(account);
            HashMap<String, int[]> movableCardsPowers = Shop.getMovableCardsPowers();
            HashMap<String, Integer> costs = Shop.getCosts();
            HashMap<String, Integer> numbers = Shop.getNumbers();
            HashSet<String> costumCards = Shop.getCostumeCards();
            HashMap<String, Integer> collectionCardNumbers = account.getCollection().getCardNumbers();
            objectOutputStream.writeObject(new UpdateWholeShop(collectionCardNumbers, account.getMoney(), costumCards, cards, collectionCards, movableCardsPowers, costs, numbers));
        } else {
            objectOutputStream.writeObject(new LoginBasedCommand("", "", false, "", true, loginErrorNumber, Account.getAccountByUserName(userName)));
        }
    }

    private void createAccountServerSide(ObjectOutputStream objectOutputStream, String userName, String password) throws IOException {
        if (server.isAccountAvailable(userName))
            objectOutputStream.writeObject(new LoginBasedCommand("", "", false, "", false, 0, null));
        else {
            LoginMenuProcess.createAccount(userName, password);
            objectOutputStream.writeObject(new LoginBasedCommand(userName, password, true, "", false, 0, Account.getAccountByUserName(userName)));
        }
    }

    public void updateShop(ObjectOutputStream objectOutputStream) throws IOException {
        updateCards = new ArrayList<>(Server.getLatestUpdates(lastUpdate.getValue()));
        if (updateCards.size() == 0)
            return;
        for (UpdateCards updateCard : updateCards) {
            objectOutputStream.writeObject(updateCard);

        }
        UpdateCards updateCards1 = updateCards.get(updateCards.size() - 1);
        lastUpdate = new Pair<>(updateCards1, Server.getIndexOfUpdate(updateCards1));
    }

    //login based

}

