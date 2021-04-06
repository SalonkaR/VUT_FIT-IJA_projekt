package ija.project;

import java.util.ArrayList;
import java.util.List;

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

    public List<DropPoint> getDropPoint() {
        return dropPoint;
    }

    public List<Parking> getParking() {
        return parking;
    }

    public List<Carriage> getCarriages() {
        return carriages;
    }

    public List<ItemGenerator> getItemGenerators() {
        return itemGenerators;
    }

    public List<OrderGenerator> getOrderGenerators() {
        return orderGenerators;
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

    public void setDropPoint(List<DropPoint> dropPoint) {
        this.dropPoint = dropPoint;
    }

    public void setParking(List<Parking> parking) {
        this.parking = parking;
    }

    public void setCarriages(List<Carriage> carriages) {
        this.carriages = carriages;
    }

    public void setItemGenerators(List<ItemGenerator> itemGenerators) {
        this.itemGenerators = itemGenerators;
    }

    public void setOrderGenerators(List<OrderGenerator> orderGenerators) {
        this.orderGenerators = orderGenerators;
    }
}
