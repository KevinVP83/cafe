package be.hogent.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {
    private Beverage beverage1;
    private Beverage beverage2;
    private Beverage beverage3;
    private Beverage beverage4;
    private OrderItem orderItem1;
    private OrderItem orderItem2;
    private OrderItem orderItem3;
    private OrderItem orderItem4;
    private OrderItem orderItem5;

    @BeforeEach
    void setup() {
        beverage1 = new Beverage(1,"Cola",2.40);
        beverage2 = new Beverage(2,"Cola-Light",2.40);
        beverage3 = new Beverage(3,"Jupiler",2.50);
        beverage4 = new Beverage(4,"Latte",3.20);
        orderItem1 = new OrderItem(beverage1,1);
        orderItem2 = new OrderItem(beverage2,2);
        orderItem3 = new OrderItem(beverage3,3);
        orderItem4 = new OrderItem(beverage4,4);
        orderItem5 = new OrderItem(beverage1,2);

    }

    @Test
    void getQuantityTest() {
        assertEquals(1,orderItem1.getQuantity(),"orderitem 1 quantity should be 1");
        assertEquals(4,orderItem4.getQuantity(),"orderitem 4 quantity should be 4");
    }

    @Test
    void setQuantityTest() {
        orderItem2.setQuantity(1);
        assertEquals(3,orderItem2.getQuantity(),"orderitem 2 quantity should be 3");
        orderItem3.setQuantity(4);
        assertEquals(7,orderItem3.getQuantity(),"orderitem 3 quantity should be 7");
    }

    @Test
    void getBeverageTest() {
        assertEquals(beverage1,orderItem1.getBeverage(),"orderitem 1 beverage should be beverage 1");
        assertEquals(beverage4,orderItem4.getBeverage(),"orderitem 4 beverage should be beverage 4");
    }

    @Test
    void getPriceTest() {
        assertEquals(2.40,orderItem1.getPrice(),"getPrice test 1 failed");
        orderItem1.setQuantity(3);
        assertEquals(9.60,orderItem1.getPrice(),"getPrice test 2 failed");
        assertEquals(12.80,orderItem4.getPrice(),"getPrice test 3 failed");
        orderItem4.setQuantity(4);
        assertEquals(25.60,orderItem4.getPrice(),"getPrice test 4 failed");
    }

    @Test
    void EqualsTest() {
        assertTrue(orderItem1.equals(orderItem5));
        assertFalse(orderItem1.equals(orderItem2));
    }

    @Test
    void HashCodeTest() {
        assertTrue(orderItem1.hashCode() == orderItem5.hashCode());
        assertFalse(orderItem1.hashCode() == orderItem3.hashCode());
    }
}
