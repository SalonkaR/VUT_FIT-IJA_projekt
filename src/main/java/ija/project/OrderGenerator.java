package ija.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class OrderGenerator {
    private String name;
    private int maxGoods;
    private int maxItems;
    @JsonIgnore
    private int count;
    @JsonIgnore
    private DropPoint dropPoint;
    @JsonIgnore
    private List<Goods> goods;

    //empty constructor for jackson(yml)
    public OrderGenerator() {
    }

    public String getName() {
        return name;
    }

    public int getMaxGoods() {
        return maxGoods;
    }

    public int getMaxItems() {
        return maxItems;
    }

    @JsonIgnore
    public OrderGenerator(String name, int maxGoods, int maxItems) {
        this.name = name;
        this.maxGoods = maxGoods;
        this.maxItems = maxItems;
    }
    public void generate(){
        Random random = new Random();
        HashMap<Goods, Integer> list = new HashMap<Goods, Integer>();
        int r;
        int j;
        for (int i = 0; i < maxGoods; i++){
            r = random.nextInt(goods.size());
            j = random.nextInt(maxItems);
            if (j == 0){
                j = 1;
            }
            list.put(goods.get(r), j);
        }
        Order order = new Order(this.name, this.dropPoint, list);
    }

    public void setGoods(List<Goods> goods) {
        this.goods = goods;
    }

    public void setDropPoint(DropPoint dropPoint) {
        this.dropPoint = dropPoint;
    }
}
