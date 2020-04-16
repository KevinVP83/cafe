package be.hogent.model.dao;

import be.hogent.model.Table;

public interface OrderDAO {
    public void insertOrder(Table table) throws DAOException;
    public int getLatestOrderNr();
}
