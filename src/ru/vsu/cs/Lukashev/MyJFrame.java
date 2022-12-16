package ru.vsu.cs.Lukashev;

import flanagan.math.FourierTransform;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.Range;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class MyJFrame extends JFrame {
    public MyJFrame() {
//        initUImodulation();
//        initUIFreq();
//        initUIsyntSig();
        initUIFiltSig();
    }

    private void initUIFreq(){
        XYDataset dataset = createDatasetFreq();
        JFreeChart chart = createChartFreq(dataset, "Частотный спектр");
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        add(chartPanel);
        pack();
        setTitle("Line chart");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    private XYDataset createDatasetFreq() {
        Calculation c = new Calculation();
        double[][] arr = c.generateSpectrum(c.amplModulation());
        XYSeries seriesAmp = generateXYSeries("Amp",arr);
        arr = c.generateSpectrum(c.freqModulation());
        XYSeries seriesFreq = generateXYSeries("Freq", arr);
        arr = c.generateSpectrum(c.phaseModulation());
        XYSeries seriesPhase = generateXYSeries("Phase", arr);
        var dataset = new XYSeriesCollection();
        dataset.addSeries(seriesAmp);
        dataset.addSeries(seriesPhase);
        dataset.addSeries(seriesFreq);
        return dataset;
    }
    private JFreeChart createChartFreq(XYDataset dataset, String name) {

        JFreeChart chart = ChartFactory.createXYLineChart(
                name,
                "",
                "",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();

        var renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesShapesVisible (0, false);
        renderer.setSeriesPaint         (0, Color.orange);
        renderer.setSeriesStroke        (0, new BasicStroke(2.5f));

        renderer.setSeriesShapesVisible (1, false);
        renderer.setSeriesPaint         (1, Color.red);
        renderer.setSeriesStroke        (1, new BasicStroke(2.5f));

        renderer.setSeriesShapesVisible (2, false);
        renderer.setSeriesPaint         (2, Color.green);
        renderer.setSeriesStroke        (2, new BasicStroke(2.5f));
        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);
        NumberAxis rangeAxisY = (NumberAxis) plot.getRangeAxis(0);
        NumberAxis rangeAxisX = (NumberAxis) plot.getDomainAxis();
        rangeAxisY.setRange(0, 0.5);
        rangeAxisX.setRange(0, 300);
        chart.setTitle(new TextTitle(name,
                        new Font("Serif", java.awt.Font.BOLD, 18)
                )
        );
        return chart;
    }

    private void initUIsyntSig(){
        XYDataset dataset = createDatasetSyntSig();
        JFreeChart chart = createChartSyntSig(dataset, "Синтезированный сигнал");
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        add(chartPanel);
        pack();
        setTitle("Line chart");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    private XYDataset createDatasetSyntSig() {
        Calculation c = new Calculation();
        double[][] spectrum = c.generateSpectrum(c.amplModulation());
        double[] specY = c.absolute(spectrum[1]);
        int i=0;
        for(double d : specY){
            if(d < 0.2)
                specY[i] = 0;
            i++;
        }

        FourierTransform fourierTransform = new FourierTransform();
        fourierTransform.setFftData(specY);
        fourierTransform.transform();
        double[] y = fourierTransform.getTransformedDataAsAlternate();
        double[][] arr = new double[c.getTimeline().length][c.getTimeline().length];
        arr[0] = c.getTimeline();
        arr[1] = y;
        XYSeries seriesAmp = generateXYSeries("Amp",arr);
        var dataset = new XYSeriesCollection();
        dataset.addSeries(seriesAmp);
        return dataset;
    }
    private JFreeChart createChartSyntSig(XYDataset dataset, String name) {

        JFreeChart chart = ChartFactory.createXYLineChart(
                name,
                "",
                "",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();

        var renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesShapesVisible (0, false);
        renderer.setSeriesPaint         (0, Color.orange);
        renderer.setSeriesStroke        (0, new BasicStroke(2.5f));

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);
        NumberAxis rangeAxisY = (NumberAxis) plot.getRangeAxis();
//        NumberAxis rangeAxisX = (NumberAxis) plot.getDomainAxis();
        rangeAxisY.setRange(-3, 3);
//        rangeAxisX.setRange(0, 300);
        chart.setTitle(new TextTitle(name,
                        new Font("Serif", java.awt.Font.BOLD, 18)
                )
        );
        return chart;
    }




    private void initUIFiltSig(){
        XYDataset dataset = createDatasetFiltSig();
        JFreeChart chart = createChartFiltSig(dataset, "Отфильтрованный сигнал");
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        add(chartPanel);
        pack();
        setTitle("Line chart");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private XYDataset createDatasetFiltSig() {
        Calculation c = new Calculation();
        double[][] spectrum = c.generateSpectrum(c.amplModulation());
        double[] specY = c.absolute(spectrum[1]);
        int i=0;
        for(double d : specY){
            if(d < 0.2)
                specY[i] = 0;
            i++;
        }

        FourierTransform fourierTransform = new FourierTransform();
        fourierTransform.setFftData(specY);
        fourierTransform.transform();
        double[] y = fourierTransform.getTransformedDataAsAlternate();
        double[][] arr = new double[2][c.getTimeline().length];
        y = c.absolute(y);
        arr[0] = c.getTimeline();
        arr[1] = c.generateOnlyOneZero(y);
        System.out.println(y.length + " ]]]]]]]]]]]]]");
        System.out.println(arr[1].length + " ]]]]]]]]]]]]]");
        XYSeries seriesAmp = generateXYSeries("Amp",arr);
        var dataset = new XYSeriesCollection();
        dataset.addSeries(seriesAmp);
        return dataset;
    }
    private JFreeChart createChartFiltSig(XYDataset dataset, String name) {

        JFreeChart chart = ChartFactory.createXYLineChart(
                name,
                "",
                "",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();

        var renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesShapesVisible (0, false);
        renderer.setSeriesPaint         (0, Color.orange);
        renderer.setSeriesStroke        (0, new BasicStroke(2.5f));

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);
        NumberAxis rangeAxisY = (NumberAxis) plot.getRangeAxis();
        NumberAxis rangeAxisX = (NumberAxis) plot.getDomainAxis();
        rangeAxisY.setRange(0, 1.5);
        rangeAxisX.setRange(0, 0.514);
        chart.setTitle(new TextTitle(name,
                        new Font("Serif", java.awt.Font.BOLD, 18)
                )
        );
        return chart;
    }



    private void initUImodulation() {
        XYDataset dataset = createDatasetModulation();
        JFreeChart chart = createChartModulation(dataset, "Модуляция");

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        add(chartPanel);
        pack();
        setTitle("Line chart");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    private XYDataset createDatasetModulation() {
        Calculation c = new Calculation();
        double[] y = c.amplModulation(), x = c.getTimeline();
        XYSeries seriesAmp = generateXYSeries("Amp", x, y);
        y = c.freqModulation();
        XYSeries seriesFreq = generateXYSeries("Freq", x, y);
        y = c.phaseModulation();
        XYSeries seriesPhase = generateXYSeries("Phase", x, y);
        var dataset = new XYSeriesCollection();
        dataset.addSeries(seriesAmp);
        dataset.addSeries(seriesPhase);
        dataset.addSeries(seriesFreq);
        return dataset;
    }
    private JFreeChart createChartModulation(XYDataset dataset, String name) {

        JFreeChart chart = ChartFactory.createXYLineChart(
                name,
                "",
                "",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();

        var renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesShapesVisible (0, false);
        renderer.setSeriesPaint         (0, Color.orange);
        renderer.setSeriesStroke        (0, new BasicStroke(2.5f));

        renderer.setSeriesShapesVisible (1, false);
        renderer.setSeriesPaint         (1, Color.red);
        renderer.setSeriesStroke        (1, new BasicStroke(2.5f));

        renderer.setSeriesShapesVisible (2, false);
        renderer.setSeriesPaint         (2, Color.green);
        renderer.setSeriesStroke        (2, new BasicStroke(2.5f));
        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);

        chart.setTitle(new TextTitle(name,
                        new Font("Serif", java.awt.Font.BOLD, 18)
                )
        );
        return chart;
    }



    private XYSeries generateXYSeries(String name, double[] x, double[] y){
        var series = new XYSeries(name);
        for (int i = 0; i < x.length; i++){
            series.add(x[i], y[i]);
        }
        return series;
    }

    private XYSeries generateXYSeries(String name, double[][] arr){
        double[] x = arr[0];
        double[] y = arr[1];
        var series = new XYSeries(name);
        for (int i = 0; i < y.length; i++){
            series.add(x[i], y[i]);
        }
        return series;
    }






}
