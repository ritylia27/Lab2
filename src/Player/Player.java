package Player;

import BattlePlace.BattleMap;
import GameSubject.GameData;
import Houses.Academy;
import Houses.House;
import Houses.Market;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Player {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/players";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";
    public int truPassword = 0;
    public String progress;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public void start() {

        try {
            // Установка соединения с базой данных
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Создание таблицы для хранения пользователей
            String createTableSQL = "CREATE TABLE IF NOT EXISTS users (id SERIAL PRIMARY KEY, username VARCHAR(50) UNIQUE, password VARCHAR(255), salt VARCHAR(255), progress VARCHAR(50))";
            connection.createStatement().executeUpdate(createTableSQL);
            System.out.println(ANSI_RED + "\tBAUMAN'S GATE!" + ANSI_RESET + "\nВойдите в учётную запись!\n");
            Scanner scanner = new Scanner(System.in);
            System.out.println("Выберите действие: ");
            System.out.println("1. Создать новую учётную запись");
            System.out.println("2. Войти в существующую учётную запись");
            int choice = scanner.nextInt();

            if (choice == 1) {
                System.out.println("Введите логин: ");
                String username = scanner.next();
                System.out.println("Введите пароль: ");
                String password = scanner.next();

                // Хэширование пароля
                String[] hashedPasswordAndSalt = hashPassword(password);
                String hashedPassword = hashedPasswordAndSalt[0];
                String salt = hashedPasswordAndSalt[1];

                //Создание дефолтных файлов прогресса и карт
                String progressFile = username + "Progress.dat";
                Progress.saveProgress(GameData.getDefaultHouses(), GameData.getDefaultUnits(), new HashMap<>(),  GameData.getDefaultUnitsTyping(), progressFile);

                // Добавление пользователя в базу данных
                String insertUserSQL = "INSERT INTO users(username, password, salt, progress) VALUES (?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertUserSQL);
                preparedStatement.setString(1, username); preparedStatement.setString(2, hashedPassword);
                preparedStatement.setString(3, salt); preparedStatement.setString(4, progressFile);
                preparedStatement.executeUpdate();

                System.out.println(ANSI_GREEN + "Учётная запись создана успешно." + ANSI_RESET);
                progress = progressFile;
                truPassword = 1;
            } else if (choice == 2) {
                System.out.println("Введите логин: ");
                String username = scanner.next();
                System.out.println("Введите пароль: ");
                String password = scanner.next();

                // Поиск пользователя в базе данных
                String findUserSQL = "SELECT password, salt, progress FROM users WHERE username = ?";
                PreparedStatement findUserStatement = connection.prepareStatement(findUserSQL);
                findUserStatement.setString(1, username);
                ResultSet resultSet = findUserStatement.executeQuery();

                if (resultSet.next()) {
                    String storedHashedPassword = resultSet.getString("password");
                    String storedSalt = resultSet.getString("salt");
                    String progressFile = resultSet.getString("progress");

                    // Хэширование введенного пароля с использованием сохраненной соли
                    String[] hashedPasswordAndSalt = hashPassword(password, storedSalt);
                    String hashedPassword = hashedPasswordAndSalt[0];

                    if (storedHashedPassword.equals(hashedPassword)) {
                        System.out.println(ANSI_GREEN + "Успешный вход в учётную запись." + ANSI_RESET);
                        progress = progressFile;
                        truPassword = 1;
                    } else {
                        System.out.println(ANSI_RED + "Ошибка входа. Неверный пароль." + ANSI_RESET);
                    }
                } else {
                    System.out.println(ANSI_RED + "Ошибка входа. Нет пользователя с таким логином." + ANSI_RESET);
                }
            } else {System.out.println(ANSI_RED + "Ошибка входа." + ANSI_RESET);}

            // Закрытие транзакции
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String[] hashPassword(String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = factory.generateSecret(spec).getEncoded();

        // Возвращаем хэшированный пароль и соль
        return new String[] { Base64.getEncoder().encodeToString(hash), Base64.getEncoder().encodeToString(salt) };
    }

    private static String[] hashPassword(String password, String saltString) throws InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] salt = Base64.getDecoder().decode(saltString);

        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = factory.generateSecret(spec).getEncoded();

        // Возвращаем хэшированный пароль и соль
        return new String[] { Base64.getEncoder().encodeToString(hash), saltString };
    }
}
