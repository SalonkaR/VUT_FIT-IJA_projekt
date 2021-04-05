package ija.project;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

import static javafx.scene.paint.Color.*;

public class DropPoint implements Drawable{
    private Coordinates position;
    private double width = 50;
    private Rectangle mainRect;

    private List<Shape> gui = new ArrayList<>();
    private List<Shape> info = new ArrayList<>();
    private ArrayList<Order> waiting = new ArrayList<>();
    private ArrayList<Order> processing = new ArrayList<>();
    private ArrayList<Order> done = new ArrayList<>();


    public DropPoint(Coordinates position) {
        this.position = position;
        mainRect = new Rectangle(position.getX() - width/2, position.getY() - width/2, width, width);
        mainRect.setFill(ORANGE);
        gui.add(mainRect);

        DropPoint point = this;

        mainRect.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent t) {
                if(mainRect.getFill() == ORANGE) {
                    Text text = new Text(point.getContent());
                    text.setWrappingWidth(125);
                    info.add(text);
                    mainRect.setFill(RED);
                }
            }
        });
    }

    public void addOrder(Order order){
        waiting.add(order);
    }

    public void addOrder(ArrayList<Order> lst){
        waiting.addAll(lst);
    }

    private String getContent(){
        String str = "Waiting:\n";
        for (Order i : waiting) {
            str += i.getName() + "\n";
        }

        str += "\nProcessing:\n";
        for (Order i : processing) {
            str += i.getName() + "\n";
        }

        str += "\ndone:\n";
        for (Order i : done) {
            str += i.getName() + "\n";
        }
        return str;
    }

    public void updateOrder(int status, Order order) {
        if (status == 0){
            waiting.remove(order);
            processing.add(order);
        }else{
            processing.remove(order);
            done.add(order);
        }
    }

    public Order getWaitingOrder(){
        return waiting.get(0);
    }

    public int getWaitingOrderSize(){
        return waiting.size();
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
        mainRect.setFill(ORANGE);
    }

    @Override
    public void infoClear() {
        info.clear();
    }


    public Coordinates getPosition() {
        return position;
    }


}