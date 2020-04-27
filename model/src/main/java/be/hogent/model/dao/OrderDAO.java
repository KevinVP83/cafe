package be.hogent.model.dao;

import be.hogent.model.Order;
import be.hogent.model.OrderItem;
import be.hogent.model.Table;
import be.hogent.model.Waiter;

import java.util.List;
import java.util.Set;

public interface OrderDAO {
    public void insertOrder(Table table) throws DAOException;
    public int getLatestOrderNr();
    public Set<Order> getAllOrders();
    public int deleteOrder(int orderNr);
    //public List<OrderItem> getAllOrderItemsForWaiter(Waiter waiter);
}
