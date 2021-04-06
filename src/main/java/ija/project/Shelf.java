package ija.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javafx.event.EventHandler;

import javafx.scene.shape.Circle;
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
    private boolean accessPointBool;  //true = left, false = right
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
    private Circle accessCircle;
    @JsonIgnore
    private Regal regal;


    //empty constructor for jackson(yml)
    private Shelf(){
    }

    @JsonIgnore
    public Shelf(String id, Coordinates position, boolean accessPointBool, Coordinates accessPoint, double width, double height) {
        this.id = id;
        this.position = position;
        this.width = width;
        this.height = height;
        this.accessPointBool = accessPointBool;
        this.accessPoint = accessPoint;
        this.makeAccessPoint();
        this.makeGui();
    }

    @JsonIgnore
    public void makeGui(){
        gui = new ArrayList<>();
        mainRect = new Rectangle(position.getX(), position.getY(), width, height);
        mainRect.setFill(SKYBLUE);
        gui.add(mainRect);

        this.makeAccessPoint();
        accessCircle = new Circle(accessPoint.getX(), accessPoint.getY(),3, LIMEGREEN);
        gui.add(accessCircle);

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


    public void makeAccessPoint() {
        double y = this.getPosition().getY() + height/2;
        double x;
        if (accessPointBool){
            //accessPoint = Left
           x = this.getPosition().getX() - 10;
        } else {
            //accessPoint = Right
            x = this.getPosition().getX() + width + 10;
        }
        accessPoint = new Coordinates(x,y);
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
    public void removeItem(Item item){
        List<Item> lst = (List)this.items.get(item.getGoods());

        lst.remove(item);

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
    public List<Shape> getInfo() {
        return info;
    }

    @Override
    public List<Shape> updateInfo() {
        Text text = new Text(this.getContent());
        text.setWrappingWidth(125);
        info.add(text);
        return info;
    }

    @Override
    public List<Shape> getGUI() {
        if(accessPoint == null){
            this.makeAccessPoint();
        }
        if(gui == null){
            this.makeGui();
        }
        return gui;
    }

    @Override
    public String toString() {
        return "Shelf:" + position;
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

    public Regal getRegal() {
        return regal;
    }
    @JsonIgnore
    public void setRegal(Regal regal) {
        this.regal = regal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Shelf)) return false;

        Shelf shelf = (Shelf) o;

        return id.equals(shelf.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public boolean isAccessPointBool() {
        return accessPointBool;
    }

    public Coordinates getAccessPoint() {
        if (accessPoint == null){
            this.makeAccessPoint();
        }
        return accessPoint;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
