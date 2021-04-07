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
    private Map<Shelf, List<Item>> freeItems = new HashMap<>();
    @JsonIgnore
    // key = X+Y postion shelf
    private Map<Double, List<Shelf>> sortedShelves = new HashMap<>();
    @JsonIgnore
    private ArrayList<Item> reservedItems = new ArrayList<>();

    //empty constructor for jackson(yml)
    public Goods() {
    }

    public Goods(String name) {
        this.name = name;
    }

    public void addItem(Item item) {
        Shelf shelf = item.getShelf();
        if(freeItems.containsKey(shelf)){
            freeItems.get(shelf).add(item);
        } else  {
            List<Item> lst = new ArrayList<>();
            lst.add(item);
            freeItems.put(shelf, lst);
            this.addShelf(shelf);
        }
    }

    public void addShelf(Shelf shelf){
        double xy = shelf.getAccessPoint().getX() + shelf.getAccessPoint().getY();
        if (sortedShelves.containsKey(xy)){
            sortedShelves.get(xy).add(shelf);
        } else{
            List<Shelf> lst = new ArrayList<>();
            lst.add(shelf);
            sortedShelves.put(xy, lst);
        }
    }


    public String getName() {
        return name;
    }

    public void removeItem(Item item) {
        reservedItems.remove(item);
    }

    public void reserveItem(List<Item> lst){
        for (Item item : lst) {
            this.reserveItem(item);
        }
    }

    public void reserveItem(Item item){
        Shelf shelf = item.getShelf();
        reservedItems.add(item);
        freeItems.get(shelf).remove(item);

        if (freeItems.get(shelf).size() == 0){
            freeItems.remove(shelf);
            this.removeShelf(shelf);
        }
    }
    public void removeShelf(Shelf shelf){
        double xy = shelf.getAccessPoint().getX() + shelf.getAccessPoint().getY();
        sortedShelves.get(xy).remove(shelf);
        if (sortedShelves.get(xy).size() == 0){
            sortedShelves.remove(xy);
        }
    }

    public int sizeFree(){
        int cnt = 0;
        for (Shelf shelf : freeItems.keySet()){
            cnt += freeItems.get(shelf).size();
        }

        return cnt;
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

    public List<Item> getItems(int pcs){
        int cnt = 0;
        List<Item> items= new ArrayList<>();
        List<Double> lst = new ArrayList<>();
        lst.addAll(sortedShelves.keySet());
        Collections.sort(lst);

        for (Double x : lst){
            for (Shelf shelf : sortedShelves.get(x)){
                for (Item item : freeItems.get(shelf)){
                    items.add(item);
                    cnt += 1;
                    if (cnt == pcs){
                        break;
                    }
                }
                if (cnt == pcs){
                    break;
                }
            }
            if (cnt == pcs){
                break;
            }
        }
        this.reserveItem(items);

        return items;

    }


    public HashMap<Double, List<Item>> getItemsMap(Integer psc) {
        HashMap<Double, List<Item>> map = new HashMap<>();
        Double x;
        for (Item item : this.getItems(psc)){
            x = item.getShelf().getAccessPoint().getY();
            if (map.containsKey(x)){
                map.get(x).add(item);
            } else {
                List<Item> lst= new ArrayList<>();
                lst.add(item);
                map.put(x, lst);
            }
        }

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
