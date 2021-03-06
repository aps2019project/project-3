package presenter;

import com.google.gson.Gson;
import model.*;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class MainProcess {

    public static void readFiles() throws IOException {
        Gson gson = new Gson();
        readAccounts();
        readHeroes(gson);
        readMinions(gson);
        readSpells(gson);
        readUsableItems(gson);
        readCollectibleItems(gson);
    }

    private static void readCollectibleItems(Gson gson) throws IOException {
        File folder;
        File[] listOfFiles;
        folder = new File("src/model/items/collectibleitems");
        listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                Path path = new File(file.getPath()).toPath();
                Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
                CollectibleItem collectibleItem= gson.fromJson(reader, CollectibleItem.class);
                CollectibleItem.addToCollectibleItems(collectibleItem);
                reader.close();
            }
        }
    }

    private static void readUsableItems(Gson gson) throws IOException {
        File folder;
        File[] listOfFiles;
        folder = new File("src/model/items/usableitems");
        listOfFiles = folder.listFiles();
        System.out.println("***");
        for (File file : listOfFiles) {
            if (file.isFile()) {
                Path path = new File(file.getPath()).toPath();
                Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
                UsableItem usableItem = gson.fromJson(reader, UsableItem.class);
                System.out.println("***" + usableItem.getName());
                System.out.flush();
                UsableItem.addToUsableItems(usableItem);
                Shop.addToItems(usableItem);
                reader.close();
            }
        }
    }

    private static void readSpells(Gson gson) throws IOException {
        File folder;
        File[] listOfFiles;
        folder = new File("src/model/spells");
        listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                Path path = new File(file.getPath()).toPath();
                Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
                Spell spell = gson.fromJson(reader, Spell.class);
                Spell.addToSpells(spell);
                Shop.addToSpells(spell);
                reader.close();
            }
        }
    }

    private static void readMinions(Gson gson) throws IOException {
        File folder;
        File[] listOfFiles;
        folder = new File("src/model/minions");
        listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                Path path = new File(file.getPath()).toPath();
                Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
                Minion minion = gson.fromJson(reader, Minion.class);
                Minion.addToMinions(minion);
                Shop.addToMinions(minion);
                reader.close();
            }
        }
    }

    private static void readHeroes(Gson gson) throws IOException {
        File folder;
        File[] listOfFiles;
        folder = new File("src/model/heroes");
        listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                Path path = new File(file.getPath()).toPath();
                Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
                Hero hero = gson.fromJson(reader, Hero.class);
                Hero.addToHeroes(hero);
                Shop.addToHeroes(hero);
                reader.close();
            }
        }
    }

    static void readAccounts() throws IOException {
        Account.getAccounts().clear();
        File folder = new File("src/model/accounts");
        File[] listOfFiles = folder.listFiles();
        Gson gson = new Gson();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                Path path = new File(file.getPath()).toPath();
                Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
                        Account account = gson.fromJson(reader, Account.class);
                Account.getAccounts().add(account);
                reader.close();
            }
        }
    }
}
