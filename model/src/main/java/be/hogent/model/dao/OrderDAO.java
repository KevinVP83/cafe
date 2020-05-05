package be.hogent.model.dao;

import be.hogent.model.Order;
import be.hogent.model.Table;

import java.util.Set;

public interface OrderDAO {
    void insertOrder(Table table) throws DAOException;
    int getLatestOrderNr();
    Set<Order> getAllOrders();
    int deleteOrder(int orderNr);
    //public List<OrderItem> getAllOrderItemsForWaiter(Waiter waiter);
}
