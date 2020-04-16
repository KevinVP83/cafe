package be.hogent.model.dao;

import be.hogent.model.Waiter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class WaiterDAOImpl extends BaseDAO implements WaiterDAO{

    private static final String GET_ALL_WAITERS = "SELECT * from waiters";
    private final Logger logger = LogManager.getLogger(WaiterDAOImpl.class.getName());

    private static WaiterDAOImpl instance = new WaiterDAOImpl();

    private WaiterDAOImpl() {
    }

    public static WaiterDAOImpl getInstance() {
        if (instance == null)
            instance = new WaiterDAOImpl();
        return instance;
    }

    @Override
    public Set<Waiter> getWaiters() {
        Set<Waiter> waiters = new HashSet<>();
        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_WAITERS);
                ResultSet rs = preparedStatement.executeQuery()
        ) {

            while (rs.next()) {
                Waiter waiter = new Waiter(rs.getInt("waiterID"),rs.getString("lastName"),rs.getString("firstName"),rs.getString("password"));
                waiters.add(waiter);
            }
            logger.info("Waiters successfully loaded from database");

        } catch (SQLException | DAOException e) {
            logger.error("Error getting waiters from database " + e.getMessage());
        }
        return waiters;
    }
}
