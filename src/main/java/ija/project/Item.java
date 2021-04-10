package ija.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * Trieda reprezentujuca konkretny Item(kus, vyrobok).
 *
 * @author
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class Item {
    private String name;
    private Goods goods;
    private Shelf shelf;
    private boolean reserve = false;

    /**
     * Prazdny konstruktor, ktory sluzi pre deserializaciu yml
     */
    public Item(){
    }

    /**
     * Konstruktor, ktory itemu priradi nazov, goods a policu v ktorej sa nachadza
     * @param name
     * @param goods
     * @param shelf
     */
    @JsonIgnore
    public Item(String name, Goods goods, Shelf shelf) {
        this.name = name;
        this.goods = goods;
        this.shelf = shelf;
        this.goods.addItem(this);
        this.shelf.addItem(this);
    }

    /**
     * Funkcia vyzdiela niektore z atributov itemu
     */
    public void shareMe(){
        this.goods.addItem(this);
        this.shelf.addItem(this);
    }

    public String getName() {
        return name;
    }

    public Goods getGoods() {
        return goods;
    }

    public Shelf getShelf() {
        return shelf;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public void setShelf(Shelf shelf) {
        this.shelf = shelf;
    }

    @JsonIgnore
    public void sell(){
        this.goods.removeItem(this);
    }

    public void setReserve() {
        reserve = true;
    }

    public double getWeight() {
        return goods.getWeight();
    }


}
