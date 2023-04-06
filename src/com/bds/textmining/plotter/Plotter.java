package com.bds.textmining.plotter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.data.xy.XYDataset;
import org.jfree.chart.JFreeChart;
//import org.jfree.chart.labels.StandardXYItemLabelGenerator;
//import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import javax.swing.JFrame;

import java.awt.Color;
import java.util.List;
import org.jfree.chart.ChartPanel;

public class Plotter {
	
	public static void plot2d(double[][] input) {      
        
        JFreeChart chart = ChartFactory.createScatterPlot(
	            "Original Scatter Plot", 
	            "feature 1", 
	            "feature 2", 
	            createDataset(input),
	            PlotOrientation.VERTICAL,
	            true,
	            true,
	            false
	            );

        chart.getXYPlot().getRenderer().setSeriesPaint(0, new Color(Color.BLUE.getRGB()));

        ChartFrame frame = new ChartFrame("Cluster Plot", chart);
        frame.pack();
        frame.setVisible(true);
        
		
	}
	
	public static void plotClusters(double[][] input, int[] clusterInfo, String[] docTitles) {
		
		XYSeriesCollection dataset = new XYSeriesCollection();  
        
		int k = 3;
		
        for (int i = 0; i < k; i++) {
            XYSeries series = new XYSeries("Cluster " + (i + 1));
            for (int j = 0; j < clusterInfo.length; j++) {
                if (clusterInfo[j] == i) {
                    series.add(input[j][0], input[j][1]);
                }
            }
            dataset.addSeries(series);
        }

        JFreeChart chart = ChartFactory.createScatterPlot(
        		"Clusters Scatter Plot", 
        		"feature 1", 
        		"feature 2", 
        		dataset, 
        		PlotOrientation.VERTICAL, 
        		true, 
        		true, 
        		false);

        int[] colors = {Color.BLUE.getRGB(), Color.ORANGE.getRGB(), Color.GREEN.getRGB()};
        int i = 0;
        for (Object seriesObj : dataset.getSeries()) {
            chart.getXYPlot().getRenderer().setSeriesPaint(i++, new Color(colors[i%colors.length]));
        }

        ChartFrame frame = new ChartFrame("Cluster Plot", chart);
        frame.pack();
        frame.setVisible(true);
	}
	
	private static XYDataset createDataset(double[][] input) {

		final XYSeriesCollection dataset = new XYSeriesCollection();

		XYSeries series = new XYSeries("Chrome");
		
		int m = input.length;
		  
	    for(int i = 0; i < m; i++)
	    		series.add(input[i][0],input[i][1]);

		dataset.addSeries(series);
		
		return dataset;

	}
}