package ija.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.ArrayList;

/**
 *
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class Regal {
    private String name;
    private ArrayList<Shelf> shelves;
    @JsonIgnore
    public Coordinates top;
    @JsonIgnore
    public Coordinates bottom;

    //empty constructor for jackson(yml)
    private Regal() {
    }

    @JsonIgnore
    public Regal(String name, ArrayList<Shelf> shelves) {
        this.name = name;
        this.shelves = shelves;
    }

    public void shareMe(){
        for (Shelf shelf : shelves){
            shelf.setRegal(this);
        }
    }

    public String getName() {
        return name;
    }

    public ArrayList<Shelf> getShelves() {
        return shelves;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShelves(ArrayList<Shelf> shelves) {
        this.shelves = shelves;
    }

    @JsonIgnore
    public void addShelf(Shelf shelf){
        shelves.add(shelf);
    }

    @JsonIgnore
    public void setCoordinates(){
        if (!shelves.isEmpty()){
            double x = shelves.get(0).getPosition().getX();
            double topY = shelves.get(0).getPosition().getY();
            double bottomY = shelves.get(0).getPosition().getY();

            for (Shelf shelf : shelves){
                topY = Math.min(shelf.getPosition().getY(), topY);
                bottomY = Math.max(shelf.getPosition().getY(),bottomY);
            }
            int diff;
            if (shelves.get(0).isAccessPointBool()){ //true = left
                diff = - 10;
            } else {
                diff = 10 + (int)shelves.get(0).getWidth();
            }

            this.top = new Coordinates(x + diff, topY - 10);
            this.bottom = new Coordinates(x + diff, bottomY + 10 + (int)shelves.get(0).getHeight());
        }
    }

    @Override
    public String toString() {
        return "regal:" + name;
    }

    @JsonIgnore
    public Coordinates getTop() {
        if (top == null){
            this.setCoordinates();
        }
        return top;
    }

    @JsonIgnore
    public Coordinates getBottom() {
        if (bottom == null){
            this.setCoordinates();
        }
        return bottom;
    }
}
