package ija.project;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static javafx.scene.paint.Color.*;

public class Shelf implements Drawable {
    private Cordinate position;
    private Cordinate accessPoint;
    private double width = 50;
    private double height = 50;

    private ArrayList<Item> items;
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

    public Item removeItem(Goods good){
        Iterator var2 = this.items.iterator();
        Item item;
        do{
            if (!var2.hasNext()) {
                return null;
            }

            item = (Item) var2.next();
        } while(item.getGoods()!=good);

        this.items.remove(item);
        return item;
    }

    public void addItem(Item item){
        items.add(item);
    }

    @Override
    public List<Shape> getGUI() {
        return gui;
    }
}
