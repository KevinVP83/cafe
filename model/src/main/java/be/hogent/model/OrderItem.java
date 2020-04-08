package be.hogent.model;

import java.util.Objects;

public class OrderItem {
    private Beverage beverage;
    private int quantity;

    //Getters and Setters

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity += quantity;
    }

    public Beverage getBeverage() {
        return beverage;
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


    //Constructor

    public OrderItem(Beverage beverage, int quantity){
        this.beverage = beverage;
        this.quantity = quantity;
    }

    //methods

    public double getPrice() {
        return (beverage.getPrice()) * quantity;
    }
}
