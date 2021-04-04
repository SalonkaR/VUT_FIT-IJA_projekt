package ija.project;

import java.util.List;

public class Data {
    private List<Coordinate> coordinates;
    private Shelf shelf;

    //empty constructor for jackson(yml)
    public Data(){
    }

    public Data(List<Coordinate> coordinates, Shelf shelf) {
        this.coordinates = coordinates;
        this.shelf = shelf;
    }

    //getter for jackson(yml)
    public List<Coordinate> getCoordinates(){
        return coordinates;
    }

    //getter for jackson(yml)
    public Shelf getShelf(){
        return shelf;
    }

    @Override
    public String toString() {
        return "Data{" +
                "coordinates=" + coordinates +
                ", shelf=" + shelf +
                '}';
    }
}
