import BattlePlace.BattleMap;
import GameSubject.Game;
import Houses.Academy;
import Houses.House;
import Houses.Market;
import Player.Player;
import Player.Progress;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_CYAN = "\u001B[36m";

    public static boolean answerYesOrNo() {
        while (true) {
            String answer = new Scanner(System.in).nextLine().toLowerCase();
            if (answer.equals("нет")) {
                return false;
            } else if (answer.equals("да")) { return true; } else {
                System.out.println("Ответом может быть лишь Да/Нет");
            }
        }

    }

    public static void main(String[] args) throws InterruptedException, IOException {
        Scanner scanner = new Scanner(System.in);

        //Вход в систему
        Player user = new Player();
        while (user.truPassword == 0) {
            user.start();
        }

        //Загружаем весь прогресс из файла юзера в объекты
        String progress = user.progress; Progress.loadProgress(progress);
        HashMap<String, House> listHouses = Progress.getHousesProgress();
        HashMap<String, ArrayList<Integer>> listUnits = Progress.getUnitsProgress();
        HashMap<String, BattleMap> listMaps = Progress.getMaps();
        ArrayList<ArrayList<String>> unitsTyping = Progress.getUnitsTypingProgress();
        int wallet = Progress.getWa

        //Постройка домов
        System.out.println(ANSI_YELLOW + "\n\tДобро подаловать в ваш город!" + ANSI_RESET);
        for (String house : listHouses.keySet()) {
            if (house.equals("Академия") && listHouses.get(house).getLevel() == 1) {System.out.println(house + ANSI_GREEN + " Можно создать своего юнита!" + ANSI_RESET);}
            else if (house.equals("Рынок") && listHouses.get(house).getLevel() == 1) {System.out.println(house + ANSI_YELLOW + " Можно обменять ресурсы!" + ANSI_RESET);}
            else {System.out.println(listHouses.get(house).getInfo());}
        }
        System.out.println("Хотите ли что-то улучшить? (Да/Нет)");
        if (answerYesOrNo()) {
            boolean continueBuilding = true;
            while (continueBuilding) {
                System.out.println("Какое здание хотите улучшить или построить (введите название) :");
                String buildHouse = scanner.nextLine();
                if (buildHouse.equals("Академия")) {
                    if (listHouses.get(buildHouse).getLevel() == 0) { listHouses.get(buildHouse).upgradeLevel(); }
                    System.out.println(ANSI_YELLOW +"Создадим нового юнита!" + ANSI_RESET);
                    Academy.addUnit(listUnits, unitsTyping);
                } else if (buildHouse.equals("Рынок")) {
                    if (listHouses.get(buildHouse).getLevel() == 0) { listHouses.get(buildHouse).upgradeLevel(); }
                    System.out.println(ANSI_YELLOW + "Тут можно обменять ресурсы." + ANSI_RESET);
                    Market.changeResources(wallet, wood, stone);
                } else if (listHouses.containsKey(buildHouse)) {
                    listHouses.get(buildHouse).upgradeLevel();
                    Academy.updateUnits(listUnits, unitsTyping, buildHouse);
                } else { System.out.println("Ты неправильно ввёл название.");}

                System.out.println("Хочешь построить ещё что-то? (Да/Нет)");
                continueBuilding = answerYesOrNo();
            }
        }

        System.out.println("\n\tВы можете создавать и сохранять карты для последующих игр!");
        if (!listMaps.isEmpty()) {
            System.out.println("Ваши карты:"); int i = 0;
            for (String nameMap : listMaps.keySet()) {i++; System.out.println(i + ". " + nameMap);}
        } else {
            System.out.println("У вас пока нет карт!");
        }


        Progress.saveProgress(listHouses, listUnits, listMaps, unitsTyping, progress);


        boolean continuePlaying = true;
        int userWins = 0; int botWins = 0;
        Game game = new Game(listUnits, unitsTyping);
        while (continuePlaying) {
            game.start();
            game.setGamerUnitsArray();
            boolean userWon = game.game();

            if (userWon) {
                userWins++;
                System.out.println("Ты выиграл этот раунд!");
            } else {
                botWins++;
                System.out.println("Бот выиграл этот раунд.");
            }

            System.out.println("Счёт: Ты - " + userWins + " : Бот - " + botWins);
            System.out.println("Хочешь сыграть ещё раз? (да/нет)");

            String answer = scanner.nextLine().toLowerCase();
            if (answer.equals("нет")) {
                continuePlaying = false;
            }
        }

        System.out.println("Спасибо за игру!");
    }
}
