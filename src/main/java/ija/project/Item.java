package ija.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class Item {
    private String name;
    private Goods goods;
    private Shelf shelf;
    private boolean reserve = false;

    //empty constructor for jackson(yml)
    public Item() {
    }

    @JsonIgnore
    public Item(String name, Goods goods, Shelf shelf) {
        this.name = name;
        this.goods = goods;
        this.shelf = shelf;
        this.goods.addItem(this);
        this.shelf.addItem(this);
    }

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
        //List<Goods> goods = data.getGoods();
        //for (Goods good : goods){
        //    if(this.goods.getName() == good.getName()){
        //        this.goods = good;
        //   }
        //}
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

    @Override
    public String toString() {
        return "Item:"  + name ;
    }

    public boolean isReserve() {
        return reserve;
    }
}
