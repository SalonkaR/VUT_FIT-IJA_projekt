package ija.project;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import javafx.scene.input.MouseEvent;

import java.util.*;

import static javafx.scene.paint.Color.*;

public class Shelf implements Drawable {
    private Cordinate position;
    private Cordinate accessPoint;
    private double width = 50;
    private double height = 50;

    private Map<Goods, List<Item>> items = new HashMap();
    private List<Shape> gui;

    public Shelf(Cordinate position, double width, double height) {
        this.position = position;
        this.width = width;
        this.height = height;
        gui = new ArrayList<>();
        Rectangle rect = new Rectangle(position.getX(), position.getY(), width, height);
        rect.setFill(SKYBLUE);
        gui.add(rect);

        rect.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (rect.getFill() == SKYBLUE) {
                    rect.setFill(RED);
                } else if (rect.getFill() == RED) {
                    rect.setFill(SKYBLUE);
                }
            }
        });
    }

    public Item removeItem(Goods goods){
        List<Item> lst = (List)this.items.get(goods);
        if (lst == null) {
            return null;
        } else {
            return lst.isEmpty() ? null : (Item)lst.remove(0);
        }
    }

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
}
