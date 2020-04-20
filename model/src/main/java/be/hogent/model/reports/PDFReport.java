package be.hogent.model.reports;

import be.hogent.model.OrderItem;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.List;


public class PDFReport {

    private Path dest = Paths.get(System.getProperty("user.home"),"Cafe Java","test.pdf") ;
    private final Logger logger = LogManager.getLogger(PDFReport.class.getName());
    private static PDFReport instance = new PDFReport();

    private PDFReport() {
    }

    public static PDFReport getInstance() {
        if (instance == null)
            instance = new PDFReport();
        return instance;
    }

    public void exportToPDF(List<OrderItem> orderList) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(new File(dest.toString())));
            System.out.println(dest.toString());
            document.open();
            Paragraph p = new Paragraph();
            p.add("OrderItems sold by waiter - Overview");
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);
            Chunk line = new Chunk(new DottedLineSeparator());
            document.add(line);
            PdfPTable table = new PdfPTable(3);
            table.setHeaderRows(1);
            PdfPCell cell = new PdfPCell();
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPhrase(new Phrase("Beverage"));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Quantity"));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Price"));
            table.addCell(cell);
            for (OrderItem orderItem: orderList) {
                table.addCell(orderItem.getBeverage().toString());
                table.addCell(String.valueOf(orderItem.getQuantity()));
                table.addCell(new DecimalFormat("#.0#").format(orderItem.getPrice()));
            }
            document.add(table);
            document.add(line);
            Paragraph p2 = new Paragraph();
            p2.add("Total Price of these OrderItems is: € ");
            p2.setAlignment(Element.ALIGN_CENTER);
            document.add(p2);
            document.close();
            logger.info("pdf successfully created!");
        } catch (Exception e) {
            logger.error("error creating pdf file");
        }
    }
}
