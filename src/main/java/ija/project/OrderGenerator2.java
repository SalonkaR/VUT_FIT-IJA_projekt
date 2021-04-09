package ija.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class OrderGenerator2 {
    private String name;
    private int time;
    private int maxGoods;
    private int maxItems;
    private DropPoint dropPoint;

    @JsonIgnore
    private int cnt = 0;
    @JsonIgnore
    private int cntTime = 0;
    @JsonIgnore
    private Random random = new Random();
    @JsonIgnore
    private List<Goods> goods;

    //empty constructor for jackson(yml)
    public OrderGenerator2() {
    }

    @JsonIgnore
    public OrderGenerator2(String name, DropPoint point, int time, int maxItems, int maxGoods) {
        this.name = name;
        this.time = time;
        this.dropPoint = point;
        this.maxItems = maxItems;
        this.maxGoods = maxGoods;
    }

    public void reset(){
        cnt = 0;
        cntTime = 0;
    }

    @JsonIgnore
    public void generate(){
        cntTime += 1;


        if (cntTime == time){
            cntTime = 0;
            int numItems = 0;
            int currentNumItems = 0;
            HashMap<Goods, Integer> list = new HashMap<>();

            int numOfGoods = 0;
            while (numOfGoods == 0) {
                numOfGoods = random.nextInt(maxGoods + 1);
            }

            for (int i = 0; i<= numOfGoods; i++){
                if (maxItems - currentNumItems < 2){
                    currentNumItems -= 2;
                }
                while (numItems == 0) {
                    numItems = random.nextInt(maxItems - currentNumItems);
                }
                currentNumItems += numItems;
                list.put(goods.get(random.nextInt(goods.size())), numItems);
            }

            new Order(name + "/" + cnt, dropPoint, list);
            cnt += 1;
        }
    }
    @JsonIgnore
    public void setGoods(List<Goods> goods) {
        this.goods = goods;
    }

    public String getName() {
        return name;
    }


    public int getCnt() {
        return cnt;
    }


    public int getTime() {
        return time;
    }


    public int getCntTime() {
        return cntTime;
    }


    public int getMaxItems() {
        return maxItems;
    }


    public int getMaxGoods() {
        return maxGoods;
    }


    public Random getRandom() {
        return random;
    }


    public DropPoint getDropPoint() {
        return dropPoint;
    }


    public List<Goods> getGoods() {
        return goods;
    }
}
