package ija.project;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

import static javafx.scene.paint.Color.*;

public class Parking implements Drawable{
    private Coordinates position;
    private double width = 50;
    private Rectangle mainRect;

    private List<Shape> gui = new ArrayList<>();
    private List<Shape> info = new ArrayList<>();
    private ArrayList<Carriage> parked = new ArrayList<>();
    private ArrayList<Carriage> worked = new ArrayList<>();


    public Parking(Coordinates position) {
        this.position = position;
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
}
