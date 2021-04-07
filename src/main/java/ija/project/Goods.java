package ija.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class Goods {
    private String name;
    private double weight;
    @JsonIgnore
    private ArrayList<Item> freeItems = new ArrayList<>();
    @JsonIgnore
    private ArrayList<Item> reservedItems = new ArrayList<>();

    //empty constructor for jackson(yml)
    public Goods() {
    }

    public Goods(String name) {
        this.name = name;
    }

    public void addItem(Item item) {
        freeItems.add(item);
    }

    public List<Item> getFreeItems(){
        return freeItems;
    }

    public String getName() {
        return name;
    }

    public void removeItem(Item item) {
        freeItems.remove(item);
    }

    public void reserveItem(List<Item> lst){
        for (Item item : lst) {
            freeItems.remove(item);
            reservedItems.add(item);
        }
    }

    public void reserveItem(Item item){
        freeItems.remove(item);
        reservedItems.add(item);
    }

    public int sizeFree(){
        return freeItems.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Goods)) return false;

        Goods goods = (Goods) o;

        return name != null ? name.equals(goods.name) : goods.name == null;
    }

    @Override
    public int hashCode() {
        return 0;
    }


    public HashMap<Double, List<Item>> getItems(Integer psc) {
        HashMap<Double, List<Item>> map = new HashMap<>();
        List<Item> forRes = new ArrayList<>();

        int cnt = 0;
        for(Item item : freeItems){
            if(map.containsKey(item.getShelf().getPosition().getY())){
                map.get(item.getShelf().getPosition().getY()).add(item);
            } else {

                List<Item> lst = new ArrayList<>();
                lst.add(item);
                map.put(item.getShelf().getPosition().getY(), lst);
            }
            forRes.add(item);
            cnt += 1;
            if (cnt == psc){
                reserveItem(forRes);
                return map;
            }

        }
        reserveItem(forRes);
        return map;
    }

    @Override
    public String toString() {
        return "Goods:" + name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
