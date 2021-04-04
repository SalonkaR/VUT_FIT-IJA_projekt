package ija.project;

import java.util.ArrayList;

public class ShelfBlock {
    ArrayList<Shelf> shelfs;
    
    Coordinates top;
    Coordinates bottom;

    public ShelfBlock() {
    }
    
    public void addShelf(Shelf shelf){
        shelfs.add(shelf);
    }
    
    public void setCordinate(){
        if (!shelfs.isEmpty()){
            double y = shelfs.get(0).getPosition().getY();
            double topX = shelfs.get(0).getPosition().getX();
            double bottomX = shelfs.get(0).getPosition().getX();

            for (Shelf shelf : shelfs){
                topX = Math.min(shelf.getPosition().getX(), topX);
                bottomX = Math.max(shelf.getPosition().getX(),bottomX);
            }

            this.top = new Coordinates(topX, y);
            this.bottom = new Coordinates(bottomX, y);

        }
    }
}