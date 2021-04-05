package ija.project;

import java.util.ArrayList;
import java.util.List;

public class Data {
    private List<Shelf> shelves;
    private List<Regal> regals;
    private List<Goods> goods;
    private List<Item> items;

    public Data(){
        shelves = new ArrayList<>();
        regals = new ArrayList<>();
        goods = new ArrayList<>();
        items = new ArrayList<>();
    }

    public List<Shelf> getShelves(){
        return shelves;
    }

    public List<Regal> getRegals() {
        return regals;
    }

    public List<Goods> getGoods() {
        return goods;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setShelves(List<Shelf> shelves){
        this.shelves = shelves;
    }

    public void setRegals(List<Regal> regals) {
        this.regals = regals;
    }

    public void setGoods(List<Goods> goods) {
        this.goods = goods;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Data{" +
                "shelves=" + shelves +
                ", regals=" + regals +
                '}';
    }
}
