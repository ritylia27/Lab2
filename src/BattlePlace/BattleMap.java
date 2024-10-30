package BattlePlace;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class BattleMap implements Serializable {
    int sizeX, sizeY;
    private final String[][] battleMapMatrix;
    private final ArrayList<String> basicFields = new ArrayList<>(Arrays.asList("▓", "#", "@", "!"));
    private final ArrayList<String> fieldsToChoose = basicFields;

    public BattleMap(int mapSizeX, int mapSizeY) {
        sizeX = mapSizeX;
        sizeY = mapSizeY;
        battleMapMatrix = new String[mapSizeY][mapSizeX];
        for (int i = 0; i < sizeY; i++) {
            for (int j = 0; j < sizeX; j++) {
                battleMapMatrix[i][j] = basicFields.getFirst();
            }
        }
    }

    public BattleMap(int mapSizeX, int mapSizeY, int dificulty) {
        sizeX = mapSizeX;
        sizeY = mapSizeY;
        battleMapMatrix = new String[mapSizeY][mapSizeX];
        for (int i = 1; i < dificulty; i++) {
            fieldsToChoose.add(basicFields.getFirst());
        }
        fillFieldsArray();
        for (int j = 0; j < mapSizeX; j++) {
            battleMapMatrix[0][j] = basicFields.getFirst();
        }
        for (int j = 0; j < mapSizeX; j++) {
            battleMapMatrix[mapSizeY - 1][j] = basicFields.getFirst();
        }
        for (int i = 1; i < mapSizeY - 1; i++) {
            for (int j = 0; j < mapSizeX; j++) {
                battleMapMatrix[i][j] = fieldsToChoose.get((int) (Math.random() * fieldsToChoose.size()));
            }
        }
    }

    private void fillFieldsArray() {

    }

    public String[] getBattleMapLine(int index) {
        return battleMapMatrix[index];
    }

    public int getSizeY() {
        return sizeY;
    }

    public int getSizeX() {
        return sizeX;
    }

    public String getFieldByPosition(int xCoord, int yCoord) {
        return battleMapMatrix[yCoord][xCoord];
    }

    public ArrayList<String> getBasicFields() {
        return basicFields;
    }

    public void placeSmth(String smth, int xCoord, int yCoord) {
        battleMapMatrix[yCoord][xCoord] = smth;
    }
}