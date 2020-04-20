package be.hogent.model.dao;

import be.hogent.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class OrderDAOImpl extends BaseDAO implements OrderDAO {

    private static final String INSERT_ORDER = "INSERT into orders (orderNumber, beverageID, qty, date, waiterID) VALUES (?, ?,?,?,?)";
    private static final String MAX_ORDER_NUMBER = "SELECT MAX(orderNumber) from orders";
    private static final String GET_ALL_ORDERS = "SELECT * from orders";
    private static final String DELETE_ORDER = "DELETE from orders where orderNumber = ?";
    private static final String GET_ALL_ORDERITEMS_FOR_WAITER = "SELECT * from orders where waiterID = ?";


    private final Logger logger = LogManager.getLogger(OrderDAOImpl.class.getName());

    private static OrderDAOImpl instance = new OrderDAOImpl();

    private OrderDAOImpl() {
    }

    public static OrderDAOImpl getInstance() {
        if (instance == null)
            instance = new OrderDAOImpl();
        return instance;
    }

    public void insertOrder(Table table) throws DAOException {
        try (Connection connection = getConnection();
             PreparedStatement orderStatement = connection.prepareStatement(INSERT_ORDER)) {
            for (OrderItem orderItem: table.getOrder().getOrderItems()
                 ) {
                orderStatement.setInt(1,table.getOrder().getOrderNr());
                orderStatement.setInt(2,orderItem.getBeverage().getID());
                orderStatement.setInt(3,orderItem.getQuantity());
                orderStatement.setDate(4, Date.valueOf(table.getOrder().getDate()));
                orderStatement.setInt(5,table.getAssignedWaiter().getWaiterID());
                orderStatement.executeUpdate();
                logger.info("Order added successfully!");
            }
        } catch (SQLException e){
            logger.error("failed adding Order to Database", e);
            throw new DAOException("Error inserting order " + e.getMessage());
        }
    }

    public int getLatestOrderNr(){
        int orderNr = 0;
        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(MAX_ORDER_NUMBER);
                ResultSet rs = preparedStatement.executeQuery();
        ) {

            while (rs.next()) {
                orderNr = rs.getInt(1);
            }
            logger.info("Last ordernr loaded from database");

        } catch (SQLException | DAOException e) {
            logger.error("Error getting latest ordernr from database " + e.getMessage());
        }
        return orderNr;
    }

    @Override
    public Set<Order> getAllOrders() {
        Set<Order> orders = new HashSet<>();
        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_ORDERS);
                ResultSet rs = preparedStatement.executeQuery()
        ) {

            while (rs.next()) {
                Order order = new Order(rs.getInt("orderNumber"), rs.getDate("date"), rs.getInt("waiterID"));
                Beverage beverage = BeverageDAOImpl.getInstance().getBeverageByID(rs.getInt("beverageID"));
                OrderItem orderItem = new OrderItem(beverage,rs.getInt("qty"));
                for (Order o: orders) {
                    if(o.getOrderNr()==order.getOrderNr()){
                        o.addOrderItem(orderItem);
                    }
                    else{
                        orders.add(order);
                    }
                }
            }
            logger.info("Orders successfully loaded from database");

        } catch (SQLException | DAOException e) {
            logger.error("Error getting orders from database " + e.getMessage());
        }
        return orders;
    }

    public int deleteOrder(int orderNr){
        int result = 0;
        try (Connection connection = getConnection();
             PreparedStatement pStatement = connection.prepareStatement(DELETE_ORDER)) {
            pStatement.setInt(1, orderNr);

            result = pStatement.executeUpdate();
            logger.info(String.format("%d order deleted", result));

        } catch (Exception e) {
            logger.error("Error delete order. " + e.getMessage());
        }
        return result;
    }

    public List<OrderItem> getAllOrderItemsForWaiter(Waiter waiter){
        List<OrderItem> orderItems = new ArrayList<>();
        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_ORDERITEMS_FOR_WAITER)){
            preparedStatement.setInt(1, waiter.getWaiterID());
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                OrderItem orderItem = new OrderItem(BeverageDAOImpl.getInstance().getBeverageByID(rs.getInt("beverageID")),rs.getInt("qty"));
                orderItems.add(orderItem);
            }
            logger.info("Waiters successfully loaded from database");

        } catch (SQLException | DAOException e) {
            logger.error("Error getting waiters from database " + e.getMessage());
        }
        return orderItems;
    }

    public List<OrderItem> getAllOrderItemsByDate(Waiter waiter, LocalDate date){
        Set<Order> orders = OrderDAOImpl.getInstance().getAllOrders();
        List<OrderItem> orderItems;
        orderItems = orders.stream()
                .filter(order -> order.getDate().equals(date))
                .filter(order -> order.getWaiterID() == waiter.getWaiterID())
                .flatMap(order -> order.getOrderItems().stream().collect(Collectors.toList()));
//        for (Order o: orders) {
//            if (waiter.getWaiterID()==o.getWaiterID() && date.equals(o.getDate())) {
//                for (OrderItem oI: o.getOrderItems()) {
//                    orderItems.add(oI);
//                }
//            }
//        }
        return orderItems;
    }
}

