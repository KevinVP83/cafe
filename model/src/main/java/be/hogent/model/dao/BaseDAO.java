package be.hogent.model.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class BaseDAO implements DAO {
    private final Logger logger = LogManager.getLogger (BaseDAO.class.getName ());

    private String dbUrl, dbPassword, dbUser;


    private  void readProperties (String propFile) {

        Properties dbProperties = new Properties ();

        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream (propFile)) {

            dbProperties.load (inputStream);
            dbUrl = dbProperties.getProperty("dbUrl");
            dbPassword = dbProperties.getProperty("dbPassword");
            dbUser = dbProperties.getProperty("dbUser");

        } catch (IOException ioe) {
            logger.error("db properties not loaded");
            ioe.printStackTrace ();
        }
    }

    public Connection getConnection () throws DAOException {
        try {
            readProperties("db.properties");
            return DriverManager.getConnection (dbUrl, dbUser, dbPassword);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DAOException ("Failed to connect to database!");
        }
    }
}
