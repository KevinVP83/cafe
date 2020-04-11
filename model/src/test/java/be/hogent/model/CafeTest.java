package be.hogent.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CafeTest {
    private Cafe cafe;
    private Waiter wout;
    private Waiter nathalie;
    private Waiter ilse;
    private Waiter patrick;
    private Table table1;
    private Table table2;
    private Table table3;


    @BeforeEach
    public void setup(){
        cafe = new Cafe();
        wout = new Waiter(1,"Peters","Wout", "password");
        nathalie = new Waiter(2,"Segers","Nathalie", "password");
        ilse = new Waiter(3,"Vandenbroeck","Ilse", "password");
        patrick = new Waiter(4,"Desmet","Patrick", "password");
        table1 = new Table(1);
        table2 = new Table(2);
        table3 = new Table(3);
    }


    @Test
    void addWaiterTest() {
        Assertions.assertEquals(0, cafe.getWaiters().size(), "Test addWaiter() 01 failed");
        cafe.addWaiter(wout);
        Assertions.assertEquals(1, cafe.getWaiters().size(), "Test addWaiter() 02 failed");
        cafe.addWaiter(nathalie);
        cafe.addWaiter(ilse);
        cafe.addWaiter(patrick);
        Assertions.assertEquals(4, cafe.getWaiters().size(), "Test addWaiter() 03 failed");
        cafe.addWaiter(patrick);
        Assertions.assertEquals(4, cafe.getWaiters().size(), "Test addWaiter() 03 failed");
    }

    @Test
    void loginTest() throws Cafe.AlreadyLoggedOnException, Cafe.WrongCredentialsException {
        cafe.addWaiter(wout);
        cafe.addWaiter(nathalie);
        cafe.addWaiter(ilse);
        cafe.addWaiter(patrick);

        cafe.login("Wout Peters", "password");
        assertTrue(cafe.isLoggedOn(),"loginTest 1 failed");
        assertEquals(wout, cafe.getLoggedOnWaiter());

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
        cafe.setActiveTable(table1);
        assertEquals(table1,cafe.getActiveTable(),"activeTable should be table 1");
        cafe.setActiveTable(table3);
        assertEquals(table3,cafe.getActiveTable(),"activeTable should be table 3");
    }

    @Test
    void setBeveragesTest() {
        cafe.setBeverages();
        assertEquals(5,cafe.getBeverages().size(),"Beverages size should be 5");
    }

    @Test
    void getBeveragesTest() {
        assertEquals(0,cafe.getBeverages().size(),"Beverages should be empty");
        cafe.setBeverages();
        assertEquals(5,cafe.getBeverages().size(),"Beverages size should be 5");
    }

    @Test
    void setTablesTest(){
       cafe.setTables();
       assertEquals(6,cafe.getTables().size(),"Test setTables failed");
    }

    @Test
    void getTablesTest(){
        assertEquals(0,cafe.getTables().size(),"Test setTables failed");
        cafe.setTables();
        assertEquals(6,cafe.getTables().size(),"Test setTables failed");
    }

    @Test
    void payTest(){

    }

    @Test
    void assignWaiterTest() throws Cafe.alreadyOtherWaiterAssignedException {
        cafe.getTables().add(table1);
        cafe.assignWaiter(1,wout);
        assertEquals(wout, table1.getAssignedWaiter(),"Table1 assigned waiter should be Wout");
        
    }
}