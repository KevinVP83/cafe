package be.hogent.model.dao;

import be.hogent.model.Beverage;

import java.util.List;

public interface BeverageDAO {
    public List<Beverage> getBeverages();
    public Beverage getBeverageByID(int beverageID);
}
