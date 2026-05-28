import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.List;
import java.util.Map;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
/**
 * StatsPanel displays pollution statistics for the currently selected pollutant.
 * 
 * Shows: 
 *   Summary statistics for the selected year.
 *   A chart of trends across all 6 years with multiple chart type options.
 *   
 * @author Begum Zeytun
 * @version 1.0
 */
public class StatsPanel extends VBox
{
    // UI elements
    private Label pollutantLabel;
    private Label yearLabel;
    private Label avgValueLabel;
    private Label maxValueLabel;
    private Label minValueLabel;
    private Label countLabel;
    private Label stddevLabel;
    private ComboBox<String> chartTypeCombo;
    private VBox chartContainer;
    private Label medianValueLabel;
    
    // dependencies
    private PollutionFilter filter;
    private ChartBuilder chartBuilder;
    
    // current selection state
    private String currentPollutant = "no2";
    private String currentYear = "2023";
    
    // chart type options
    private static final String CHART_COMBINED = "Average / Max / Min";
    private static final String CHART_AVERAGE = "Average only";
    private static final String CHART_MAX = "Maximum only";
    private static final String CHART_MIN = "Minimum only";
    private static final String CHART_MEDIAN = "Median only";
    private static final String CHART_STDDEV = "Std Deviation";
    private static final String CHART_BAR = "Bar Chart (Yearly Avg)";
    /**
     * Construct the StatsPanel.
     * 
     * @param filter A PollutionFilter wired to the shared DataManager.
     */
    public StatsPanel(PollutionFilter filter)
    {
        super(16);
        this.filter = filter;
        this.chartBuilder = new ChartBuilder();
        
        setPadding(new Insets(20, 24, 20, 24));
        setStyle("-fx-background-color: #1a1a2e;");
        
        getChildren().addAll(
            buildHeader(),
            buildStatCards(),
            buildChartControls(),
            buildChartContainer()
        );
        // initial display 
        refresh(currentPollutant, currentYear);
    }

    /**
     * Refresh all statistics and the chart for a new pollutant/year selection.
     *
     * @param pollutant The selected pollutant.
     * @param year The selected year.
     */
    public void refresh(String pollutant, String year)
    {
        currentPollutant = pollutant;
        currentYear = year;
        List<DataPoint> points = filter.getFilteredData(pollutant, year);
        pollutantLabel.setText("Pollutant: " + pollutant.toUpperCase());
        yearLabel.setText("Year: " + year);
        
        if(points.isEmpty())
        {
            avgValueLabel.setText("–");
            maxValueLabel.setText("–");
            minValueLabel.setText("–");
            medianValueLabel.setText("–");
            countLabel.setText("–");
            stddevLabel.setText("–");
        } else{
            avgValueLabel.setText(String.format("%.2f µg/m³", StatisticsCalc.average(points)));
            maxValueLabel.setText(String.format("%.2f µg/m³", StatisticsCalc.max(points)));
            minValueLabel.setText(String.format("%.2f µg/m³", StatisticsCalc.min(points)));
            medianValueLabel.setText(String.format("%.2f µg/m³", StatisticsCalc.median(points)));
            countLabel.setText(String.valueOf(StatisticsCalc.countValid(points)));
            stddevLabel.setText(String.format("%.2f µg/m³", StatisticsCalc.standardDeviation(points)));
            
        }
        rebuildChart();
    }
    
    /**
     * Builds the panel header row with pollutant and year labels.
     * 
     * @return An HBox containing the header labels.
     */
    private HBox buildHeader()
    {
        pollutantLabel = styledLabel("Pollutant: -", 16, true);
        yearLabel = styledLabel("Year: -", 16, false);
        HBox header = new HBox(24, pollutantLabel, yearLabel);
        header.setAlignment(Pos.CENTER_LEFT);
        return header;
    }
    
    /**
     * Builds the row of statistic cards(average, max, min, count, std dev).
     * 
     * @return An HBox containing one card per statistic.
     */
    private HBox buildStatCards()
    {
        avgValueLabel = styledLabel("-", 20, true);
        maxValueLabel = styledLabel("-", 20, true);
        minValueLabel = styledLabel("-", 20, true);
        medianValueLabel = styledLabel("-", 20, true);
        countLabel = styledLabel("-", 20, true);
        stddevLabel = styledLabel("-", 20, true);
        
        HBox cards = new HBox(12,
                  buildCard("Average", avgValueLabel),
                  buildCard("Maximum", maxValueLabel),
                  buildCard("Minimum", minValueLabel),
                  buildCard("Median", medianValueLabel),
                  buildCard("Data Points", countLabel),
                  buildCard("Std Dev", stddevLabel)
                  );
                  cards.setAlignment(Pos.CENTER_LEFT);
                  return cards;
    }
    
