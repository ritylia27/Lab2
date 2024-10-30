package GameSubject;

import Bot.Bot;
import BattlePlace.BattleMap;
import Unit.Unit;

import java.util.concurrent.TimeUnit;
import java.util.*;

public class Game {

    public static final String CONSOLE_FLUSH = "\033[H\033[2J";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_CYAN = "\u001B[36m";

    int wallet;
    int wood;
    int stone;
    BattleMap battleMap;

    Bot secondGamer;

    ArrayList<Unit> gamerUnitsArray = new ArrayList<>();
    ArrayList<Unit> secondGamerUnitsArray = new ArrayList<>();

    private HashMap<String, ArrayList<Integer>> unitsSpecsMap;
    private ArrayList<ArrayList<String>> unitsTyping;
    public Game(HashMap<String, ArrayList<Integer>> unitsList, ArrayList<ArrayList<String>> unitsTyping) {this.unitsSpecsMap = unitsList; this.unitsTyping = unitsTyping;}

    private final ArrayList<ArrayList<Float>> unitTypesPenalties = new ArrayList<>() {{
        add(new ArrayList<>(Arrays.asList(1f, 1.5f, 2f, 1.2f)));
        add(new ArrayList<>(Arrays.asList(1f, 1.5f, 2f, 1.2f)));
        add(new ArrayList<>(Arrays.asList(1f, 2.2f, 1.2f, 1.5f)));
    }};

