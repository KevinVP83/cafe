package be.hogent.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Order {
    private static Logger logger = LogManager.getLogger(Order.class.getName());
    private Waiter waiter;
    private int orderNr;
    private int tableNr;
    private Set<OrderItem> orderItems = new HashSet<>();
    private LocalDate date;

    //Getters and Setters

    public Waiter getWaiter() {
        return waiter;
    }

    public int getOrderNr() {
        return orderNr;
    }

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate() {
        this.date = LocalDate.now();
    }

    public void setWaiter(Waiter waiter){ this.waiter = waiter;}

    public int getTableNr() { return tableNr; }

    //constructor

    public Order(){}

    public Order(int tableNr){
        setDate();
        this.tableNr = tableNr;
    }

    //Methods

    public void addOrderItem(OrderItem orderItem){
        if(!(orderItems.contains(orderItem))){
            orderItems.add(orderItem);
            logger.info("OrderItem successfully created for table " + tableNr + "!");
        }
        else {
            for (OrderItem o: orderItems) {
                if (orderItem.equals(o)){
                    o.setQuantity(orderItem.getQuantity());
                    logger.info("OrderItem successfully updated for table " + tableNr + "!");
                }
            }
        }
    }

    public double getTotalPrice() {
        return orderItems.stream().mapToDouble(OrderItem::getPrice).sum();
    }

    public void setOrderNr(int orderNr) {
        this.orderNr = orderNr;
    }
}

