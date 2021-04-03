package ija.project;

import java.util.ArrayList;

public class Goods {
    private String name;
    private ArrayList<Item> items;

    public Goods(String name, ArrayList<Item> items) {
        this.name = name;
        this.items = items;
    }


    public void addItem(Item item){
        items.add(item);
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

        if (name != null ? !name.equals(goods.name) : goods.name != null) return false;
        return items != null ? items.equals(goods.items) : goods.items == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (items != null ? items.hashCode() : 0);
        return result;
    }
}
