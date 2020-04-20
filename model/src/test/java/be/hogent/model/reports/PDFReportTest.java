package be.hogent.model.reports;

import be.hogent.model.Waiter;
import be.hogent.model.dao.OrderDAOImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PDFReportTest {

    private Waiter wout;

    @BeforeEach
    public void setup(){
        wout = new Waiter(1,"Peters","Wout","password");
    }

    @Test
    public void exportToPDFTest(){
        PDFReport.getInstance().exportToPDF(OrderDAOImpl.getInstance().getAllOrderItemsForWaiter(wout));
    }
}
