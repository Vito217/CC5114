package tarea1;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class MyPlot extends ApplicationFrame{

    public MyPlot(String title, double[][] dataset){
        super(title);
        final XYSeries series = new XYSeries("Dataset MyPlot");
        for(int i=0; i<dataset.length; i++){
            series.add(dataset[i][0], dataset[i][1]);
        }
        final XYSeriesCollection data = new XYSeriesCollection(series);
        final JFreeChart chart = ChartFactory.createXYLineChart(
                "Dataset",
                "X",
                "Y",
                data,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }
}
