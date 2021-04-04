package ija.project;

import java.util.ArrayList;

public class ShelfBlock {
    ArrayList<Shelf> shelves;
    
    Coordinates top;
    Coordinates bottom;

    public ShelfBlock() {
    }
    
    public void addShelf(Shelf shelf){
        shelves.add(shelf);
    }
    
    public void setCordinate(){
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

    public void share(){
        for (Shelf shelf : shelves){
            shelf.saveBlock(this);
        }
    }
}
