package GameSubject;

import Houses.House;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class GameData implements Serializable {

    private static final Integer wallet = 75;
    private static final Integer wood = 50;
    private static final Integer stone = 50;

    public static Integer getWallet() {return wallet;}
    public static Integer getWood() {return wood;}
    public static Integer getStone() {return stone;}
    private static final ArrayList<ArrayList<String>> unitsTyping = new ArrayList<>() {{
        add(new ArrayList<>(Arrays.asList("Мечник", "Копьеносец", "Топорщик")));
        add(new ArrayList<>(Arrays.asList("Тяжелый лучник", "Легкий лучник", "Арбалетчик")));
        add(new ArrayList<>(Arrays.asList("Рыцарь", "Кирасир", "Конный лучник")));
    }};
    public static ArrayList<ArrayList<String>> getDefaultUnitsTyping() { return unitsTyping; }

    private static final HashMap<String, House> defaultHouses = new HashMap<>() {{
        put("Академия", new House("Академия", 20, 20));
        put("Рынок", new House("Рынок", 20, 20));
        put("Кузница", new House("Кузница", 5, 15));
        put("Дом лекаря", new House("Дом лекаря", 5, 5));
        put("Арсенал", new House("Арсенал", 2, 10));
    }};
    public static HashMap<String, House> getDefaultHouses() { return defaultHouses; }

    private static final HashMap<String, ArrayList<Integer>> defaultUnits = new HashMap<>() {{
        //Foot units
        // Мечник - HP, атака, дальность, броня, перемещение, стоимость
        put("Мечник", new ArrayList<>(Arrays.asList(50, 5, 1, 8, 3, 10)));
        put("Копьеносец", new ArrayList<>(Arrays.asList(50, 12, 5, 20, 5, 30)));
        put("Топорщик", new ArrayList<>(Arrays.asList(45, 9, 1, 3, 4, 20)));
        //Bow units
        put("Тяжелый лучник", new ArrayList<>(Arrays.asList(30, 6, 5, 8, 2, 15)));
        put("Легкий лучник", new ArrayList<>(Arrays.asList(25, 3, 3, 4, 4, 19)));
        put("Арбалетчик", new ArrayList<>(Arrays.asList(40, 7, 6, 3, 2, 23)));
        //Horse units
        put("Рыцарь", new ArrayList<>(Arrays.asList(30, 5, 1, 3, 6, 20)));
        put("Кирасир", new ArrayList<>(Arrays.asList(50, 2, 1, 7, 5, 23)));
        put("Конный лучник", new ArrayList<>(Arrays.asList(25, 3, 3, 2, 5, 25)));
    }};

    public static HashMap<String, ArrayList<Integer>> getDefaultUnits() { return defaultUnits; }
}
