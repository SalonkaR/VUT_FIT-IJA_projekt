package ija.project;

/**
 * Trieda Coordinates reprezentuje poziciu na platne vo forme suradnic x a y
 *
 * @author Jakub Sokolík - xsokol14
 */
public class Coordinates {
    private double x;
    private double y;

    private Coordinates() { }

    /**
     * Konstruktor, ktory priradi argumenty x a y do premennych
     * @param x suradnica na osi X
     * @param y suradnica na osi Y
     */
    public Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Vracia hodnotu pozicie na osi X
     * @return suradnica x
     */
    public double getX() {
        return x;
    }

    /**
     * Nastavuje hodnotu suradnice x
     * @param x suradnica x
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Vracia hodnotu pozicie na osi Y
     * @return suradnica y
     */
    public double getY() {
        return y;
    }

    /**
     * Nastavuje hodnotu suradnice y
     * @param y suradnica y
     */
    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "[" + x +
                "," + y +
                ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinates)) return false;

        Coordinates coordinates = (Coordinates) o;

        if (Double.compare(coordinates.x, x) != 0) return false;
        return Double.compare(coordinates.y, y) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
