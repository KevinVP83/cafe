package be.hogent.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WaiterTest {
    private Waiter wout;
    private Waiter nathalie;
    private Waiter nathalie2;
    private Waiter patrick;

    @BeforeEach
    public void setup(){
        wout = new Waiter(1,"Peters","Wout","password");
        nathalie = new Waiter(2,"Segers","Nathalie","password");
        nathalie2 = new Waiter(2,"Segers","Nathalie","password");
        patrick = new Waiter(3,"Desmet","Patrick","password");

    }

    @Test
    void equalsTest() {
        assertNotEquals(wout, nathalie, "wout and nathalie shouldn't be equals");
        assertEquals(nathalie, nathalie2, "nathalie and nathalie2 should be equals");
    }

    @Test
    void getPasswordTest()  {
        assertEquals("password",wout.getPassword(),"password test Wout failed");
        assertEquals("password",nathalie.getPassword(),"password test Nathalie failed");
        assertEquals("password",patrick.getPassword(),"password test Patrick failed");
    }

    @Test
    void ToStringTest()  {
        assertTrue(wout.toString().contains("Wout Peters"),"ToString Wout failed!");
        assertTrue(nathalie.toString().contains("Nathalie Segers"),"ToString Nathalie failed!");
        assertTrue(patrick.toString().contains("Patrick Desmet"),"ToString Patrick failed!");
    }

    @Test
    void getIDTest() {
        assertEquals(1,wout.getWaiterID(),"ID from Wout should be 1");
        assertEquals(2,nathalie.getWaiterID(),"ID from Nathalie should be 1");
        assertEquals(3,patrick.getWaiterID(),"ID from Patrick should be 1");
    }

    @Test
    void getLastNameTest() {
        assertTrue(wout.getLastName().contains("Peters"),"getLastName Wout failed");
        assertTrue(nathalie.getLastName().contains("Segers"),"getLastName Nathalie failed");
        assertTrue(patrick.getLastName().contains("Desmet"),"getLastName Patrick failed");
        assertFalse(wout.getLastName().contains("Wout"),"getLastName Wout 2 failed");
    }

    @Test
    void getFirsNameTest() {
        assertTrue(wout.getFirsName().contains("Wout"),"getFirstName Wout failed");
        assertTrue(nathalie.getFirsName().contains("Nathalie"),"getFirstName Nathalie failed");
        assertTrue(patrick.getFirsName().contains("Patrick"),"getFirstName Patrick failed");
        assertFalse(patrick.getFirsName().contains("Desmet"),"getFirstName Patrick 2 failed");
    }
}