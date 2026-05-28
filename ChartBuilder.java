import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import java.util.Collections;
/**
 * ChartBuilder creates JavaFX LineChart objects showing pollution trends
 * over the 6 year data rane(2018-2023).
 *
 * @author Begum Zeytun
 * @version 1.0
 */
public class ChartBuilder
{
    /**
     * Builds a LineChart showing the average pollution trend over the years.
     * 
     * @param yearlyData A map from year string to list of DataPoints for the given year.
     * @param pollutant The pollutant label shown in the chart title.
     * @return A JavaFX LineChart ready to add to the scene.
     */
    public LineChart<Number, Number> buildAverageChart(Map<String, List<DataPoint>>
                     yearlyData, String pollutant)
    {
        Map<String, Double> trend = StatisticsCalc.yearlyAverageTrend(yearlyData);
        return buildChart(trend, "Average " + pollutant.toUpperCase() + " (µg/m³)", pollutant);
    }

    /**
     * Builds a LineChart showing the max pollution trend over the years.
     *
     * @param yearlyData A map from year string to list of DataPoints for the given year.
     * @param pollutant The pollutant label shown in the chart title.
     * @return A JavaFX LineChart ready to add to the scene.
     */
    public LineChart<Number, Number> buildMaxChart(Map<String, List<DataPoint>> yearlyData, String pollutant)
    {   
        Map<String, Double> trend = StatisticsCalc.yearlyMaxTrend(yearlyData);
        return buildChart(trend, "Maximum " + pollutant.toUpperCase() + " (µg/m³)", pollutant);
    }
    
    /**
     * Builds a LineChart showing the min pollution trend over all years.
     * 
     * @param yearlyData A map from year string to list of DataPoints for the given year.
     * @param pollutant The pollutant label shown in the chart title.
     * @return A JavaFX LineChart ready to add to the scene.
     */
    public LineChart<Number, Number> buildMinChart(Map<String, List<DataPoint>> yearlyData, String pollutant)
    {
        Map<String, Double> trend = StatisticsCalc.yearlyMinTrend(yearlyData);
        return buildChart(trend, "Minimum " + pollutant.toUpperCase() + " (µg/m³)", pollutant);
    }
    
    /**
     * Builds a LineChart showing the median pollution trend over all years.
     * 
     * @param yearlyData A map from year string to list of DataPoints for the given year.
     * @param pollutant The pollutant label shown in the chart title.
     * @return A JavaFX LineChart ready to add to the scene.
     */
    public LineChart<Number, Number> buildMedianChart(Map<String, List<DataPoint>> yearlyData, String pollutant)
    {
        // Calculates median for each year
        TreeMap<String, Double> medianTrend = new TreeMap<>();
        for (Map.Entry<String, List<DataPoint>> entry : yearlyData.entrySet())
        {
            double median = StatisticsCalc.median(entry.getValue());
            medianTrend.put(entry.getKey(), median);
        }
        return buildChart(medianTrend, "Median " + pollutant.toUpperCase() + " (µg/m³)", pollutant);
    }
    
    /**
     * Builds a LineChart showing the standard deviation trend over the years.
     * This helps visualise how spread out pollution values are each year.
     * 
     * @param yearlyData A map from year string to list of DataPoints for the given year.
     * @param pollutant The pollutant label shown in the chart title.
     * @return A JavaFX LineChart ready to add to the scene.
     */
    public LineChart<Number, Number> buildStdDevChart(
                 Map<String, List<DataPoint>> yearlyData, String pollutant)
    {
        // Calculate std dev for each year
        TreeMap<String, Double> stdDevTrend = new TreeMap<>();
        for (Map.Entry<String, List<DataPoint>> entry : yearlyData.entrySet())
        {
            double stdDev = StatisticsCalc.standardDeviation(entry.getValue());
            stdDevTrend.put(entry.getKey(), stdDev);
        }
        return buildChart(stdDevTrend, "Std Dev " + pollutant.toUpperCase() + " (µg/m³)", pollutant);
    }
    
