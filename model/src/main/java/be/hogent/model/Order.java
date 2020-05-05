package be.hogent.model;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Order implements Serializable {

    private static final long serialVersionUID = 8372442029493730445L;
    private final Logger logger = LogManager.getLogger(Order.class.getName());
    private int waiterID;
    private int orderNr;
    private int tableNr;
    private final Set<OrderItem> orderItems = new HashSet<>();
    private LocalDate date;

    //constructors

    public Order(){}

    public Order(int tableNr){
        setDate();
        setTableNr(tableNr);
    }

    public Order(int orderNr, Date date, int waiterID){
        this.orderNr = orderNr;
        this.date = date.toLocalDate();
        this.waiterID = waiterID;
    }


    //Getters and Setters

    public int getWaiterID() {
        return waiterID;
    }

    public int getOrderNr() {
        return orderNr;
    }

    public Set<OrderItem> getOrderItems() { return orderItems;}

    public LocalDate getDate() {
        return date;
    }

    public void setDate() { this.date = LocalDate.now();}

    public void setWaiterID(Waiter waiter){ this.waiterID = waiter.getWaiterID();}

    public int getTableNr() { return tableNr; }

    public void setOrderNr(int orderNr) { this.orderNr = orderNr;}

    public void setTableNr(int tableNr) { this.tableNr = tableNr;}

    //Overrides

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderNr == order.orderNr;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNr);
    }

    //Methods

    public void addOrderItem(OrderItem orderItem){
        if(!(orderItems.contains(orderItem))){
            orderItems.add(orderItem);
            logger.info("OrderItem successfully created for table " + getTableNr() + "!");
        }
        else {
            for (OrderItem o: orderItems) {
                if (orderItem.equals(o)){
                    o.increaseQuantity(orderItem.getQuantity());
                    logger.info("OrderItem successfully updated for table " + getTableNr() + "!");
                }
            }
        }
    }

    public void removeOrderItem(OrderItem orderItem){
        if(!(orderItems.contains(orderItem))){
            logger.error("No such beverage found for table " + getTableNr() + "!");
        }
        else {
            for (OrderItem o: orderItems) {
                if (orderItem.equals(o)){
                    if(o.getQuantity()<orderItem.getQuantity()){
                        logger.error("No such amount of  " + o.getBeverage().toString() + " found!");
                    }
                    else {
                        o.decreaseQuantity(orderItem.getQuantity());
                        logger.info("OrderItem successfully updated for table " + getTableNr() + "!");
                    }
                }
            }
        }
    }

    public double getTotalPrice() {
        return orderItems.stream().mapToDouble(OrderItem::getPrice).sum();
    }
}

