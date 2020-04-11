package be.hogent.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class Order {
    private static Logger logger = Logger.getLogger(Order.class.getName());
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

    //Constructors

    public Order(){}

    public Order(int orderNr, Waiter waiter, int tableNr){
        setDate();
        this.orderNr = orderNr;
        this.waiter = waiter;
        this.tableNr = tableNr;
    }

    //Methods

    public void addOrderItem(OrderItem orderItem){
        if(!(orderItems.contains(orderItem))){orderItems.add(orderItem);}
        else {
            for (OrderItem o: orderItems) {
                if (orderItem.equals(o)){
                    o.setQuantity(orderItem.getQuantity());
                }
            }
        }
    }

    public double getTotalPrice() {
        return orderItems.stream().mapToDouble(OrderItem::getPrice).sum();
    }
}
