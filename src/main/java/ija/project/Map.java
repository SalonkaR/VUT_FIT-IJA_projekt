package ija.project;

import java.util.ArrayList;
import java.util.List;

public class Map {
    private List<Shelf> shelves;

    public Map(){
        shelves = new ArrayList<>();
    }

    public List<Shelf> getShelves(){
        return shelves;
    }

    public void setShelves(List<Shelf> shelves){
        this.shelves = shelves;
    }

    @Override
    public String toString() {
        return "Map{" +
                "shelves=" + shelves +
                '}';
    }
}
