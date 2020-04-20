package be.hogent.model;

public class Beverage {
    private final int ID;
    private final String name;
    private final double price;

    public Beverage(int ID, String name, double price){
        this.ID = ID;
        this.name = name;
        this.price = price;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String toString(){
        return this.getName();
    }
}
