package Houses;

import java.io.Serializable;

public class House implements Serializable {
    private int level;
    private String name;
    private int woodPrice;
    private int stonePrice;
    public House(String name, int woodPrice, int stonePrice) {
        this.level = 0;
        this.name = name;
        this.woodPrice = woodPrice;
        this.stonePrice = stonePrice;
    }

    public String getName() {return name;}
    public int getLevel() {return level;}
    public int getWoodPrice() {return (int) (woodPrice * (1 + level * 0.2));}
    public int getStonePrice() {return (int) (stonePrice * (1 + level * 0.2));}
    public String getInfo() {
        String price = "\t\t\tдерево: " + getWoodPrice() + "\tкамень: " + getStonePrice();
        if (level == 0) {
            return name + " (Нет в городе)" + price;
        } else {
            return name + " (Уровень " + level + ")" + price;
        }
    }

    public void upgradeLevel() {
        level++;
    }

}
