package ija.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

import static javafx.scene.paint.Color.*;

/**
 *
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class DropPoint implements Drawable{
    private String name;
    private Coordinates position;
    @JsonIgnore
    private double width = 30;
    @JsonIgnore
    private Rectangle mainRect;
    @JsonIgnore
    private List<Shape> gui;
    @JsonIgnore
    private List<Shape> info = new ArrayList<>();
    @JsonIgnore
    private ArrayList<Order> waiting = new ArrayList<>();
    @JsonIgnore
    private ArrayList<Order> processing = new ArrayList<>();
    @JsonIgnore
    private ArrayList<Order> done = new ArrayList<>();
    @JsonIgnore
    private ArrayList<Order> capacityless = new ArrayList<>();
    private int maxToSee = 5;

    //empty constructor for jackson(yml)
    public DropPoint() {
    }

    public String getName() {
        return name;
    }

    public Coordinates getPosition() {
        return position;
    }

    public void reset(){
        waiting =  new ArrayList<>();
        done = new ArrayList<>();
        processing = new ArrayList<>();
        capacityless = new ArrayList<>();
        this.makeGui();
    }



    @JsonIgnore
    public DropPoint(String name, Coordinates position) {
        this.name = name;
        this.position = position;
        this.makeGui();
    }

    @JsonIgnore
    public void makeGui(){
        gui = new ArrayList<>();
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

    @JsonIgnore
    public void addOrder(Order order){
        waiting.add(order);
    }

    @JsonIgnore
    public void addOrder(ArrayList<Order> lst){
        waiting.addAll(lst);
    }

    @JsonIgnore
    private String getContent(){
        String str = "Waiting:\n";
        int cnt = 0;
        for (Order i : waiting) {
            str += i.getName() + "\n";
            if (cnt == maxToSee){
                str += "...";
                break;
            }
            cnt +=1;
        }

        str += "\nProcessing:\n";
        for (Order i : processing) {
            str += i.getName() + "\n";
            if (cnt == 2*maxToSee){
                str += "...";
                break;
            }
            cnt +=1;
        }

        str += "\nDone:\n";
        for (Order i : done) {
            str += i.getName() + "\n";
            if (cnt == 3*maxToSee){
                str += "...";
                break;
            }
            cnt +=1;
        }

        str += "\nOut of storage capacity:\n";
        for (Order i : capacityless) {
            str += i.getName() + "\n";
            if (cnt == 4*maxToSee){
                str += "...";
                break;
            }
            cnt +=1;
        }
        return str;
    }

    @JsonIgnore
    public void updateOrder(int status, Order order) {
        if (status == 0){
            waiting.remove(order);
            processing.add(0, order);
        }else{
            processing.remove(order);
            done.add(0, order);
            if (done.size() > 15){
                done.remove(done.size() - 1);
            }
        }
    }

    @JsonIgnore
    public Order getWaitingOrder(){
        Order order = waiting.get(0);
        if (order.checkCapacity()) {
            return order;
        } else {
            waiting.remove(0);
            capacityless.add(0, order);
            if (capacityless.size() > 15){
                capacityless.remove(capacityless.size() - 1);
            }
            return null;
        }
    }

    @JsonIgnore
    public int getWaitingOrderSize(){
        return waiting.size();
    }

    @Override
    public List<Shape> getGUI() {
        if(gui == null){
            this.makeGui();
        }
        return gui;
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
    public void off() {
        mainRect.setFill(ORANGE);
    }

    @Override
    public void infoClear() {
        info.clear();
    }

}