package ija.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.List;
import java.util.Random;

/**
 * Trieda ItemGenerator generuje itemy pred spustením programu.
 *
 * @author Matúš Tvarožný - xtvaro00
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class ItemGenerator {
    public String name;
    public Goods goods;
    public int count;
    @JsonIgnore
    public List<Shelf> shelves;

    /**
     * Prazdný konštruktor, ktorý slúži pre deserializáciu yml.
     */
    public ItemGenerator() {
    }

    /**
     * Konstruktor, ktory generatoru priradi nazov, goodsy, ktore bude generovat a ich pocet.
     * @param name
     * @param goods
     * @param count
     */
    @JsonIgnore
    public ItemGenerator(String name, Goods goods, int count) {
        this.name = name;
        this.goods = goods;
        this.count = count;
    }

    /**
     * Funkcia vracia nazov generatoru.
     * @return Nazov generatoru
     */
    public String getName() {
        return name;
    }

    /**
     * Funkcia vracia goods, ktore generator generuje.
     * @return Goods generatora
     */
    public Goods getGoods() {
        return goods;
    }

    /**
     * Funkcia vracia zadany pocet goodsov, ktore ma generator vygenerovat.
     * @return Pocet goods
     */
    public int getCount() {
        return count;
    }

    /**
     * Funkcia naplni zoznam polic do ktorych sa budu nahodne rozhadzovat itemy.
     * @param shelves Zoznam polic
     */
    @JsonIgnore
    public void setShelves(List<Shelf> shelves) {
        this.shelves = shelves;
    }

    /**
     * Funkcia sa stara o nahodne rozmiestnenie itemov po sklade.
     */
    @JsonIgnore
    public void createIt(){
        Random random = new Random();
        int i;
        for (int cnt = 0; cnt < count; cnt++){
            i = random.nextInt(shelves.size());
            Item item = new Item(goods.getName() + cnt, goods, shelves.get(i));
        }
    }
}
