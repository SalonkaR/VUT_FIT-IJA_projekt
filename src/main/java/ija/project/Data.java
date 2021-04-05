package ija.project;

import java.util.ArrayList;
import java.util.List;

public class Data {
    private List<Shelf> shelves;
    private List<Regal> regals;
    private List<Goods> goods;
    private List<Item> items;
    private List<Goods> orders;
    private List<DropPoint> dropPoint;
    private List<Parking> parking;


    public Data(){
        shelves = new ArrayList<>();
        regals = new ArrayList<>();
        goods = new ArrayList<>();
        items = new ArrayList<>();
        orders = new ArrayList<>();
        dropPoint = new ArrayList<>();
        parking = new ArrayList<>();
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

    public List<Goods> getOrders() {
        return orders;
    }

    public List<DropPoint> getDropPoint() {
        return dropPoint;
    }

    public List<Parking> getParking() {
        return parking;
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

    public void setOrders(List<Goods> orders) {
        this.orders = orders;
    }

    public void setDropPoint(List<DropPoint> dropPoint) {
        this.dropPoint = dropPoint;
    }

    public void setParking(List<Parking> parking) {
        this.parking = parking;
    }

    @Override
    public String toString() {
        return "Data{" +
                "shelves=" + shelves +
                ", regals=" + regals +
                '}';
    }
}
