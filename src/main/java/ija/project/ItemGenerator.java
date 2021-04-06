package ija.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.List;
import java.util.Random;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class ItemGenerator {
    public String name;
    public Goods goods;
    public int count;
    @JsonIgnore
    public List<Shelf> shelves;

    //empty constructor for jackson(yml)
    public ItemGenerator() {
    }

    public String getName() {
        return name;
    }

    public Goods getGoods() {
        return goods;
    }

    public int getCount() {
        return count;
    }

    @JsonIgnore
    public ItemGenerator(String name, Goods goods, int count) {
        this.name = name;
        this.goods = goods;
        this.count = count;
    }

    @JsonIgnore
    public void setShelves(List<Shelf> shelves) {
        this.shelves = shelves;
    }

    @JsonIgnore
    public void createIt(){
        Random random = new Random();
        int i;
        for (int cnt = 0; cnt < count; cnt++){
            i = random.nextInt(shelves.size());
            Item item = new Item(goods.getName() + cnt, goods, shelves.get(i));
        }
    }
}
