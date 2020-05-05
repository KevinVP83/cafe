package be.hogent.model;

import java.io.Serializable;

public class Beverage implements Serializable {

    private static final long serialVersionUID = 2725342721603742762L;
    private final int ID;
    private final String name;
    private final double price;

    //Constructor

    public Beverage(int ID, String name, double price){
        this.ID = ID;
        this.name = name;
        this.price = price;
    }

    //Getters and setters

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
