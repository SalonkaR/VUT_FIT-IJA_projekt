package ija.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * Trieda reprezentujuca konkretny Item(kus, vyrobok).
 *
 * @author Matúš Tvarožný - xtvaro00
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
     * @param name Nazov itemu
     * @param goods Goods itemu
     * @param shelf Polica itemu v ktorej sa nachadza
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
     * Funkcia vyzdiela niektore z atributov itemu, pre vzajomne prepojenie s inymi triedami
     */
    public void shareMe(){
        this.goods.addItem(this);
        this.shelf.addItem(this);
    }

    /**
     * Funkcia vracia nazov itemu
     * @return Nazov itemu
     */
    public String getName() {
        return name;
    }

    /**
     * Funkcia vracia goods(druh) itemu
     * @return Goods pod ktory item patri
     */
    public Goods getGoods() {
        return goods;
    }

    /**
     * Funkcia vracia policu v ktorej sa item nachadza
     * @return Polica v ktoej sa nachadza item
     */
    public Shelf getShelf() {
        return shelf;
    }

    /**
     * Funkcia nastavuje nazov itemu
     * @param name Nazov itemu
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Funkcia nastavuje goods(druh) itemu
     * @param goods Goods pod ktory item patri
     */
    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    /**
     * Funkcia nastavuje policu v ktorej sa item nachadza
     * @param shelf Polica v ktorej sa nachaza item
     */
    public void setShelf(Shelf shelf) {
        this.shelf = shelf;
    }

    /**
     * Funkcia reprezentujuca predaj itemu, po jej zavolani sa item odstrani z jeho goodsov
     */
    @JsonIgnore
    public void sell(){
        this.goods.removeItem(this);
    }

    /**
     * Funkcia rezervuje item
     */
    public void setReserve() {
        reserve = true;
    }

    /**
     * Funkcia vrati vahu itemu
     * @return Vaha itemu (double)
     */
    public double getWeight() {
        return goods.getWeight();
    }


}
