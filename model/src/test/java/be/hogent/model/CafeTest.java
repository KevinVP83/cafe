package be.hogent.model;

import be.hogent.model.dao.DAOException;
import be.hogent.model.dao.OrderDAOImpl;
import be.hogent.model.reports.PDFReport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CafeTest {
    private Cafe cafe;
    private Waiter wouter;
    private Waiter nathan;
    private Waiter ilse;
    private Waiter patrick;
    private Table table7;
    private Table table8;
    private Beverage beverage1;
    private Beverage beverage2;


    @BeforeEach
    public void setup() {
        cafe = new Cafe();
        wouter = new Waiter(5, "Peters", "Wouter", "password");
        nathan = new Waiter(6, "Segers", "Nathan", "password");
        ilse = new Waiter(7, "Vandenbroeck", "Ilse", "password");
        patrick = new Waiter(8, "Desmet", "Patrick", "password");
        table7 = new Table(7);
        table8 = new Table(8);
        beverage1 = new Beverage(1, "Cola", 2.40);
        beverage2 = new Beverage(2, "Cola-Light", 2.40);
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

        assertTrue(cafe.login("Wouter Peters", "password"),"loginTest 1 failed");
        assertTrue(cafe.isLoggedOn(),"cafe.isLoggedOn should be true!");
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
        cafe.getTables().add(table7);
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
        cafe.setTables();
        assertEquals(6,cafe.getTables().size(),"Test setTables failed");
    }

    @Test
    void getTablesTest(){
        assertEquals(6,cafe.getTables().size(),"Test getTables failed");
    }

    @Test
    void assignWaiterTest() throws Cafe.AlreadyLoggedOnException, Cafe.WrongCredentialsException, Cafe.alreadyOtherWaiterAssignedException {
        cafe.addWaiter(wouter);
        cafe.addWaiter(nathan);
        cafe.getTables().add(table7);
        cafe.setActiveTable(table7);
        cafe.login("Wouter Peters", "password");
        cafe.assignWaiter(table7);
        assertEquals(wouter, table7.getAssignedWaiter(),"Table1 assigned waiter should be Wout");
        cafe.logoff();
        cafe.login("Nathan Segers", "password");
        cafe.setActiveTable(table7);
        try {
            cafe.assignWaiter(cafe.getActiveTable());
        }
        catch (Cafe.alreadyOtherWaiterAssignedException e){
            Assertions.assertEquals("be.hogent.model.Cafe$alreadyOtherWaiterAssignedException", e.getClass().getName(), "assignWaiterTest 2 failed. alreadyOtherWaiterAssignedException expected");
        }
        assertEquals(wouter, table7.getAssignedWaiter(),"assignWaiterTest 3 failed. Table1 assigned waiter should still be Wout");
    }

    @Test
    void orderRemoveOrderAndPayTest() throws Cafe.alreadyOtherWaiterAssignedException, Cafe.AlreadyLoggedOnException, Cafe.WrongCredentialsException {
        cafe.addWaiter(nathan);
        cafe.getTables().add(table7);
        cafe.login("Wout Peters", "password");
        cafe.setActiveTable(table7);
        cafe.order(beverage1, 2);
        assertEquals(1, cafe.getActiveTable().getOrder().getOrderItems().size(), "orderTest1 failed. OrderItems should be 1");
        cafe.order(beverage1, 3);
        assertEquals(1, cafe.getActiveTable().getOrder().getOrderItems().size(), "orderTest2 failed. OrderItems should still be 1");
        assertEquals(12.0, cafe.getActiveTable().getOrder().getTotalPrice(), 2, "orderTest2 failed");
        cafe.removeOrder(beverage1,2);
        assertEquals(7.2, cafe.getActiveTable().getOrder().getTotalPrice(), 2, "Remove order test failed");
        cafe.order(beverage2, 2);
        assertEquals(2, cafe.getActiveTable().getOrder().getOrderItems().size(), "orderTest3 failed. OrderItems should be 2");
        cafe.logoff();
        cafe.login("Nathan Segers", "password");
        cafe.setActiveTable(table7);
        try {
            cafe.order(beverage1, 2);
        } catch (Cafe.alreadyOtherWaiterAssignedException e) {
            Assertions.assertEquals("be.hogent.model.Cafe$alreadyOtherWaiterAssignedException", e.getClass().getName(), "orderTest 4 failed. alreadyOtherWaiterAssignedException expected");
        }
        try {
            cafe.removeOrder(beverage1, 2);
        } catch (Cafe.alreadyOtherWaiterAssignedException e) {
            Assertions.assertEquals("be.hogent.model.Cafe$alreadyOtherWaiterAssignedException", e.getClass().getName(), "Remove order test 2 failed. alreadyOtherWaiterAssignedException expected");
        }
        try {
            cafe.pay(table7);
        } catch (Cafe.alreadyOtherWaiterAssignedException e) {
            Assertions.assertEquals("be.hogent.model.Cafe$alreadyOtherWaiterAssignedException", e.getClass().getName(), "PayTest 1 failed. alreadyOtherWaiterAssignedException expected");
        }
        cafe.logoff();
        cafe.login("Wout Peters", "password");
        cafe.pay(table7);
        assertNull(table7.getOrder(), "Table 7 order should be null");
        OrderDAOImpl.getInstance().deleteOrder(OrderDAOImpl.getInstance().getLatestOrderNr());
    }
    @Test
    public void getAllOrderItemsForWaiterTest() throws Cafe.AlreadyLoggedOnException, Cafe.WrongCredentialsException {
        cafe.login("Wout Peters","password");
        assertTrue(cafe.getAllOrderItemsForWaiter(cafe.getLoggedOnWaiter()).size()>0,"getAllOrdersForWaiterTest failed");
    }

    @Test
    public void getAllOrderItemsByDateTest() throws DAOException, Cafe.AlreadyLoggedOnException, Cafe.WrongCredentialsException {
        cafe.login("Patrick Desmet","password");
        table7.setAssignedWaiter(cafe.getLoggedOnWaiter());
        table7.createOrder();
        table7.getOrder().setOrderNr(99999);
        table7.getOrder().addOrderItem(new OrderItem(beverage1,4));
        table7.getOrder().addOrderItem(new OrderItem(beverage2,3));
        OrderDAOImpl.getInstance().insertOrder(table7);
        assertTrue(cafe.getAllOrderItemsByDate(cafe.getLoggedOnWaiter(), LocalDate.now()).size()>0);
        assertTrue(OrderDAOImpl.getInstance().deleteOrder(table7.getOrder().getOrderNr())>0);
        OrderDAOImpl.getInstance().deleteOrder(table7.getOrder().getOrderNr());
    }

    @Test
    public void createPDFTest() throws Cafe.AlreadyLoggedOnException, Cafe.WrongCredentialsException {
        cafe.login("Wout Peters", "password");
        assertTrue(cafe.createPDF(cafe.getLoggedOnWaiter(), cafe.getAllOrderItemsForWaiter(cafe.getLoggedOnWaiter())), "exportToPDFTest failed!");
    }

    @Test
    public void getTotalWaiterSalesTest(){
        assertEquals(3, cafe.getTop3WaiterSales().size());
    }

    @Test
    public void showTopWaitersReport() throws IOException {
        assertTrue(cafe.showTopWaitersReport(cafe.getTop3WaiterSales()),"showTopWaitersReport failed!");
    }

    @Test
    public void getAllDatesForWaiterTest() throws Cafe.AlreadyLoggedOnException, Cafe.WrongCredentialsException, DAOException {
        cafe.login("Wout Peters", "password");
        assertTrue(cafe.getAllDatesForWaiter(cafe.getLoggedOnWaiter()).size()>0);
    }

    @Test
    public void serializeTest(){
        assertTrue(cafe.serializeTables());
        assertTrue(cafe.deserializeTables());
    }
}