    /**
     * Builds an individual statistic card(a labelled box with a value).
     * 
     * @param title The card heading.
     * @param valueLabel The label that will hold the numeric value.
     * @return A styled VBox card.
     */
    private VBox buildCard(String title, Label valueLabel)
    {
        Label titleLbl = styledLabel(title, 11, false);
        titleLbl.setStyle("-fx-text-fill: #8080a0;");
        
        VBox card = new VBox(4, titleLbl, valueLabel);
        card.setPadding(new Insets(10, 12, 10, 12));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle(
            "-fx-background-color: #22223a;" +
            "-fx-background-radius: 6;" +
            "-fx-border-color: #3a3a5c;" +
            "-fx-border-radius: 6;" +
            "-fx-border-width: 1;"
            );
            card.setPrefWidth(130);
            return card;
    }
    
    /**
     * Builds the chart type selector row above the chart.
     * 
     * @return An HBox containing the label and ComboBox
     */
    private HBox buildChartControls()
    {
        Label lbl = styledLabel("Chart type:", 13, false);
        chartTypeCombo = new ComboBox<>();
        chartTypeCombo.getItems().addAll(CHART_COMBINED, CHART_AVERAGE, CHART_MAX, CHART_MIN, CHART_MEDIAN, CHART_STDDEV, CHART_BAR);
        chartTypeCombo.setValue(CHART_COMBINED);
        chartTypeCombo.setStyle(
            "-fx-background-color: #22223a;" +
            "-fx-text-fill: #cccccc;" +
            "-fx-border-color: #3a3a5c;"
            );
            chartTypeCombo.setOnAction(e -> rebuildChart());
            
            HBox row = new HBox(10, lbl, chartTypeCombo);
            row.setAlignment(Pos.CENTER_LEFT);
            return row;
    }
    
    /**
     * Builds the container that holds the LineChart.
     * 
     * @return A VBox that will hold the chart.
     */
    private VBox buildChartContainer()
    {
        chartContainer = new VBox();
        chartContainer.setStyle("-fx-background-color: #12122a; -fx-background-radius: 6;");
        VBox.setVgrow(chartContainer, Priority.ALWAYS);
        return chartContainer;
    }
    
    /**
     * Rebuilds the chart inside chartContainer based on the current pollutant,
     * year and chart type selection.
     */
    private void rebuildChart()
    {
        chartContainer.getChildren().clear();
        
        Map<String, List<DataPoint>> yearlyData = filter.getFilteredDataAllYears(currentPollutant);
        String type = chartTypeCombo.getValue();
        
        Node chart;
        if (CHART_AVERAGE.equals(type)) 
        {
            chart = chartBuilder.buildAverageChart(yearlyData, currentPollutant);
        } else if (CHART_MAX.equals(type)) 
        {
            chart = chartBuilder.buildMaxChart(yearlyData, currentPollutant);
        } else if (CHART_MIN.equals(type)) 
        {
            chart = chartBuilder.buildMinChart(yearlyData, currentPollutant);
        } else if (CHART_MEDIAN.equals(type)) 
        {
            chart = chartBuilder.buildMedianChart(yearlyData, currentPollutant);
        } else if (CHART_STDDEV.equals(type)) 
        {
            chart = chartBuilder.buildStdDevChart(yearlyData, currentPollutant);
        } else if (CHART_BAR.equals(type)) 
        {
            chart = chartBuilder.buildYearlyBarChart(yearlyData, currentPollutant);
        } else {
            chart = chartBuilder.buildCombinedChart(yearlyData, currentPollutant);
        }
        
        VBox.setVgrow(chart, Priority.ALWAYS);
        chartContainer.getChildren().add(chart);
    }
    
    /**
     * Creates a styled Label helper.
     * 
     * @param text The label text.
     * @param size Font size.
     * @param bold Whether to use bold weight.
     * @return A configured Label.
     */
    private Label styledLabel(String text, int size, boolean bold)
    {
        Label label = new Label(text);
        // choose font weight based on bold parameter
        FontWeight weight;
        if(bold) 
        {
            weight = FontWeight.BOLD;
        } else {
            weight = FontWeight.NORMAL;
        }
        label.setFont(Font.font("Arial", weight, size));
        label.setStyle("-fx-text-fill: #e0e0e0;");
        return label;
    }
}