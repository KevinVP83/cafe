package be.hogent.model.dao;

import java.sql.Connection;

public interface DAO {
    Connection getConnection () throws DAOException;
}
