package Houses;

import java.util.HashMap;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class Market extends House {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public Market() {
        super("Рынок", 50, 10);
    }

    public static boolean answerYorN() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Хотете сделать ещё обмен?");
        if (scanner.nextLine().equals("Нет")) {
            return false;
        } else {return true;}
    }

    public static void iterationExchange(HashMap<String, Integer> resourses) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите сколько ресурсов и на что вы хотите обменять. Курс 1:1. \nНапример:" + ANSI_GREEN + "10 Дерево на Монеты" + ANSI_RESET);
        String[] data = scanner.nextLine().split(" ");
        try {
            if (resourses.containsKey(data[1]) && resourses.containsKey(data[2])) {
                resourses.put(data[1], resourses.get(data[1])-parseInt(data[0]));
                resourses.put(data[2], resourses.get(data[2])+parseInt(data[0]));
            } else {
                System.out.println(ANSI_RED + "Вы неверно ввели название ресурсов!" + ANSI_RESET);
            }
        } catch (NumberFormatException e) {
            System.out.println(ANSI_RED + "Вы не ввели в начале число!" + ANSI_RESET);
        }
    }

    public static void changeResources(int wallet, int wood, int stone) {
        int finalWallet = wallet; int finalWood = wood; int finalStone = stone;
        HashMap<String, Integer> resourses = new HashMap<>() {{
            put("Монеты", finalWallet);
            put("Дерево", finalWood);
            put("Камень", finalStone);
        }};
        do {
            iterationExchange(resourses);
            wallet = resourses.get("Монеты"); wood = resourses.get("Дерево"); stone = resourses.get("Камень");
            System.out.println("Сейчас у вас: " + wallet + "монеты, " + wood + " дерево, " + stone + " камень.");
        } while (answerYorN());
    }


}
