package be.hogent.model.dao;

import be.hogent.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class OrderDAOImpl extends BaseDAO implements OrderDAO {

    private static final String INSERT_ORDER = "INSERT into orders (orderNumber, beverageID, qty, date, waiterID) VALUES (?, ?,?,?,?)";
    private static final String MAX_ORDER_NUMBER = "SELECT MAX(orderNumber) from orders";
    private static final String GET_ALL_ORDERS = "SELECT * from orders";
    private static final String DELETE_ORDER = "DELETE from orders where orderNumber = ?";
    private static final String GET_DATES_FROM_SQL = "SELECT date from orders where waiterID = ?";


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
                ResultSet rs = preparedStatement.executeQuery()
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
                ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Order order = new Order(rs.getInt("orderNumber"), rs.getDate("date"), rs.getInt("waiterID"));
                Beverage beverage = BeverageDAOImpl.getInstance().getBeverageByID(rs.getInt("beverageID"));
                OrderItem orderItem = new OrderItem(beverage,rs.getInt("qty"));
                order.getOrderItems().add(orderItem);
                if (orders.size() == 0){
                    orders.add(order);
                }
                else {
                    if(!(orders.add(order))) {
                        for (Order o : orders) {
                            if (o.getOrderNr() == order.getOrderNr()) {
                                o.addOrderItem(orderItem);
                            }
                        }
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

    public TreeSet<LocalDate> getAllDates(Waiter waiter) throws DAOException{
        TreeSet<LocalDate> dates = new TreeSet<>();
        try ( Connection connection = getConnection();
               PreparedStatement getDates = connection.prepareStatement(GET_DATES_FROM_SQL)) {
               getDates.setInt(1,waiter.getWaiterID());

               ResultSet rs = getDates.executeQuery();{
                while (rs.next()) {
                    dates.add(rs.getDate("date").toLocalDate());
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error getting dates from database." + e.getMessage());
        }
        return dates;
    }
}

