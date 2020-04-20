package be.hogent.model.dao;

import be.hogent.model.Beverage;
import java.util.Set;

public interface BeverageDAO {
    public Set<Beverage> getBeverages();
    public Beverage getBeverageByID(int beverageID);
}
