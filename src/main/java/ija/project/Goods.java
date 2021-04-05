package ija.project;

import java.util.*;

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


    public HashMap<Double, List<Item>> getItems(Integer psc) {
        HashMap<Double, List<Item>> map = new HashMap<>();
        HashMap<Double, List<Item>> res = new HashMap<>();

        for(Item item : items){
            if (!item.isReserve()){
                if(map.containsKey(item.getShelf().getPosition().getY())){
                    map.get(item.getShelf().getPosition().getY()).add(item);
                } else {
                    List<Item> lst = new ArrayList<>();
                    lst.add(item);
                    map.put(item.getShelf().getPosition().getY(), lst);
                }
            }
        }

        SortedSet<Double> keys = new TreeSet<>(map.keySet());
        int cnt = 0;
        for (double key : keys) {
            ArrayList<Item> lst = new ArrayList<>();

            for (Item item : map.get(key)){
                lst.add(item);
                item.setReserve();
                cnt += 1;

                if (cnt == psc){
                    break;
                }
            }
            res.put(key, lst);

            if (cnt == psc){
                break;
            }
        }

        return res;
    }
}
