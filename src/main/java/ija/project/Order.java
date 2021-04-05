package ija.project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Order {
    private String name;
    private HashMap<Goods, Integer> list;
    private int status = 0;
    private DropPoint dropPoint;

    public Order(String name, DropPoint point, HashMap<Goods, Integer> list) {
        this.name = name;
        this.dropPoint = point;
        this.list = list;
        dropPoint.addOrder(this);
    }

    public String getName() {
        return name;
    }

    public void update(){
        if (status < 2){
            dropPoint.updateOrder(status, this);
            status += 1;
        }
    }

    public int getStatus() {
        return status;
    }

    public HashMap<Goods, Integer> getList() {
        return list;
    }
}