    /**
     * Builds a BarChart comparing average pollution values across all years.
     * This gives a clear side by side visual comparison between years.
     * 
     * @param yearlyData A map from year string to list of DataPoints for the given year.
     * @param pollutant The pollutant label shown in the chart title.
     * @return A JavaFX BarChart ready to add to the scene.
     */
    public BarChart<String, Number> buildYearlyBarChart(Map<String, List<DataPoint>> yearlyData, String pollutant)
    {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Year");
        xAxis.setStyle("-fx-tick-label-fill: #cccccc;");
        
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("µg/m³");
        yAxis.setAutoRanging(true);
        yAxis.setStyle("-fx-tick-label-fill: #cccccc;");
        
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle(pollutant.toUpperCase() + " Average by Year");
        chart.setAnimated(false);
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Average " + pollutant.toUpperCase());
        
        // Sort years so bars go in order
        TreeMap<String, List<DataPoint>> sorted = new TreeMap<>(yearlyData);
        for (Map.Entry<String, List<DataPoint>> entry : sorted.entrySet()) {
            double avg = StatisticsCalc.average(entry.getValue());
            if (avg >= 0)
            {
                series.getData().add(new XYChart.Data<>(entry.getKey(), avg));
            }
        }
        
        chart.getData().add(series);
        chart.setStyle(
            "-fx-background-color: #1e1e2e;" +
            "-fx-text-fill: #cccccc;"
            );
        chart.setPrefHeight(300);
        return chart;
    }
    
    /**
     * Builds a LineChart that shows average, max, min together for a
     * comprehensive view of the trend over the 6 years.
     * 
     * @param yearlyData A map from year string to list of DataPoints for the given year.
     * @param pollutant The pollutant label shown in the chart title.
     * @return A JavaFX LineChart with three series.
     */
    public LineChart<Number, Number> buildCombinedChart(
            Map<String, List<DataPoint>> yearlyData, String pollutant)
    {
        Map<String, Double> avgTrend = StatisticsCalc.yearlyAverageTrend(yearlyData);
        Map<String, Double> maxTrend = StatisticsCalc.yearlyMaxTrend(yearlyData);
        Map<String, Double> minTrend = StatisticsCalc.yearlyMinTrend(yearlyData);
        
        NumberAxis xAxis = buildYearAxis();
        NumberAxis yAxis = buildValueAxis();
        
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle(pollutant.toUpperCase() + " Pollution Trend (2018–2023)");
        chart.setAnimated(false);
        chart.setStyle("-fx-background-color: #1e1e2e;");
        
        chart.getData().add(buildSeries(avgTrend, "Average"));
        chart.getData().add(buildSeries(maxTrend, "Maximum"));
        chart.getData().add(buildSeries(minTrend, "Minimum"));
        styleChart(chart);
        return chart;
    }
    
    /**
     * Builds a BarChart comparing two years side by side.
     * Shows average, max, min, and median for both years.
     * 
     * @param year1Data Data points for the first year.
     * @param year2Data Data points for the second year.
     * @param year1 The first year label.
     * @param year2 The second year label.
     * @param pollutant The pollutant label for the chart title.
     * @return A BarChart comparing two years.
     */
    public BarChart<String, Number> buildTwoYearComparisonChart(
            List<DataPoint> year1Data, List<DataPoint> year2Data,
            String year1, String year2, String pollutant)
    {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Statistic");
        xAxis.setStyle("-fx-tick-label-fill: #cccccc;");
        
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("µg/m³");
        yAxis.setAutoRanging(true);
        yAxis.setStyle("-fx-tick-label-fill: #cccccc;");
        
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle(pollutant.toUpperCase() + " Comparison: " + year1 + " vs " + year2);
        chart.setAnimated(false);
        
        // Series for year 1
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName(year1);
        series1.getData().add(new XYChart.Data<>("Average", StatisticsCalc.average(year1Data)));
        series1.getData().add(new XYChart.Data<>("Maximum", StatisticsCalc.max(year1Data)));
        series1.getData().add(new XYChart.Data<>("Minimum", StatisticsCalc.min(year1Data)));
        series1.getData().add(new XYChart.Data<>("Median", StatisticsCalc.median(year1Data)));
        
        // Series for year 2
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName(year2);
        series2.getData().add(new XYChart.Data<>("Average", StatisticsCalc.average(year2Data)));
        series2.getData().add(new XYChart.Data<>("Maximum", StatisticsCalc.max(year2Data)));
        series2.getData().add(new XYChart.Data<>("Minimum", StatisticsCalc.min(year2Data)));
        series2.getData().add(new XYChart.Data<>("Median", StatisticsCalc.median(year2Data)));
        
        chart.getData().add(series1);
        chart.getData().add(series2);
        
        chart.setStyle(
            "-fx-background-color: #1e1e2e;" +
            "-fx-text-fill: #cccccc;"
            );
            chart.setPrefHeight(350);
            return chart;
    }
    
