package ija.project;

import java.util.ArrayList;
import java.util.List;

/**
 * Trieda uchovava informacie o objektoch nacitavanych zo suboru YML
 *
 * @author Matus Tvarozny - xtvaro00
 */
public class Data {
    private List<Shelf> shelves;
    private List<Regal> regals;
    private List<Goods> goods;
    private List<Item> items;
    private List<DropPoint> dropPoint;
    private List<Parking> parking;
    private List<Carriage> carriages;
    private List<ItemGenerator> itemGenerators;
    private List<OrderGenerator> orderGenerators;
    private List<OrderGenerator2> orderGenerators2;

    /**
     * Konstruktor vytvarajuci zoznamy potrebnych objektov
     */
    public Data(){
        shelves = new ArrayList<>();
        regals = new ArrayList<>();
        goods = new ArrayList<>();
        items = new ArrayList<>();
        dropPoint = new ArrayList<>();
        parking = new ArrayList<>();
        carriages = new ArrayList<>();
        itemGenerators = new ArrayList<>();
        orderGenerators = new ArrayList<>();
        orderGenerators2 = new ArrayList<>();
    }

    /**
     * Funkcia vracia zoznam obsahujuci police nacitane z YML
     * @return Zoznam polic
     */
    public List<Shelf> getShelves(){
        return shelves;
    }

    /**
     * Funkcia vracia zoznam obsahujuci regaly nacitane z YML
     * @return Zoznam regalov
     */
    public List<Regal> getRegals() {
        return regals;
    }

    /**
     * Funkcia vracia zoznam obsahujuci vsetky goods nacitanych z YML, ktore sa budu v sklade nachadzat
     * @return Zoznam goods
     */
    public List<Goods> getGoods() {
        return goods;
    }

    /**
     * Funkcia vracia zoznam obsahujuci itemy nacitane z YML
     * @return Zoznam vsetkych itemov
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * Funkcia vracia zoznam obsahujuci drop pointy nacitane z YML
     * @return Zoznam drop pointov
     */
    public List<DropPoint> getDropPoint() {
        return dropPoint;
    }

    /**
     * Funkcia vracia zoznam obsahujuci parkoviska nacitane z YML
     * @return Zoznam parkovisk
     */
    public List<Parking> getParking() {
        return parking;
    }

    /**
     * Funkcia vracia zoznam obsahujuci voziky nacitane z YML so vsetkymi ich potrebnymi atributmi
     * @return Zoznam vozikov
     */
    public List<Carriage> getCarriages() {
        return carriages;
    }

    /**
     * Funkcia vracia zoznam obsahujuci generatory itemov nacitane z YML
     * @return Zoznam generatorov itemov
     */
    public List<ItemGenerator> getItemGenerators() {
        return itemGenerators;
    }

    /**
     * Funkcia vracia zoznam obsahujuci generatory objednavok nacitane z YML
     * @return Zoznam generatorov objednavok
     */
    public List<OrderGenerator> getOrderGenerators() {
        return orderGenerators;
    }

    /**
     * Funkcia vracia zoznam obsahujuci generatory objednavok nacitane z YML
     * @return Zoznam generatorov objednavok
     */
    public List<OrderGenerator2> getOrderGenerators2() {
        return orderGenerators2;
    }

    /**
     * Funkcia naplni zoznam polic
     * @param shelves zoznam polic
     */
    public void setShelves(List<Shelf> shelves){
        this.shelves = shelves;
    }

    /**
     * Funkcia naplni zoznam regalov
     * @param regals zoznam regalov
     */
    public void setRegals(List<Regal> regals) {
        this.regals = regals;
    }

    /**
     * Funkcia naplni zoznam goodsov
     * @param goods zoznam goodsov
     */
    public void setGoods(List<Goods> goods) {
        this.goods = goods;
    }

    /**
     * Funkcia naplni zoznam itemov
     * @param items zoznam itemov
     */
    public void setItems(List<Item> items) {
        this.items = items;
    }

    /**
     * Funkcia naplni zoznam drop pointov
     * @param dropPoint zoznam drop pointov
     */
    public void setDropPoint(List<DropPoint> dropPoint) {
        this.dropPoint = dropPoint;
    }

    /**
     * Funkcia naplni zoznam parkovisiek
     * @param parking zoznam parkovisiek
     */
    public void setParking(List<Parking> parking) {
        this.parking = parking;
    }

    /**
     * Funkcia naplni zoznam vozikov
     * @param carriages zoznam vozikov
     */
    public void setCarriages(List<Carriage> carriages) {
        this.carriages = carriages;
    }

    /**
     * Funkcia naplni zoznam generatorov itemov
     * @param itemGenerators zoznam generatorov itemov
     */
    public void setItemGenerators(List<ItemGenerator> itemGenerators) {
        this.itemGenerators = itemGenerators;
    }

    /**
     * Funkcia naplni zoznam generatorov objednavok
     * @param orderGenerators zoznam generatorov objednavok
     */
    public void setOrderGenerators(List<OrderGenerator> orderGenerators) {
        this.orderGenerators = orderGenerators;
    }

    /**
     * Funkcia naplni zoznam generatorov objednavok
     * @param orderGenerators2 zoznam generatorov objednavok
     */
    public void setOrderGenerators2(List<OrderGenerator2> orderGenerators2) {
        this.orderGenerators2 = orderGenerators2;
    }
}
