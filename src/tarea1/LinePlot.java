package tarea1;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import javax.swing.*;

public class LinePlot extends JFrame {

    public LinePlot(String title, double[][] loss){
        super(title);

        XYSeriesCollection dataset = new XYSeriesCollection();
        for (int i=0; i<loss.length; i++){
        XYSeries series = new XYSeries("Row "+ i);
            for (int j=0; j<loss[i].length; j++) {
                series.add(j, loss[i][j]);
            }
            dataset.addSeries(series);
        }

        JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                "X",
                "Y",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false

        );

        // Create Panel
        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);

    }

    public void show_plot(){
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
