package ija.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.ArrayList;
import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class Goods {
    private String name;
    //@JsonIgnore
    private ArrayList<Item> items = new ArrayList<>();

    //empty constructor for jackson(yml)
    public Goods() {
    }

    public Goods(String name) {
        this.name = name;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public List<Item> getItems(){
        return items;
    }

    public String getName() {
        return name;
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public int size(){
        return items.size();
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

    public void print() {
        System.out.println(items);
    }
}
