package be.hogent.model.dao;

import be.hogent.model.Beverage;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BeverageDAOImplTest {
    @Test
    void testGetAllBeverages() {

        Set<Beverage> beverages = BeverageDAOImpl.getInstance().getBeverages();
        assertEquals(17,beverages.size(),"Beverages size should be 17");
    }
}
