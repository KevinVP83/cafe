package be.hogent.model.dao;

import be.hogent.model.Beverage;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class BeverageDAOImplTest {

    @Test
    public void getAllBeveragesTest() {

        List<Beverage> beverages = BeverageDAOImpl.getInstance().getBeverages();
        assertEquals(17,beverages.size(),"Beverages size should be 17");
    }

    @Test
    public void getBeverageByIDTest(){
        Beverage beverage = BeverageDAOImpl.getInstance().getBeverageByID(5);
        assertEquals(12.0,beverage.getPrice(),2,"getBeverageByID test 1 failed");
        beverage = BeverageDAOImpl.getInstance().getBeverageByID(2);
        assertEquals(3.0,beverage.getPrice(),2,"getBeverageByID test 2 failed");
        //assertThrows(SQLException.class, (Executable) BeverageDAOImpl.getInstance().getBeverageByID(20));
    }
}
