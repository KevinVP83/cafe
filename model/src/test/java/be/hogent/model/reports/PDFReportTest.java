package be.hogent.model.reports;

import be.hogent.model.Cafe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PDFReportTest {

    private Cafe cafe;


    @BeforeEach
    public void setup() throws Cafe.AlreadyLoggedOnException, Cafe.WrongCredentialsException {
        cafe =  new Cafe();
        cafe.login("Wout Peters","password");
    }

    @Test
    public void exportToPDFTest(){
        assertTrue(PDFReport.getInstance().exportToPDF(cafe.getLoggedOnWaiter(), cafe.getAllOrderItemsForWaiter(cafe.getLoggedOnWaiter())), "exportToPDFTest failed!");
    }
}