    private static boolean isNotNumeric(String str) {
        try {
            Integer.parseInt(str);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    public void start() throws InterruptedException {
        System.out.println("Добро пожаловать в игру " + ANSI_RED + "BAUMAN'S GATE!" + ANSI_RESET);
        TimeUnit.SECONDS.sleep(1); //Пауза в 1 секунду
        battleMap = new BattleMap(15, 15, 8);
        fillWallet();
        secondGamer = new Bot(secondGamerUnitsArray, GameData.getDefaultUnitsTyping(), GameData.getDefaultUnits(), unitTypesPenalties, battleMap.getBasicFields());
    }

    public void buildHouses(HashMap<String, Integer[]> ListHouses, int wood, int stone, HashMap<String, Integer> ListBuiltHouses) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Хочешь ли построить или улучшить какое-либо здание?");
        for (String house : ListHouses.keySet()) System.out.println(house);
        System.out.println("У тебя " + wood + " дерева и " + stone + "камня.");
        while (wood > 0 && stone > 0) {
            System.out.println("Введи название здания:");
            String house = scanner.next();
            int priceWood = ListHouses.get(house)[0];
            int priceStone = ListHouses.get(house)[1];
            if (wood - priceWood >= 0 && stone - priceStone >= 0) {
                wood -= priceWood;
                stone -= priceStone;
                ListBuiltHouses.put(house, ListBuiltHouses.get(house)+1);
            }
        }
    }

    private void fillWallet() {
        wallet = 75;
    }

    String checkAnswer(String answ, ArrayList<String> checkList) {
        while (!checkList.contains(answ.toLowerCase())) {
            System.out.println("Введи ответ заново:");
            answ = new Scanner(System.in).nextLine().toLowerCase();
        }
        return answ;
    }

    private boolean inputGamerUnitsArray(HashMap<String, Integer> unitsPrices, ArrayList<Unit> gamerUnitsArrayToAdd) {
        HashMap<String, Integer> hasPurchasedStringMap = new HashMap<>();
        int sum = 0;
        boolean inputSucceed = true;
        boolean answ = false;
        while (true) {
            ArrayList<String> purchaseStringList = new ArrayList<>(Arrays.asList(new Scanner(System.in).nextLine().toLowerCase().split(" "))); // строка "Герой Кол-во" из ввода
            String purchaseUnitName;
            int purchaseUnitCount;
            switch (purchaseStringList.size()) {
                case (3):
                    purchaseUnitName = (purchaseStringList.getFirst() + " " + purchaseStringList.get(1)).toLowerCase();
                    break;
                case (2):
                    purchaseUnitName = purchaseStringList.getFirst().toLowerCase();
                    String supposedUnitName = (purchaseStringList.getFirst() + " " + purchaseStringList.get(1)).toLowerCase();
                    if (unitsPrices.containsKey(supposedUnitName)) {
                        System.out.println("Похоже, ты не указал количество воинов! Укажи здесь:");
                        purchaseStringList.add(new Scanner(System.in).nextLine());
                        purchaseUnitName = (purchaseStringList.getFirst() + " " + purchaseStringList.get(1)).toLowerCase();
                        break;
                    }
                    break;
                case (1):
                    purchaseUnitName = purchaseStringList.getFirst().toLowerCase();
                    if (unitsPrices.containsKey(purchaseUnitName)) {
                        System.out.println("Похоже, ты не указал количество воинов! Укажи здесь:");
                        purchaseStringList.add(new Scanner(System.in).nextLine());
                        break;
                    }
                    if (hasPurchasedStringMap.keySet().isEmpty()) {
                        System.out.println("Похоже, ты пытаешься купить пустой набор воинов. Так нельзя!");
                        return false;
                    }
                    if (Objects.equals(checkAnswer(purchaseStringList.getFirst(), new ArrayList<>(Arrays.asList("да", "нет"))), "да")) {
                        answ = true;
                        break;
                    } else return false;
                default:
                    System.out.println("Похоже, ты что-то неправильно написал:(");
                    return false;
            }
            if (!answ) {
                if (!unitsPrices.containsKey(purchaseUnitName)) {
                    System.out.println("Похоже, ты неправильно написал имя героя:(");
                    inputSucceed = false;
                }
                if (isNotNumeric(purchaseStringList.getLast())) {
                    System.out.println("Похоже, ты неправильно написал число воинов:(");
                    return false;
                } else {
                    purchaseUnitCount = Integer.parseInt(purchaseStringList.getLast());
                }
                if (!inputSucceed) {
                    return false;
                } else {
                    if (sum + unitsPrices.get(purchaseUnitName) * purchaseUnitCount > wallet) {
                        System.out.println("На столько героев у тебя не хватает денег, возьми меньше или возьми других:");
                        System.out.println("У тебя осталось " + ANSI_RED + (wallet - sum) + ANSI_RESET +  " денежек в кошельке");
                        continue;
                    }
                    sum += unitsPrices.get(purchaseUnitName) * purchaseUnitCount;
                    if (hasPurchasedStringMap.containsKey(purchaseUnitName)) {
                        int temp = hasPurchasedStringMap.get(purchaseUnitName);
                        hasPurchasedStringMap.put(purchaseUnitName, temp + purchaseUnitCount);
                    } else {
                        hasPurchasedStringMap.put(purchaseUnitName, purchaseUnitCount);
                    }
                }
            }
            System.out.println("У тебя осталось " + ANSI_RED + (wallet - sum) + ANSI_RESET + " монет");
            if ((wallet - sum) < Collections.min(unitsPrices.values()) || answ) {
                if (wallet != 0 && !answ) {
                    System.out.println("У тебя осталось недостаточно монет(");
                }
                System.out.println("Итого твой выбор:");
                for (String unitName : hasPurchasedStringMap.keySet()) {
                    System.out.println(unitName + " " + hasPurchasedStringMap.get(unitName));
                }
                System.out.println("Покупаем такой набор?:");
                String answer = new Scanner(System.in).nextLine().toLowerCase();
                if (Objects.equals(checkAnswer(answer, new ArrayList<>(Arrays.asList("да", "нет"))), "да")) {
                    String tempUnitName;
                    String tempPurchasedName;
                    String unitMapImage;
                    int order = 0;
                    for (String purchasedUnitName : hasPurchasedStringMap.keySet()) {
                        int countToBuy = hasPurchasedStringMap.get(purchasedUnitName);
                        for (int j = 0; j < countToBuy; j++) {
                            int a = 0;
                            tempPurchasedName = purchasedUnitName.substring(0, 1).toUpperCase() + purchasedUnitName.substring(1);
                            for (a = 0; a < unitsTyping.size(); a++) {
                                if (unitsTyping.get(a).contains(tempPurchasedName)) {
                                    break;
                                }
                            }
                            tempUnitName = tempPurchasedName + " " + (j + 1);
                            order++;
                            unitMapImage = colorByType(a) + order + ANSI_RESET;
                            gamerUnitsArrayToAdd.add(new Unit(unitMapImage, tempUnitName, unitsSpecsMap.get(tempPurchasedName), unitTypesPenalties.get(a),
                                    battleMap.getBasicFields()));
                        }
                    }
                    return true;
                } else return false;
            }
        }
    }

    public void setGamerUnitsArray() {
        printGamerUnitsArrayChoice();
        System.out.println("Сейчас у тебя " + ANSI_RED + wallet + ANSI_RESET + " монет!");
        String purchaseString = "Напиши в строку, каких и сколько героев хочешь взять, например: " + ANSI_GREEN + "Мечник 2" + ANSI_RESET;
        System.out.println(purchaseString);
        HashMap<String, Integer> unitsPrices = new HashMap<>();
        for (String unitName : unitsSpecsMap.keySet()) {
            unitsPrices.put(unitName.toLowerCase(), unitsSpecsMap.get(unitName).get(5));
        }
        while (!inputGamerUnitsArray(unitsPrices, gamerUnitsArray)) {
            fillWallet();
            System.out.println("Сейчас у тебя " + ANSI_RED + wallet + ANSI_RESET + " монет!");
            System.out.println("Давай еще раз:");
            System.out.println(purchaseString);
        }
        System.out.println("Твой набор героев:");
        for (Unit unit : gamerUnitsArray) {
            System.out.println(unit.getName() + "; ");
        }
    }

    private String formattedTypeOfUnitsColumnName(ArrayList<String> fields, ArrayList<Float> penalties) {
        return String.format(
                "Штраф за 1 клетку:  %s: %.1f   %s: %.1f   %s: %.1f   %s: %.1f",
                fields.getFirst(),
                penalties.getFirst(),
                fields.get(1),
                penalties.get(1),
                fields.get(2),
                penalties.get(2),
                fields.get(3),
                penalties.get(3)
        );
    }

    private String colorByType(int unitsTyping) {
        return switch (unitsTyping) {
            case (0) -> ANSI_CYAN;
            case (1) -> ANSI_RED;
            case (2) -> ANSI_BLUE;
            default -> ANSI_RESET;
        };
    }

    private String specsAlignmentByType(int unitsTyping) {
        String color = colorByType(unitsTyping);
        return "|  %2d  | " + color + "%-15s" + ANSI_RESET +
                " |    %2d    |  %2d   |        %2d       |   %2d   |      %2d       |    %3d    |\n";
    }

    private void printGamerUnitsArrayChoice() {
        System.out.println("Таблица воинов:");
        String divider = "+------+-----------------+----------+-------+-----------------+--------+---------------+-----------+\n";
        String columnNames = "|   №  |     Название    | Здоровье | Атака | Дальность атаки | Броня |  Перемещение  | Стоимость |\n";
        String footColumnName = "|      |            Пешие           |   " +
                formattedTypeOfUnitsColumnName(battleMap.getBasicFields(), unitTypesPenalties.getFirst()) + "      |\n";
        String archerColumnName = "|      |           Лучники          |   " +
                formattedTypeOfUnitsColumnName(battleMap.getBasicFields(), unitTypesPenalties.get(1)) + "      |\n";
        String horseColumnName = "|      |           Всадники         |   " +
                formattedTypeOfUnitsColumnName(battleMap.getBasicFields(), unitTypesPenalties.get(2)) + "      |\n";
        String[] typeColumnNames = {footColumnName, archerColumnName, horseColumnName};
        System.out.print(divider);
        Object[] tempSpecs = new Object[8];
        int iter = 1;
        for (int i = 0; i < unitsTyping.size(); i++) {
            System.out.print(typeColumnNames[i]);
            System.out.print(divider);
            System.out.print(columnNames);
            System.out.print(divider);
            for (int j = 0; j < unitsTyping.get(i).size(); j++) {
                tempSpecs[0] = iter++;
                tempSpecs[1] = unitsTyping.get(i).get(j);
                for (int a = 2; a < 8; a++) {
                    tempSpecs[a] = unitsSpecsMap.get(unitsTyping.get(i).get(j)).get(a - 2);
                }
                System.out.format(specsAlignmentByType(i), tempSpecs);
                System.out.print(divider);
            }
        }
    }

    private ArrayList<String> getCurrentUnitsState(ArrayList<Unit> unitsList) {
        ArrayList<String> result = new ArrayList<>();
        for (Unit unit : unitsList) {
            result.add("| " + unit.getMapImage() + ")" + unit.getName() + " - " + "Здоровье:" +
                    ANSI_GREEN + unit.getHealthPoints() + ANSI_RESET + "; Защита:" + ANSI_BLUE + unit.getDefensePoints() + ANSI_RESET +
                    "; Атака:" + ANSI_RED + unit.getAttackPoints() + ANSI_RESET + "; |");
            result.add("| Дальность атаки:" + unit.getAttackDistance() + "; Дальность перемещения:" + unit.getMovePoints() + " |");
        }
        return result;
    }

    private void printCurrentMapAndState() {
        ArrayList<String> currentUnitsStateLines = getCurrentUnitsState(gamerUnitsArray);
        int minLines = Math.min(battleMap.getSizeY(), currentUnitsStateLines.size());
        System.out.print("   ");
        for (int i = 1; i < 10; i++) {
            System.out.format("%d ", i);
        }
        for (int i = 10; i < 16; i++) {
            System.out.format("%d", i);
        }
        System.out.println(" X");
        for (int i = 0; i < minLines; i++) {
            printBattleMapLine(i);
            System.out.println("  " + currentUnitsStateLines.get(i));
        }
        int a = battleMap.getSizeY() - currentUnitsStateLines.size();
        if (a > 0) {
            for (int i = minLines; i < minLines + a; i++) {
                printBattleMapLine(i);
                System.out.println();
            }
        } else if (a < 0) {
            String emptyString = " ".repeat(3 + battleMap.getSizeX() * 2 + 2);
            for (int i = minLines; i < minLines - a; i++) {
                System.out.print(emptyString);
                System.out.println(currentUnitsStateLines.get(i));
            }
        }
        System.out.println("Y");
    }

    private void printBattleMapLine(int i) {
        String[] battleLine;
        System.out.format("%d ", i + 1);
        if (i < 9) {
            System.out.print(" ");
        }
        battleLine = battleMap.getBattleMapLine(i);
        for (int j = 0; j < battleMap.getSizeX(); j++) {
            System.out.print(battleLine[j] + " ");
        }
    }

    private void placeUnitsIntoMap(ArrayList<Unit> unitsToPlace, int row) {
        Unit eachUnitToPlace;
        int xCoord;
        ArrayList<Integer> placeCoords = new ArrayList<>() {{
            for (int i = 7; i >= 0; i--) {
                add(i);
            }
            int j = 0;
            for (int i = 1; i < 14; i += 2) {
                add(i, 8 + j);
                j++;
            }
        }};
        for (int i = 0; i < unitsToPlace.size(); i++) {
            eachUnitToPlace = unitsToPlace.get(i);
            xCoord = placeCoords.get(i);
            battleMap.placeSmth(eachUnitToPlace.getMapImage(), xCoord, row);
            eachUnitToPlace.move(xCoord, row);
        }
    }

    private void replaceUnitInMap(Unit moveUnit, int xMoveCoord, int yMoveCoord) {
        battleMap.placeSmth(battleMap.getBasicFields().getFirst(), moveUnit.getxCoord(), moveUnit.getyCoord());
        battleMap.placeSmth(moveUnit.getMapImage(), xMoveCoord, yMoveCoord);
        moveUnit.move(xMoveCoord, yMoveCoord);
    }

    public boolean game() throws InterruptedException {
        String inputHero, inputAction, inputAnswer;
        String secondGamerActionString;
        boolean canAttackAnyone;
        ArrayList<String> inputCoords = new ArrayList<>();
        ArrayList<String> attackableUnitsIndexListToString = new ArrayList<>();
        ArrayList<Integer> attackableUnitsIndexList = new ArrayList<>();
        Unit selectedUnit, attackableUnit;
        int inputHeroNum, xMoveCoord, yMoveCoord, secondGamerMove, secondGamerAct,
                secondGamerFirstArg, secondGamerSecondArg, secondGamerThirdArg;
        System.out.println("Каким образом играть? Сначала по номеру выбери героя, потом действие.");
        System.out.println("Вперед, игра началась!");
        ArrayList<String> heroCheck = new ArrayList<>() {{
            for (int i = 0; i < gamerUnitsArray.size(); i++) {
                add(String.format("%d", i + 1));
            }
        }};
        ArrayList<String> actionCheck = new ArrayList<>() {{
            for (int i = 0; i < 2; i++) {
                add(String.format("%d", i + 1));
            }
        }};
        ArrayList<String> answerCheck = new ArrayList<>() {{
            add("да");
            add("нет");
        }};
        placeUnitsIntoMap(gamerUnitsArray, 0);
        placeUnitsIntoMap(secondGamerUnitsArray, battleMap.getSizeY() - 1);
        while (!gamerUnitsArray.isEmpty() && !secondGamerUnitsArray.isEmpty()) {
            inputCoords.clear();
            canAttackAnyone = false;
            printCurrentMapAndState();
            System.out.println("Выбери героя для хода по номеру:");
            inputHero = checkAnswer(new Scanner(System.in).nextLine(), heroCheck);
            if (isNotNumeric(inputHero)) {
                System.out.println("Ты, похоже, написал не номер героя...");
                continue;
            } else {
                inputHeroNum = (Integer.parseInt(inputHero));
                if ((inputHeroNum > gamerUnitsArray.size()) || (inputHeroNum <= 0)) {
                    System.out.println("Ты, похоже, написал не тот номер...");
                    continue;
                } else {
                    selectedUnit = gamerUnitsArray.get(inputHeroNum - 1);
                }
            }
            System.out.println("Выбери действие: Передвижение - 1, атака - 2:");
            inputAction = checkAnswer(new Scanner(System.in).nextLine(), actionCheck);
            if (Objects.equals(inputAction, "2")) {
                attackableUnitsIndexList.clear();
                attackableUnitsIndexListToString.clear();
                for (int i = 0; i < secondGamerUnitsArray.size(); i++) {
                    if (selectedUnit.canAttack(secondGamerUnitsArray.get(i))) {
                        attackableUnitsIndexList.add(i);
                        canAttackAnyone = true;
                    }
                }
                if (!canAttackAnyone) {
                    System.out.println("Твой герой " + gamerUnitsArray.get(Integer.parseInt(inputHero) - 1).getName() +
                            " не может никого атаковать, ни до кого не достает!");
                    continue;
                } else {
                    if (attackableUnitsIndexList.size() == 1) {
                        System.out.println("Атаковать можешь только одного: " + secondGamerUnitsArray.get(attackableUnitsIndexList.getFirst()).getName() +
                                "\nАтакуешь?");
                        inputAnswer = checkAnswer(new Scanner(System.in).nextLine().toLowerCase(), answerCheck);
                        if (Objects.equals(inputAnswer, "да")) {
                            attackableUnit = secondGamerUnitsArray.get(attackableUnitsIndexList.getFirst());
                            attackableUnit.getDamage(selectedUnit.getAttackPoints());
                            if (attackableUnit.checkDeath()) {
                                battleMap.placeSmth(battleMap.getBasicFields().getFirst(), attackableUnit.getxCoord(), attackableUnit.getyCoord());
                                secondGamerUnitsArray.remove(attackableUnit);
                            }
                        } else continue;
                    } else {
                        System.out.println("Aтаковать можешь таких героев врага:");
                        for (Integer integer : attackableUnitsIndexList) {
                            System.out.println(integer + 1 + ")" + secondGamerUnitsArray.get(integer).getName());
                        }
                        System.out.println("Кого атакуешь? Напиши номер:");
                        for (Integer i : attackableUnitsIndexList) {
                            attackableUnitsIndexListToString.add(String.format("%d", i + 1));
                        }
                        inputAnswer = checkAnswer(new Scanner(System.in).nextLine().toLowerCase(), attackableUnitsIndexListToString);
                        attackableUnit = secondGamerUnitsArray.get(Integer.parseInt(inputAnswer) - 1);
                        attackableUnit.getDamage(selectedUnit.getAttackPoints());
                        if (attackableUnit.checkDeath()) {
                            battleMap.placeSmth(battleMap.getBasicFields().getFirst(), attackableUnit.getxCoord(), attackableUnit.getyCoord());
                            secondGamerUnitsArray.remove(attackableUnit);
                        }
                    }
                }
            } else if (Objects.equals(inputAction, "1")) {
                System.out.print("Напиши координаты, куда хочешь сходить - x y:");
                inputAnswer = new Scanner(System.in).nextLine();
                inputCoords.addAll(Arrays.asList(inputAnswer.split(" ")));
                while (true) {
                    if (inputCoords.size() != 2) {
                        System.out.println("Ты что-то написал не так! Напиши координаты еще раз:");
                    } else if (isNotNumeric(inputCoords.getFirst()) || isNotNumeric(inputCoords.get(1))) {
                        System.out.println("Ты написал не числа! Напиши координаты еще раз:");
                    } else {
                        xMoveCoord = Integer.parseInt(inputCoords.getFirst()) - 1;
                        yMoveCoord = Integer.parseInt(inputCoords.get(1)) - 1;
                        break;
                    }
                    inputAnswer = new Scanner(System.in).nextLine();
                    inputCoords.clear();
                    inputCoords.addAll(Arrays.asList(inputAnswer.split(" ")));
                }
                if (xMoveCoord > 14 || xMoveCoord < 0 || yMoveCoord > 14 || yMoveCoord < 0) {
                    System.out.println("Герои не могут выходить за рамки поля!");
                    continue;
                } else if (!Objects.equals(battleMap.getFieldByPosition(xMoveCoord, yMoveCoord), battleMap.getBasicFields().getFirst())) {
                    System.out.println("Герои могут делать ход только в обычную клетку!");
                    continue;
                } else if (!selectedUnit.canMove(xMoveCoord, yMoveCoord, battleMap)) {
                    System.out.println("Герой " + selectedUnit.getName() + " не может пойти в эту клетку!");
                    continue;
                } else {
                    replaceUnitInMap(selectedUnit, xMoveCoord, yMoveCoord);
                }
            } else {
                System.out.println("Похоже, ты что-то не так написал!");
                continue;
            }
            System.out.println("Результат твоего хода:");
            if (secondGamerUnitsArray.isEmpty()) {
                break;
            }
            printCurrentMapAndState();
            secondGamerMove = secondGamer.botMove(secondGamerUnitsArray, gamerUnitsArray, battleMap);
            secondGamerAct = secondGamerMove / (16 * 16 * 16);
            secondGamerFirstArg = (secondGamerMove % (16 * 16 * 16)) / (16 * 16);
            secondGamerSecondArg = (secondGamerMove % 256) / 16;
            secondGamerThirdArg = secondGamerMove % 16;
            switch (secondGamerAct) {
                case (1):
                    attackableUnit = gamerUnitsArray.get(secondGamerSecondArg);
                    attackableUnit.getDamage(
                            secondGamerUnitsArray.get(secondGamerFirstArg).getAttackPoints());
                    secondGamerActionString = secondGamerUnitsArray.get(secondGamerFirstArg).getName() + " атаковал твоего героя " +
                            attackableUnit.getName() + " и нанес " + secondGamerUnitsArray.get(secondGamerFirstArg).getAttackPoints() + " урона.";
                    if (attackableUnit.checkDeath()) {
                        battleMap.placeSmth(battleMap.getBasicFields().getFirst(), attackableUnit.getxCoord(), attackableUnit.getyCoord());
                        gamerUnitsArray.remove(attackableUnit);
                        secondGamerActionString = secondGamerUnitsArray.get(secondGamerFirstArg).getName() + " атаковал твоего героя " +
                                attackableUnit.getName() + " и убил его ";
                    }
                    break;
                case (2):
                    selectedUnit = secondGamerUnitsArray.get(secondGamerFirstArg);
                    replaceUnitInMap(selectedUnit, secondGamerSecondArg, secondGamerThirdArg);
                    secondGamerActionString = selectedUnit.getName() + " переместился на поле (" +
                            secondGamerSecondArg + "; " + secondGamerThirdArg + ").";
                    break;
                default:
                    continue;
            }
            System.out.println("Результат хода твоего противника:");
            System.out.println(secondGamerActionString);
            //printCurrentMapAndState();
        }
        if (gamerUnitsArray.isEmpty()) {
            System.out.println(ANSI_RED + "Ты проиграл!" + ANSI_RESET);
            secondGamerUnitsArray.clear();
            return false;
        } else {
            System.out.println(ANSI_GREEN + "Ты выиграл!" + ANSI_RESET);
            gamerUnitsArray.clear();
            return true;
        }
    }
}