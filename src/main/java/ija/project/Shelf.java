package ija.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
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
    private Cordinate position;
    private Cordinate accessPoint;
    @JsonIgnore
    private double width = 50;
    @JsonIgnore
    private double height = 50;
    @JsonIgnore
    private List<Shape> gui;
    @JsonIgnore
    private Map<Goods, List<Item>> items = new HashMap();


    //empty constructor for jackson(yml)
    private Shelf(){
    }

    @JsonIgnore
    public Shelf(Cordinate position, double width, double height) {
        this.position = position;
        this.width = width;
        this.height = height;
        this.makeGui();
    }

    @JsonIgnore
    public void makeGui(){
        gui = new ArrayList<>();
        Rectangle rect = new Rectangle(position.getX(), position.getY(), width, height);
        Text text = new Text(position.getX(), position.getY(), "aaa");
        rect.setFill(SKYBLUE);
        gui.add(rect);
        rect.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent t) {
                if(rect.getFill() == SKYBLUE){
                    rect.setFill(RED);
                } else if(rect.getFill() == RED){
                    rect.setFill(SKYBLUE);
                }
            }
        });
    }

    public Cordinate getPosition() {
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
        }else {
            List<Item> lst = new ArrayList();
            lst.add(item);
            this.items.put(goods, lst);
        }
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
}
