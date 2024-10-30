package Houses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class Academy extends House {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    public Academy() { super("Академия", 40, 40); }
    private static final String[] nameParameters = {"здоровье", "атаку", "дальность атаки", "броню", "дальность перемещения", "стоимость"};

    public static String nameUnit() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите имя юнита:");
        return scanner.next();
    }
    public static int typeUnit() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите тип юнита:\n1. Пеший;\n2. Лучник;\n3. Всадник.");
        return scanner.nextInt()-1;
    }
    public static ArrayList<Integer> parametersUnit() {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Integer> parameters = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            System.out.println("Введите " + nameParameters[i] + " юнита (1-99) :");
            parameters.add(parseInt(scanner.next()));
        }
        return parameters;
    }
    public static void addUnit(HashMap<String, ArrayList<Integer>> unitsSpecsMap, ArrayList<ArrayList<String>> unitsTyping) {
        String unitName = nameUnit();
        int typeUnit = typeUnit();
        ArrayList<Integer> unitParameters = parametersUnit();
        if (!unitsSpecsMap.containsKey(unitName)) {
            unitsSpecsMap.put(unitName, unitParameters);
            unitsTyping.get(typeUnit).add(unitName);
        } else {
            System.out.println("Такой юнит уже существует.");
        }
    }
    public static void updateUnits(HashMap<String, ArrayList<Integer>> unitsSpecsMap, ArrayList<ArrayList<String>> unitsTyping, String house) {
        int position = Arrays.asList("Дом лекаря", "Кузница", "Арсенал").indexOf(house);
        if (position == 2) {position++;}
        for (int i = 0; i < unitsTyping.size(); i++) {
            for (int j = 0; j < unitsTyping.get(i).size(); j++) {
                String keyUnit = unitsTyping.get(i).get(j);
                ArrayList<Integer> newParameters = unitsSpecsMap.get(keyUnit); newParameters.set(position, newParameters.get(position)+1);
                unitsSpecsMap.put(house, newParameters);
            }
        }
        System.out.println(Arrays.asList("Здоровье", "Атака", "", "Броня").get(position) + " всех юнитов " + ANSI_RED + "+1" + ANSI_RESET);
    }

}
