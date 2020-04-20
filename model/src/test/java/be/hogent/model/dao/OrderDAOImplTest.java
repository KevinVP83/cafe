package be.hogent.model.dao;

import be.hogent.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class OrderDAOImplTest {

    private Beverage cola;
    private Beverage latte;
    private OrderItem orderItem1;
    private OrderItem orderItem2;
    private Waiter wout;
    private Order order;
    private Table table1;
    private Set<Order> orders;

    @BeforeEach
    public void setup(){
        wout = new Waiter(1,"Peters","Wout","password");
        cola = new Beverage(1,"Cola",2.40);
        latte = new Beverage(2,"Latte",3.20);
        orderItem1 = new OrderItem(cola,2);
        orderItem2 = new OrderItem(latte,3);
        order = new Order();
        table1 = new Table(1);
    }

    @Test
    public void testGetAllOrdersAndDeleteOrders() throws DAOException {
        table1.setAssignedWaiter(wout);
        table1.createOrder();
        table1.getOrder().setOrderNr(9999);
        table1.getOrder().addOrderItem(orderItem1);
        table1.getOrder().addOrderItem(orderItem2);
        OrderDAOImpl.getInstance().insertOrder(table1);
        table1.clearTable();
        assertNull(table1.getOrder(), "testGetAllOrders 1 failed");
        orders = OrderDAOImpl.getInstance().getAllOrders();
        assertNotNull(orders,"GetAllOrdersTest 2 failed");
        assertTrue(OrderDAOImpl.getInstance().deleteOrder(9999)>0,"Delete order test failed!");
    }

    @Test
    public void getAllOrdersForWaiterTest(){
        assertNotNull(OrderDAOImpl.getInstance().getAllOrderItemsForWaiter(wout));
    }
}
