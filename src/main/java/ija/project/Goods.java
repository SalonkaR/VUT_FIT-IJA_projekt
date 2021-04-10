package ija.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.*;

/**
 * Trieda reprezentuje tovar ako taky(druh), nie konkretny vyrobok.
 * Pod jej zastitom su uz konkretne itemy.
 *
 * @author Jakub Sokolik - xsokol14
 */

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

    /**
     * Prazdny konstruktor, ktory sluzi pre deserializaciu yml
     */
    public Goods(){
    }

    /**
     * Konstruktor ktory tovaru priradi jeho nazov
     * @param name Nazov tovaru
     */
    public Goods(String name) {
        this.name = name;
    }

    /**
     * Funkcia, ktora po stlaceni tlacidla reset nastavi goods do vychodzieho stavu
     */
    public void reset(){
        freeItems = new HashMap<>();
        sortedShelves = new HashMap<>();
        reservedItems = new ArrayList<>();
    }

    /**
     * Funkcia prida item do police
     * @param item Item na pridanie
     */
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

    /**
     * Funkcia prida policu do zoznamu polic, ktore dany goods obsahuju
     * @param shelf Polica na pridanie
     */
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

    /**
     * Funkcia vracia nazov druhu tovaru
     * @return Nazov tovaru
     */
    public String getName() {
        return name;
    }

    /**
     * Funkcia odstrani item zo zonamu rezervovanych itemov
     * @param item Item na odstranenie
     */
    public void removeItem(Item item) {
        reservedItems.remove(item);
    }

    /**
     * Funkcia prida itemy zo zoznamu itemov medzi rezervovane itemy aby sa nestalo, ze pre jeden item pojdu 2 voziky naraz
     * @param lst Zoznam itemov, ktore budu rezervovane
     */
    public void reserveItem(List<Item> lst){
        for (Item item : lst) {
            this.reserveItem(item);
        }
    }

    /**
     * Funkcia rezervuje item a odstrani ho zo zonamu dostupnych itemov
     * @param item Item na rezervaciu
     */
    public void reserveItem(Item item){
        Shelf shelf = item.getShelf();
        reservedItems.add(item);
        freeItems.get(shelf).remove(item);

        if (freeItems.get(shelf).size() == 0){
            freeItems.remove(shelf);
            this.removeShelf(shelf);
        }
    }

    /**
     * AK sa z police minu vsetky itemy daneho druhu(goods), tak je odstranena zo zoznamu
     * @param shelf
     */
    public void removeShelf(Shelf shelf){
        double xy = shelf.getAccessPoint().getX() + shelf.getAccessPoint().getY();
        sortedShelves.get(xy).remove(shelf);
        if (sortedShelves.get(xy).size() == 0){
            sortedShelves.remove(xy);
        }
    }

    /**
     * Funkcia vracia celkovy pocet dostupnych itemov tohto tovaru z celeho skladu
     * @return Pocet volnych itemov
     */
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

    /**
     * Metóda vráti niekoľko itemov, ktoré su najbližšiek parkovisku.
     *
     * @param pcs počet itemov, ktoré sa vrátia.
     * @return zoznam itemov.
     */
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

    /**
     * Funkcia vracia vahu tovaru
     * @return Vaha tovaru
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Funkcia nastavuje vahu tovaru
     * @param weight Vaha tovaru
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }
}
