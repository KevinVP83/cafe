package be.hogent.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderTest {

    private Beverage cola;
    private Beverage latte;
    private Order order1;
    private Order order2;
    private Order order3;
    private Order order4;
    private Waiter wout;
    private Waiter nathalie;

    @BeforeEach
    void setup(){
        wout = new Waiter(1,"Peters","Wout","password");
        nathalie = new Waiter(2,"Segers","Nathalie","password");
        cola = new Beverage(1,"Cola",2.40);
        latte = new Beverage(2,"Latte",3.20);
        order1 = new Order();
        order2 = new Order();
        order3 = new Order();
        order4 = new Order();
    }

    @Test
    void addOrderItemTest() {
        order1.addOrderItem(new OrderItem(cola, 2));
        order1.addOrderItem(new OrderItem(latte, 3));
        assertEquals(2,order1.getOrderItems().size(),"addOrderItemTest 1 failed");
        order1.addOrderItem(new OrderItem(cola, 3));
        assertEquals(2,order1.getOrderItems().size(),"addOrderItemTest 2 should still give 2");
    }

    @Test
    void getTotalPriceTest() {
        order1.addOrderItem(new OrderItem(cola, 2));
        order1.addOrderItem(new OrderItem(latte, 3));
        assertEquals(14.4,order1.getTotalPrice(),2,"getTotalPrize test 1 failed");
        order1.addOrderItem(new OrderItem(cola, 3));
        assertEquals(21.6,order1.getTotalPrice(),2,"getTotalPrize test 2 failed");
    }

    @Test
    void getTableNrTest(){
        Order order5 = new Order(5);
        assertEquals(5,order5.getTableNr(),"getTableNrTest failed. TableNr should be 5!");
    }

    @Test
    void getDateTest(){
        Order order5 = new Order(5);
        assertEquals(LocalDate.now(),order5.getDate(),"Order 5 date should be today");
    }
}