    /**
     * Internal helper that builds a single series LineChart from a year-value map.
     * 
     * @param trend A map from year string to pollution value.
     * @param seriesName The label for the data series.
     * @param pollutant Used for the chart title.
     * @return A ready to use LineChart.
     */
    private LineChart<Number, Number> buildChart(
            Map<String, Double> trend, String seriesName, String pollutant)
    {
        NumberAxis xAxis = buildYearAxis();
        NumberAxis yAxis = buildValueAxis();
        
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle(seriesName);
        chart.setAnimated(false);
        chart.setStyle("-fx-background-color: #1e1e2e;");
        
        chart.getData().add(buildSeries(trend, seriesName));
        styleChart(chart);
        return chart;
    }
    
    /**
     * Builds a chart data series from a year-value map.
     * Years are sorted numerically so the line always runs left to right.
     * 
     * @param trend A map from year string to double value.
     * @param seriesName The display name for this series.
     * @return A populated XYChart.Series
     */
    private XYChart.Series<Number, Number> buildSeries(
            Map<String, Double> trend, String seriesName)
    {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(seriesName);
        
        // Sorts by year so the line is always chronological
        TreeMap<String, Double> sorted = new TreeMap<>(trend);
        for (Map.Entry<String, Double> entry : sorted.entrySet()) 
        {
            double value = entry.getValue();
            if (value >= 0) 
            {
                series.getData().add(
                    new XYChart.Data<>(Integer.parseInt(entry.getKey()), value));
            }
        }
        return series;
    }
    
    /**
     * Builds the X axis configured for the years.
     * 
     * @return A NumberAxis for the year range.
     */
    private NumberAxis buildYearAxis()
    {
        NumberAxis xAxis = new NumberAxis(2017, 2024, 1);
        xAxis.setLabel("Year");
        xAxis.setTickLabelFormatter(new javafx.util.StringConverter<Number>()
        {
            @Override
            public String toString(Number n)
            {
                int v = n.intValue();
                // only show labels for the years we have data for
                if (v >= 2018 && v <= 2023)
                {
                    return String.valueOf(v);
                } else
                {
                    return "";
                }
            }
            @Override
            public Number fromString(String s) 
            { 
                return Integer.parseInt(s); 
            }
        });
        xAxis.setStyle("-fx-tick-label-fill: #cccccc;");
        return xAxis;
    }
    
    /**
     * Builds the Y axis for pollution values.
     * 
     * @return A NumberAxis with auto-range enabled.
     */
    private NumberAxis buildValueAxis()
    {
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("µg/m³");
        yAxis.setAutoRanging(true);
        yAxis.setStyle("-fx-tick-label-fill: #cccccc;");
        return yAxis;
    }
    
    /**
     * Apply consistent dark theme CSS styling to a chart.
     * 
     * @param chart The chart to style.
     */
    private void styleChart(LineChart<Number, Number> chart)
    {
        chart.setStyle(
            "-fx-background-color: #1e1e2e;" +
            "-fx-plot-background-color: #12122a;" +
            "-fx-text-fill: #cccccc;"
        );
        chart.lookup(".chart-title");
        chart.setPrefHeight(300);
    }
}