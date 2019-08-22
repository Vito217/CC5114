package tarea1;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeriesCollection;
import java.awt.Color;
import javax.swing.*;

public class BinaryPlot extends JFrame {

    public BinaryPlot(String title, Tuple dt, double m, double b){
        super(title);

        // Main plot
        XYPlot plot = new XYPlot();

        // Scatter elements
        XYItemRenderer renderer1 = new XYLineAndShapeRenderer(false, true);
        ValueAxis domain1 = new NumberAxis("Domain1");
        ValueAxis range1 = new NumberAxis("Range1");

        // Scatter Data
        XYSeries series1 = new XYSeries("1");
        XYSeries series2 = new XYSeries("2");

        double[][] dataset = (double[][]) dt.getFirst();
        double[][] output = (double[][]) dt.getSecond();

        for(int i=0; i<dataset.length; i++){
            if(Math.round(output[i][0]) == 0){
                series1.add(dataset[i][0], dataset[i][1]);
            }
            else {
                series2.add(dataset[i][0], dataset[i][1]);
            }
        }
        XYSeriesCollection data1 = new XYSeriesCollection();
        data1.addSeries(series1);
        data1.addSeries(series2);

        // Setting Scatter
        plot.setDataset(0, data1);
        plot.setRenderer(0, renderer1);
        plot.setDomainAxis(0, domain1);
        plot.setRangeAxis(0, range1);
        plot.mapDatasetToDomainAxis(0, 0);
        plot.mapDatasetToRangeAxis(0, 0);

        // Line data
        XYSeries collection2 = new XYSeries("3");
        collection2.add(0.0, b);
        collection2.add(1.0, m + b);
        XYSeriesCollection data2 = new XYSeriesCollection();
        data2.addSeries(collection2);

        // Line elements
        XYItemRenderer renderer2 = new XYLineAndShapeRenderer(true, false);
        ValueAxis domain2 = new NumberAxis("Domain2");
        ValueAxis range2 = new NumberAxis("Range2");

        // Setting line
        plot.setDataset(1, data2);
        plot.setRenderer(1, renderer2);
        plot.setDomainAxis(1, domain2);
        plot.setRangeAxis(1, range2);
        plot.mapDatasetToDomainAxis(1, 0);
        plot.mapDatasetToRangeAxis(1, 1);

        JFreeChart chart = new JFreeChart(
                "Multi Dataset Chart",
                JFreeChart.DEFAULT_TITLE_FONT,
                plot,
                true
        );

        plot.setBackgroundPaint(new Color(255,228,196));

        // Create Panel
        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }

    public void show_plot(){
        pack( );
        setVisible( true );
    }
}
