package be.hogent.model;

import java.util.Objects;
import java.util.logging.Logger;

public class OrderItem {
    private static Logger logger = Logger.getLogger(OrderItem.class.getName());
    private Beverage beverage;
    private int quantity;
    private String name;

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

    public String getName() { return beverage.getName(); }

    public void setName(String name){
        this.name = name;
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
        setName(beverage.getName());
    }

    //methods

    public double getPrice() {
        return (beverage.getPrice()) * quantity;
    }
}
