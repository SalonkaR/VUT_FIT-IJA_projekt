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

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class Parking implements Drawable{
    private String name;
    private DropPoint dropPoint;
    private Coordinates position;
    @JsonIgnore
    private double width = 50;
    @JsonIgnore
    private Rectangle mainRect;
    @JsonIgnore
    private List<Shape> gui;
    @JsonIgnore
    private List<Shape> info = new ArrayList<>();
    @JsonIgnore
    private ArrayList<Carriage> parked = new ArrayList<>();
    @JsonIgnore
    private ArrayList<Carriage> worked = new ArrayList<>();

    //empty constructor for jackson(yml)
    public Parking() {
    }

    public String getName() {
        return name;
    }

    public DropPoint getDropPoint() {
        return dropPoint;
    }

    public Coordinates getPosition() {
        return position;
    }

    public Parking(String name, DropPoint point, Coordinates position) {
        this.name = name;
        this.dropPoint = point;
        this.position = position;
        this.makeGui();
    }

    public void makeGui(){
        gui = new ArrayList<>();
        mainRect = new Rectangle(position.getX() - width/2, position.getY() - width/2, width, width);
        mainRect.setFill(ORANGE);
        gui.add(mainRect);

        Parking parking = this;

        mainRect.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent t) {
                if(mainRect.getFill() == ORANGE) {
                    Text text = new Text(parking.getContent());
                    text.setWrappingWidth(125);
                    info.add(text);
                    mainRect.setFill(RED);
                }
            }
        });
    }

    public void addCarriage(Carriage carriage){
        parked.add(carriage);
    }

    public void addCarriage(ArrayList<Carriage> lst){
        parked.addAll(lst);
    }

    public Order getOrder(){
        if (dropPoint.getWaitingOrderSize() == 0){
            return null;
        }
        return dropPoint.getWaitingOrder();
    }

    private String getContent(){
        String str = "Parked:\n";
        for (Carriage i : parked) {
            str += i.getName() + "\n";
        }
        str += "\nWorked:\n";
        for (Carriage i : worked) {
            str += i.getName() + "\n";
        }
        return str;
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

    @Override
    public String toString() {
        return "Parking{" +
                "position=" + position +
                ", width=" + width +
                ", mainRect=" + mainRect +
                ", gui=" + gui +
                ", info=" + info +
                ", parked=" + parked +
                ", worked=" + worked +
                '}';
    }


    public Coordinates getDropPointCoords(){
        return dropPoint.getPosition();
    }

}
