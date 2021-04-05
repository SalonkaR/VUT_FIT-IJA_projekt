package ija.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.ArrayList;

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
            double y = shelves.get(0).getPosition().getY();
            double topX = shelves.get(0).getPosition().getX();
            double bottomX = shelves.get(0).getPosition().getX();

            for (Shelf shelf : shelves){
                topX = Math.min(shelf.getPosition().getX(), topX);
                bottomX = Math.max(shelf.getPosition().getX(),bottomX);
            }
            this.top = new Coordinates(topX, y);
            this.bottom = new Coordinates(bottomX, y);
        }
    }
}
