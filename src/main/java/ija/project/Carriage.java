package ija.project;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static javafx.scene.paint.Color.GREEN;

public class Carriage implements Drawable, Mover {
    private String name;
    private Coordinates position;
    private int speed = 2;
    private int status;
    private Circle mainCircle;
    private Parking parking;
    private Order order;

    private List<Shape> gui = new ArrayList<>();
    private List<Shape> info = new ArrayList<>();
    private HashMap<Goods, Integer> need;

    private HashMap<Shelf, List<Item>> listItems2 = new HashMap<>();;

    public Carriage(String name, Parking parking, Coordinates position) {
        this.name = name;
        this.parking = parking;
        this.position = position;
        this.status = 0;
        mainCircle = new Circle(position.getX(), position.getY(), 6, GREEN);
        gui.add(mainCircle);


    }

    public String getName() {
        return name;
    }

    public void calculateRoad(){
        need = order.getList();
        HashMap<Double, List<Item>> listItems = new HashMap<>();
        HashMap<Double, List<Item>> newListItems = new HashMap<>();

        for (Goods goods : need.keySet()){
            newListItems = goods.getItems(need.get(goods));

            for (double key : newListItems.keySet()){
                if (listItems.containsKey(key)){
                    for (Item item : newListItems.get(key)){
                        listItems.get(key).add(item);
                    }
                } else {
                    listItems.put(key, newListItems.get(key));
                }
            }
        }


        for (double key : listItems.keySet()){
            for (Item item : listItems.get(key)){
                if (listItems2.containsKey(item.getShelf())){
                    listItems2.get(item.getShelf()).add(item);
                } else {
                    List<Item> lst = new ArrayList<>();
                    lst.add(item);
                    listItems2.put(item.getShelf(), lst);
                }
            }
        }
        System.out.println("roztriedil som");
        this.print();

    }

    public void print(){
        System.out.println(listItems2);
    }

    @Override
    public List<Shape> getGUI() {
        return gui;
    }

    @Override
    public List<Shape> getInfo() {
        return info;
    }

    @Override
    public void off() {

    }

    @Override
    public void infoClear() {

    }

    @Override
    public void update() {
        switch (status){
            case 0: //waiting for order
                order = parking.getOrder();
                if (order != null) {
                    status += 1; // Go to processing order
                    order.update();
                    this.calculateRoad();
                }
                break;
            case 1:
                //processing order
                break;
        }
        mainCircle.setTranslateY(mainCircle.getTranslateY() + speed);
    }

    @Override
    public String toString() {
        return "Carriage{" +
                "name='" + name + '\'' +
                ", position=" + position +
                ", speed=" + speed +
                ", status=" + status +
                ", mainCircle=" + mainCircle +
                ", gui=" + gui +
                ", info=" + info +
                '}';
    }
}
