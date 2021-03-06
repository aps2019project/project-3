package model.client;

import model.Account;
import model.Message.BattleCommand.BattleRequest;
import model.Message.GlobalChatMessage;
import model.Message.LoginBasedCommand;
import model.Message.Message;
import model.Message.SaveCommand.SaveCommand;
import model.Message.ScoreBoardCommand.ScoreBoardCommand;
import model.Message.ShopCommand.UpdateAccount;
import model.Message.Utils;
import model.MyConstants;
import model.Server.Lock;

import java.io.*;
import java.net.Socket;

public class Client implements runnables.MessageListener {
    private String authCode;
    private static Client instance = new Client();
    private Socket clientSocket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private LoginBasedCommand loginBasedCommand = new LoginBasedCommand("", "", true);
    private ScoreBoardCommand scoreBoardCommand = new ScoreBoardCommand("",false,false,false);
    private SaveCommand saveCommand = new SaveCommand(false,"","","");
    private BattleRequest battleRequest = new BattleRequest("",0,0);
    private Utils utils = new Utils("",false);
    private final  Lock loginLock = new Lock();
    private final Lock shopLock = new Lock();
    private GlobalChatMessage globalChatMessage = new GlobalChatMessage("", "");
    private UpdateAccount updateAccount = new UpdateAccount("", new Account("", ""), false);
    private final Lock lock = new Lock();

    public void start() {
        try {
            connect2Server();
            initIOStreams();
            startThreads();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void connect2Server() throws IOException {
        System.out.println("connecting to server...");
        BufferedReader bf = new BufferedReader(new FileReader("src\\model\\Server\\config.txt"));
        String serverAddress = bf.readLine();
        int serverPort = Integer.parseInt(bf.readLine());
        bf.close();
        clientSocket = new Socket(serverAddress, serverPort);
        System.out.println(serverAddress + "/" + serverPort);
        System.out.println("client connected ...");
    }

    private void initIOStreams() throws IOException {
        outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        outputStream.flush();
        inputStream = new ObjectInputStream(clientSocket.getInputStream());
    }

    private void startThreads() {
        new Thread(new runnables.GetDataRunnable(inputStream)).start();
    }

    public void sendData(Message text) {
        try {
            outputStream.writeObject(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receive(Message message) {
    }

    public static Client getInstance() {
        return instance;
    }

    //getter & setter

    public LoginBasedCommand getLoginBasedCommand() {
        return loginBasedCommand;
    }

    public void setLoginBasedCommand(LoginBasedCommand loginBasedCommand) {
        this.loginBasedCommand = loginBasedCommand;
    }

    public void setScoreBoardCommand(ScoreBoardCommand scoreBoardCommand) {
        this.scoreBoardCommand = scoreBoardCommand;
    }

    public ScoreBoardCommand getScoreBoardCommand() {
        return scoreBoardCommand;
    }

    public GlobalChatMessage getGlobalChatMessage() {
        return globalChatMessage;
    }

    public void setGlobalChatMessage(GlobalChatMessage globalChatMessage) {
        this.globalChatMessage = globalChatMessage;
    }


    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public Lock getLock() {
        return loginLock;
    }

    public Lock getShopLock() {
        return shopLock;
    }

    public SaveCommand getSaveCommand() {
        return saveCommand;
    }

    public void setSaveCommand(SaveCommand saveCommand) {
        this.saveCommand = saveCommand;
    }

    public Utils getUtils() {
        return utils;
    }

    public void setUtils(Utils utils) {
        this.utils = utils;
    }

    public UpdateAccount getUpdateAccount() {
        return updateAccount;
    }

    public void setUpdateAccount(UpdateAccount updateAccount) {
        this.updateAccount = updateAccount;
    }

    //getter & setter
}