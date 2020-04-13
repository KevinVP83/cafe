package be.hogent.model.dao;

import be.hogent.model.Waiter;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class WaiterDAOImplTest {

    @Test
    public void getAllWaitersTest() {
        Set<Waiter> waiters = WaiterDAOImpl.getInstance().getWaiters();
        assertEquals(4,waiters.size(),"Waiters size should be 4");
    }
}