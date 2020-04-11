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

//    @Test
//    void setAssignedWaiterTest() throws Exception {
//        table1.setAssignedWaiter(wout);
//        assertEquals(wout, table1.getAssignedWaiter(), "Table 1 assigned waiter should be Wout !");
//        table1.setAssignedWaiter(wout);
//        assertEquals(wout, table1.getAssignedWaiter(), "Table 1 assigned waiter should be Wout !");
//        try {
//            table1.setAssignedWaiter(nathalie);
//        } catch (Table.alreadyOtherWaiterAssignedExeption e) {
//            Assertions.assertEquals("be.hogent.cafe.model.Table$alreadyOtherWaiterAssignedExeption", e.getClass().getName(), "Assigning Nathalie to table 1 should not work!");
//        }
//    }

    @Test
    void getAssignedWaiterTest() {
        table1.setAssignedWaiter(wout);
        assertEquals(wout,table1.getAssignedWaiter(),"Table 1 assigned waiter should be Wout !");
    }
}