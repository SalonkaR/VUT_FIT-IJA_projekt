package ija.project;

public class Item {
    private Goods goods;
    private Shelf shelf;
    private boolean reserve = false;

    public Item(Goods goods, Shelf shelf) {
        this.goods = goods;
        this.shelf = shelf;
        this.goods.addItem(this);
        this.shelf.addItem(this);

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

    public void setReserve() {
        reserve = true;
    }

    public boolean isReserve() {
        return reserve;
    }
}
