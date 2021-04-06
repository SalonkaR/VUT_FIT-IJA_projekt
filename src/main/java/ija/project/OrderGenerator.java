package ija.project;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class OrderGenerator {
    private String name;
    private int maxGoods;
    private int maxItams;
    private int cnt;
    
    private List<Shelf> shelves;
    private List<Goods> goods;
    private Random random = new Random();

    public OrderGenerator(String name, int maxGoods, int maxItams) {
        this.name = name;
        this.maxGoods = maxGoods;
        this.maxItams = maxItams;
    }
    
    public void generate(){
        
    }

    public void setShelves(List<Shelf> shelves) {
        this.shelves = shelves;
    }

    public void setGoods(List<Goods> goods) {
        this.goods = goods;
    }
}
