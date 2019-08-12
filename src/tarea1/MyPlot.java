package tarea1;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeriesCollection;
import java.awt.Color;
import javax.swing.*;

public class MyPlot extends JFrame {

    public MyPlot(String title, double[][] dataset){
        super(title);
        XYSeries series1 = new XYSeries("1");
        XYSeries series2 = new XYSeries("2");

        for(int i=0; i<dataset.length; i++){
            if(Math.round(dataset[i][2]) == 0){
                series1.add(dataset[i][0], dataset[i][1]);
            }
            else {
                series2.add(dataset[i][0], dataset[i][1]);
            }
        }

        XYSeriesCollection data = new XYSeriesCollection();
        data.addSeries(series1);
        data.addSeries(series2);

        JFreeChart chart = ChartFactory.createScatterPlot(
                "Dataset",
                "X",
                "Y",
                 data
        );
        XYPlot plot = (XYPlot)chart.getPlot();
        plot.setBackgroundPaint(new Color(255,228,196));

        // Create Panel
        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }
}
