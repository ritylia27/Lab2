package Player;
import BattlePlace.BattleMap;
import Houses.House;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Progress {
    private static HashMap<String, House> houses = new HashMap<>();
    private static HashMap<String, ArrayList<Integer>> units = new HashMap<>();
    private static HashMap<String, BattleMap> maps = new HashMap<>();
    private static ArrayList<ArrayList<String>> unitsTyping = new ArrayList<>();

    public static void saveProgress(HashMap<String, House> houses, HashMap<String, ArrayList<Integer>> units, HashMap<String, BattleMap> maps, ArrayList<ArrayList<String>> unitsTyping, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(houses);
            oos.writeObject(units);
            oos.writeObject(maps);
            oos.writeObject(unitsTyping);
            System.out.println("Прогресс успешно сохранён в файл: " + filename);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении прогресса: " + e.getMessage());
        }
    }

    public static void loadProgress(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            houses = (HashMap<String, House>) ois.readObject();
            units = (HashMap<String, ArrayList<Integer>>) ois.readObject();
            maps = (HashMap<String, BattleMap>) ois.readObject();
            unitsTyping = (ArrayList<ArrayList<String>>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка при загрузке прогресса: " + e.getMessage());
        }
    }

    public static HashMap<String, House> getHousesProgress() {return houses;}
    public static HashMap<String, ArrayList<Integer>> getUnitsProgress() {return units;}
    public static HashMap<String, BattleMap> getMaps() {return maps;}
    public static ArrayList<ArrayList<String>> getUnitsTypingProgress() {return unitsTyping;}


}
