package ija.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Shelf implements Drawable {
    private String id;
    private Coordinates position;
    @JsonIgnore
    private Coordinates accessPoint;
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
    @JsonIgnore
    private Regal regal;


    //empty constructor for jackson(yml)
    private Shelf(){
    }

    @JsonIgnore
    public Shelf(String id, Coordinates position, double width, double height) {
        this.id = id;
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

    public Coordinates getPosition() {
        return position;
    }

    public String getId() {
        return id;
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

    public List<Shape> getInfo() {
        return info;
    }

    @Override
    public List<Shape> getGUI() {
        if(gui == null){
            this.makeGui();
        }
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
        String str = "";
        for (Goods i : items.keySet()) {
            str += i.getName() + "-" + items.get(i).size() + "ks\n" ;
        }
        return str;
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

    public void saveBlock(Regal regal){
        this.regal = regal;
    }
}
