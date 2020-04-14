package be.hogent.model.dao;

import be.hogent.model.Beverage;
import be.hogent.model.OrderItem;
import be.hogent.model.Table;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class OrderDAOImpl extends BaseDAO implements OrderDAO {

    private static final String INSERT_ORDER = "INSERT into orders (orderNumber, beverageID, qty, date, waiterID) VALUES (?, ?,?,?,?)";
    private static final String MAX_ORDER_NUMBER = "SELECT MAX(orderNumber) from orders";
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
                logger.info("blabla");
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
}
