package ija.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.event.EventHandler;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import static javafx.scene.paint.Color.*;

public class Shelf implements Drawable {
    private Coordinate position;
    private Coordinate accessPoint;
    @JsonIgnore
    private double width = 50;
    @JsonIgnore
    private double height = 50;
    @JsonIgnore
    private List<Shape> gui;
    @JsonIgnore
    private Map<Goods, List<Item>> items = new HashMap();
    @JsonIgnore
    private List<Shape> info = new ArrayList<>();
    @JsonIgnore
    private Rectangle mainRect;


    //empty constructor for jackson(yml)
    private Shelf(){
    }

    @JsonIgnore
    public Shelf(Coordinate position, double width, double height) {
        this.position = position;
        this.width = width;
        this.height = height;
        this.makeGui();
    }

    @JsonIgnore
    public void makeGui(){
        gui = new ArrayList<>();
        mainRect = new Rectangle(position.getX(), position.getY(), width, height);
        mainRect.setFill(SKYBLUE);
        gui.add(mainRect);
        Shelf shelf = this;
        mainRect.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent t) {
                if(mainRect.getFill() == SKYBLUE) {
                    Text text = new Text(shelf.getContent());
                    text.setWrappingWidth(125);
                    info.add(text);
                    mainRect.setFill(RED);
                }
            }
        });
    }

    public Coordinate getPosition() {
        return position;
    }

    @JsonIgnore
    public Item removeItem(Goods goods){
        List<Item> lst = (List)this.items.get(goods);
        if (lst == null) {
            return null;
        } else {
            return lst.isEmpty() ? null : (Item)lst.remove(0);
        }
    }

    @JsonIgnore
    public void addItem(Item item){
        Goods goods = item.getGoods();
        if(this.items.containsKey(goods)){
            ((List)this.items.get(goods)).add(item);
            System.out.println("new item added in map to already existing goods");
        }else {
            System.out.println("new goods added in map");
            List<Item> lst = new ArrayList();
            lst.add(item);
            this.items.put(goods, lst);
        }
    }

    public List<Shape> getInfo() {
        return info;
    }

    @Override
    public List<Shape> getGUI() {
        return gui;
    }

    @Override
    public String toString() {
        return "Shelf{" +
                "position=" + position +
                ", width=" + width +
                ", height=" + height +
                ", gui=" + gui +
                '}';
    }

    private String getContent(){
        return this.toString();
    }

    public void infoClear(){
        info.clear();
    }

    public void off(){
        mainRect.setFill(SKYBLUE);
    }

    public void print(){
        for (Goods i : items.keySet()) {
            System.out.println("key: " + i.getName() + " value: " + items.get(i).size());
        }
        System.out.println(items);
    }
}
