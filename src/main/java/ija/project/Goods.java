package ija.project;

import java.util.ArrayList;

public class Goods {
    private String name;
    private ArrayList<Item> items = new ArrayList<>();

    public Goods(String name) {

        this.name = name;
    }


    public void addItem(Item item){

        items.add(item);
    }

    public String getName() {
        return name;
    }

    public void removeItem(Item item){
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
