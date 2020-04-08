package be.hogent.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BeverageTest {
    private Beverage beverage1;
    private Beverage beverage2;
    private Beverage beverage3;
    private Beverage beverage4;

    @BeforeEach
    void setup(){
        beverage1 = new Beverage(1,"Cola",2.40);
        beverage2 = new Beverage(2,"Cola-Light",2.40);
        beverage3 = new Beverage(3,"Jupiler",2.50);
        beverage4 = new Beverage(4,"Latte",3.20);
    }

    @Test
    void getID() {
        assertEquals(1,beverage1.getID(),"ID should be 1");
        assertEquals(3,beverage3.getID(),"ID should be 3");
    }

    @Test
    void getName() {
        String expected = "Cola-Light";
        String actual = beverage2.getName();
        assertEquals(expected,actual);
        String expected1 = "Latte";
        String actual1 = beverage4.getName();
        assertEquals(expected1,actual1);
    }

    @Test
    void getPrice() {
        assertEquals(3.20,beverage4.getPrice(),"getPrice test1 failed!");
        assertEquals(2.50,beverage3.getPrice(),"getPrice test2 failed!");
    }
}