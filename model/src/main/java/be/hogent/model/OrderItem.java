package be.hogent.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.logging.Logger;

public class OrderItem implements Serializable {

    private static final long serialVersionUID = -2173213416551832750L;
    private final Beverage beverage;
    private int quantity;
    private String name;

    //Constructor

    public OrderItem(Beverage beverage, int quantity){
        this.beverage = beverage;
        this.quantity = quantity;
        setName();
    }

    //Getters and Setters

    public int getQuantity() {
        return quantity;
    }

    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void decreaseQuantity(int quantity){
        this.quantity -= quantity;
    }

    public Beverage getBeverage() {
        return beverage;
    }

    public String getName() { return name; }

    public void setName(){
        this.name = beverage.getName();
    }

    //Overrides

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(beverage, orderItem.beverage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beverage);
    }

    //methods

    public double getPrice() {
        return (beverage.getPrice()) * quantity;
    }
}
