package be.hogent.model.dao;

import be.hogent.model.OrderItem;
import be.hogent.model.Table;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderDAOImpl extends BaseDAO implements OrderDAO {

    private static final String INSERT_ORDER = "insert into orders ( orderNumber,beverageID, qty, date, waiterID)\" + \"values (?, ?, ?,?,?)";
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
                orderStatement.execute();
            }
        } catch (SQLException e){
            logger.error("failed adding Order to Database", e);
            throw new DAOException("Error inserting order " + e.getMessage());
        }
    }
}
