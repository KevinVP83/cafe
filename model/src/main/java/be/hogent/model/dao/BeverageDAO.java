package be.hogent.model.dao;

import be.hogent.model.Beverage;

import java.util.List;

public interface BeverageDAO {
    List<Beverage> getBeverages();
    Beverage getBeverageByID(int beverageID);
}
