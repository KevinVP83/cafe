package be.hogent.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CafeTest {
    private Cafe cafe;
    private Waiter wouter;
    private Waiter nathan;
    private Waiter ilse;
    private Waiter patrick;
    private Table table7;
    private Table table8;
    private Table table9;


    @BeforeEach
    public void setup(){
        cafe = new Cafe();
        wouter = new Waiter(5,"Peters","Wouter", "password");
        nathan = new Waiter(6,"Segers","Nathan", "password");
        ilse = new Waiter(7,"Vandenbroeck","Ilse", "password");
        patrick = new Waiter(8,"Desmet","Patrick", "password");
        table7 = new Table(7);
        table8 = new Table(8);
        table9 = new Table(9);
    }


    @Test
    void addWaiterTest() {
        Assertions.assertEquals(4, cafe.getWaiters().size(), "Test addWaiter() 01 failed");
        cafe.addWaiter(wouter);
        Assertions.assertEquals(5, cafe.getWaiters().size(), "Test addWaiter() 02 failed");
        cafe.addWaiter(nathan);
        cafe.addWaiter(ilse);
        cafe.addWaiter(patrick);
        Assertions.assertEquals(8, cafe.getWaiters().size(), "Test addWaiter() 03 failed");
        cafe.addWaiter(patrick);
        Assertions.assertEquals(8, cafe.getWaiters().size(), "Test addWaiter() 03 failed");
    }

    @Test
    void loginTest() throws Cafe.AlreadyLoggedOnException, Cafe.WrongCredentialsException {
        cafe.addWaiter(wouter);
        cafe.addWaiter(nathan);
        cafe.addWaiter(ilse);
        cafe.addWaiter(patrick);

        cafe.login("Wouter Peters", "password");
        assertTrue(cafe.isLoggedOn(),"loginTest 1 failed");
        assertEquals(wouter, cafe.getLoggedOnWaiter());

        try {
            cafe.login("Nathalie Segers", "password");
        }
        catch (Cafe.AlreadyLoggedOnException e){
            Assertions.assertEquals("be.hogent.model.Cafe$AlreadyLoggedOnException", e.getClass().getName(), "loginTest 2 failed");
        }
        cafe.logoff();

        try {
            cafe.login("Nathalie Segers", "paswoord");
        }
        catch (Cafe.WrongCredentialsException e){
            Assertions.assertEquals("be.hogent.model.Cafe$WrongCredentialsException", e.getClass().getName(), "loginTest 3 failed");
        }
    }

    @Test
    void getActiveTableTest() {
        cafe.setActiveTable(table7);
        assertEquals(table7,cafe.getActiveTable(),"activeTable should be table 7");
        cafe.setActiveTable(table8);
        assertEquals(table8,cafe.getActiveTable(),"activeTable should be table 8");
    }

    @Test
    void getBeveragesTest() {
        assertEquals(17,cafe.getBeverages().size(),"Beverages size should be 17");
    }

    @Test
    void setTablesTest(){
       assertEquals(6,cafe.getTables().size(),"Test setTables failed");
    }

    @Test
    void getTablesTest(){
        assertEquals(6,cafe.getTables().size(),"Test setTables failed");
    }

    @Test
    void payTest(){

    }

    @Test
    void assignWaiterTest() throws Cafe.AlreadyLoggedOnException, Cafe.WrongCredentialsException{
        cafe.addWaiter(wouter);
        cafe.addWaiter(nathan);
        cafe.setTables();
        cafe.getTables().add(table7);
        cafe.setActiveTable(table7);
        cafe.login("Wouter Peters", "password");
        cafe.assignWaiter(cafe.getActiveTable());
        assertEquals(wouter, table7.getAssignedWaiter(),"Table1 assigned waiter should be Wout");
        cafe.logoff();
        cafe.login("Nathan Segers", "password");
        cafe.setActiveTable(table7);
        cafe.assignWaiter(cafe.getActiveTable());
        assertEquals(wouter, table7.getAssignedWaiter(),"assignWaiterTest 2 failed. Table1 assigned waiter should still be Wout");
    }
}