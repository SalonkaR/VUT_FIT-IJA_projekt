package ija.project;

import java.util.List;
import java.util.Random;

public class ItemGenerator {
    public Goods goods;
    public int count;
    public List<Shelf> shelves;

    public ItemGenerator(Goods goods, int count) {
        this.goods = goods;
        this.count = count;
    }

    public void setShelves(List<Shelf> shelves) {
        this.shelves = shelves;
    }

    public void createIt(){
        Random random = new Random();
        int i;
        for (int cnt = 0; cnt < count; cnt++){
            i = random.nextInt(shelves.size());
            Item item = new Item(goods.getName() + cnt, goods, shelves.get(i));
        }
    }
}
