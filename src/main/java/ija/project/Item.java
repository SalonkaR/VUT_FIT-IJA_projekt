package ija.project;

public class Item {
    private Goods goods;
    private Shelf shelf;

    public Item(Goods goods, Shelf shelf) {
        this.goods = goods;
        this.shelf = shelf;
        this.goods.addItem(this);
        if (shelf != null){
            this.shelf.addItem(this);
        }
    }

    public Goods getGoods() {
        return goods;
    }

    public Shelf getShelf() {
        return shelf;
    }
    
    public void sell(){
        this.goods.removeItem(this);
    }
}
