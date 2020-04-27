package be.hogent.model.reports;

import be.hogent.model.Waiter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Map;

public class PieChartReport {

    Path dest = Paths.get(System.getProperty("user.home"),"TopWaiters.jpg") ;
    final Logger logger = LogManager.getLogger(PieChartReport.class.getName());


    private static PieChartReport instance = new PieChartReport();

    private PieChartReport() {
    }

    public static PieChartReport getInstance() {
        if (instance == null)
            instance = new PieChartReport();
        return instance;
    }

    private PieDataset createDataset(Map<Waiter,Double> totalSales) {
        DefaultPieDataset dataset = new DefaultPieDataset( );
        totalSales.forEach((waiter,sum) -> {dataset.setValue((waiter.toString()),sum);});
        return dataset;
    }

    public boolean createPieChart( PieDataset dataset ) throws IOException {
        JFreeChart chart = ChartFactory.createPieChart(
                "Top Waiters Chart", dataset,true,true,false);
        int width = 640;
        int height = 480;


        File pieChart = new File(dest.toString());
        ChartUtilities.saveChartAsJPEG(pieChart, chart, width, height);
        if (pieChart.exists()) {
            return true;
        } else {
            logger.error("JPG file creation failed");
            return false;
        }

    }

    public boolean makeChart(Map<Waiter,Double> totalSales) throws IOException {
        if(createPieChart(createDataset(totalSales))){
            return true;
        }
        else{return false;}
    }
}
