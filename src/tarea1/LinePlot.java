package tarea1;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class LinePlot extends JFrame {

    public LinePlot(String title, double[] loss){
        super(title);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
        for(int i=0; i<loss.length; i++){
            dataset.addValue( loss[i] , "y" , Integer.toString(i) );
        }

        JFreeChart chart = ChartFactory.createLineChart(
                title,
                "X",
                "Y",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false

        );

        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        for(double l: loss){
            max = l>max ? l : max;
            min = l<max ? l : min;
        }
        CategoryPlot plot = chart.getCategoryPlot();
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(min, max);

        // Create Panel
        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
        setContentPane(panel);

    }

    public void show_plot(){
        pack( );
        setVisible( true );
    }
}
