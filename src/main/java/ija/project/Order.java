package ija.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name") //, scope = Order.class
public class Order {
    private String name;
    //@JsonProperty("goods")
    private List<Goods> goods;
    @JsonIgnore
    private HashMap<Goods, Integer> list;
    @JsonIgnore
    private int status = 0;
    // 0- waiting
    // 1- processig
    // 2 -done
    @JsonIgnore
    private DropPoint dropPoint;

    //empty constructor for json(yml)
    private Order() {
    }

    public String getName() {
        return name;
    }

    public List<Goods> getGoods() {
        return goods;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGoods(List<Goods> goods) {
        this.goods = goods;
    }

    @JsonIgnore
    public Order(String name, List<Goods> goods, DropPoint point, HashMap<Goods, Integer> list) {
        this.name = name;
        this.goods = goods;
        this.dropPoint = point;
        this.list = list;
        dropPoint.addOrder(this);
    }

    @JsonIgnore
    public void update(){
        if (status < 2){
            dropPoint.updateOrder(status, this);
            status += 1;
        }
    }

    @JsonIgnore
    public int getStatus() {
        return status;
    }

    @JsonIgnore
    public HashMap<Goods, Integer> getList() {
        return list;
    }
}
