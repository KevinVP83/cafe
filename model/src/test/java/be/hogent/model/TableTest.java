package be.hogent.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TableTest {
    private Table table1;
    private Table table2;
    private Table table3;
    private Table table4;
    private Waiter wout;
    private Waiter nathalie;

    @BeforeEach
    void setup() {
        table1 = new Table(1);
        table2 = new Table(2);
        table3 = new Table(3);
        table4 = new Table(4);
        wout = new Waiter(1,"Peters","Wout","password");
        nathalie = new Waiter(2,"Segers","Nathalie","password");
    }

    @Test
    void getTableNrTest() {
        assertEquals(1, table1.getTableNr(),"Table 1 tablenr should be 1");
        assertEquals(2, table2.getTableNr(),"Table 2 tablenr should be 2");
        assertEquals(3, table3.getTableNr(),"Table 3 tablenr should be 3");
        assertEquals(4, table4.getTableNr(),"Table 4 tablenr should be 4");
    }

    @Test
    void setAssignedWaiterTest() {
        table1.setAssignedWaiter(wout);
        assertEquals(wout, table1.getAssignedWaiter(), "Table 1 assigned waiter should be Wout !");
    }

    @Test
    void getAssignedWaiterTest() {
        table1.setAssignedWaiter(wout);
        assertEquals(wout,table1.getAssignedWaiter(),"Table 1 assigned waiter should be Wout !");
    }

    @Test
    void compareToTest(){
        assertEquals(1,table2.compareTo(table1),"CompareToTest failed!");
    }

    @Test
    void equalsTest(){
        Table tableTest = new Table(1);
        assertEquals(table1,tableTest,"equalsTest failed!");
    }
}