package be.hogent.model.dao;

import be.hogent.model.Beverage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class BeverageDAOImpl extends BaseDAO implements BeverageDAO {

    private static final String GET_ALL_BEVERAGES = "SELECT * from beverages";

    private final Logger logger = LogManager.getLogger(BeverageDAOImpl.class.getName());

    private static BeverageDAOImpl instance = new BeverageDAOImpl();

    private BeverageDAOImpl() {
    }

    public static BeverageDAOImpl getInstance() {
        if (instance == null)
            instance = new BeverageDAOImpl();
        return instance;
    }

    @Override
    public Set<Beverage> getBeverages() {
        Set<Beverage> beverages = new HashSet<>();
        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_BEVERAGES);
                ResultSet rs = preparedStatement.executeQuery()
        ) {

            while (rs.next()) {
                Beverage beverage = new Beverage(rs.getInt("beverageID"), rs.getString("beverageName"), rs.getDouble("price"));
                beverages.add(beverage);
            }
            logger.info("Beverages successfully loaded from database");

        } catch (SQLException e) {
            logger.error("Error getting beverages from database " + e.getMessage());
        }
        return beverages;
    }
}

