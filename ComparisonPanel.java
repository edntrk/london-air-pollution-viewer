import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.List;

/**
 * ComparisonPanel allows users to compare pollution data between two 
 * different years for the same pollutant.
 * 
 * It shows side-by-side statistic cards for Year 1 and Year 2, and a
 * grouped bar chart comparing average, max, min, and median values.
 * A percentage change summary is also shown.
 *
 * @author Emre Gokdemir
 * @version 1.0
 */
public class ComparisonPanel extends ScrollPane
{
    private static final String[] YEARS = {"2018", "2019", "2020", "2021", "2022", "2023"};
    
    private ComboBox<String> year1Combo;
    private ComboBox<String> year2Combo;
    private Button compareButton;
    private Label pollutantLabel;
    
    private Label year1AvgLabel;
    private Label year1MaxLabel;
    private Label year1MinLabel;
    private Label year1MedianLabel;
    private Label year1CountLabel;
    
    private Label year2AvgLabel;
    private Label year2MaxLabel;
    private Label year2MinLabel;
    private Label year2MedianLabel;
    private Label year2CountLabel;
    
    private Label avgChangeLabel;
    private Label maxChangeLabel;
    private Label minChangeLabel;
    
    private VBox chartContainer;
    
    private PollutionFilter filter;
    private ChartBuilder chartBuilder;
    
    private String currentPollutant = "no2";
    
    /**
     * Construct the ComparisonPanel inside a ScrollPane.
     * 
     * @param filter The PollutionFilter for accessing data.
     */
    public ComparisonPanel(PollutionFilter filter)
    {
        this.filter = filter;
        this.chartBuilder = new ChartBuilder();
        
        VBox content = new VBox(14);
        content.setPadding(new Insets(20, 24, 20, 24));
        content.setStyle("-fx-background-color: #1a1a2e;");
        
        content.getChildren().addAll(
            buildTitle(),
            buildPollutantRow(),
            buildYearSelectors(),
            buildStatsRow(),
            buildChangeRow(),
            buildChartContainer()
        );
        
        setContent(content);
        setFitToWidth(true);
        setStyle("-fx-background: #1a1a2e; -fx-background-color: #1a1a2e;");
        setHbarPolicy(ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
    }
    
    /**
     * Update the current pollutant and rerun the comparison.
     * Called from MainController when the pollutant selection changes.
     * 
     * @param pollutant The selected pollutant type.
     * @param year Not used directly here, but matches the refresh signature.
     */
    public void refresh(String pollutant, String year)
    {
        currentPollutant = pollutant;
        pollutantLabel.setText("Current pollutant: " + pollutant.toUpperCase());
        runComparison();
    }
    
    /**
     * Builds the panel title.
     * 
     * @return A styled title Label.
     */
    private Label buildTitle()
    {
        Label title = new Label("Year-to-Year Comparison");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setStyle("-fx-text-fill: #e0e0e0;");
        return title;
    }
    
    /**
     * Builds a row showing which pollutant is currently selected.
     * 
     * @return An HBox with the pollutant label.
     */
    private HBox buildPollutantRow()
    {
        pollutantLabel = new Label("Current pollutant: NO2");
        pollutantLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        pollutantLabel.setStyle("-fx-text-fill: #9090d0;");
        
        HBox row = new HBox(pollutantLabel);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }
    
    /**
     * Builds the row with two year combo boxes and a compare button.
     * 
     * @return An HBox with the year selectors and compare button.
     */
    private HBox buildYearSelectors()
    {
        Label lbl1 = styledLabel("Year 1:", 13, false);
        year1Combo = buildComboBox("2018");
        
        Label lbl2 = styledLabel("Year 2:", 13, false);
        year2Combo = buildComboBox("2023");
        
        compareButton = new Button("Compare");
        compareButton.setStyle(
            "-fx-background-color: #3a3a7c;" +
            "-fx-text-fill: #e0e0e0;" +
            "-fx-background-radius: 4;" +
            "-fx-cursor: hand;"
        );
        compareButton.setOnAction(e -> runComparison());
        
        HBox row = new HBox(12, lbl1, year1Combo, lbl2, year2Combo, compareButton);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }
    
    /**
     * Builds the side-by-side stats display for both years.
     * Uses horizontal card rows so they fit without needing to scroll too far.
     * 
     * @return A VBox with Year 1 cards on top and Year 2 cards below.
     */
    private VBox buildStatsRow()
    {
        year1AvgLabel = styledLabel("–", 16, true);
        year1MaxLabel = styledLabel("–", 16, true);
        year1MinLabel = styledLabel("–", 16, true);
        year1MedianLabel = styledLabel("–", 16, true);
        year1CountLabel = styledLabel("–", 16, true);
        
        Label year1Title = styledLabel("Year 1 Statistics", 14, true);
        year1Title.setStyle("-fx-text-fill: #9090d0;");
        
        HBox year1Cards = new HBox(8,
            buildCard("Average", year1AvgLabel),
            buildCard("Maximum", year1MaxLabel),
            buildCard("Minimum", year1MinLabel),
            buildCard("Median", year1MedianLabel),
            buildCard("Data Points", year1CountLabel)
        );
        year1Cards.setAlignment(Pos.CENTER_LEFT);
        
        year2AvgLabel = styledLabel("–", 16, true);
        year2MaxLabel = styledLabel("–", 16, true);
        year2MinLabel = styledLabel("–", 16, true);
        year2MedianLabel = styledLabel("–", 16, true);
        year2CountLabel = styledLabel("–", 16, true);
        
        Label year2Title = styledLabel("Year 2 Statistics", 14, true);
        year2Title.setStyle("-fx-text-fill: #9090d0;");
        
        HBox year2Cards = new HBox(8,
            buildCard("Average", year2AvgLabel),
            buildCard("Maximum", year2MaxLabel),
            buildCard("Minimum", year2MinLabel),
            buildCard("Median", year2MedianLabel),
            buildCard("Data Points", year2CountLabel)
        );
        year2Cards.setAlignment(Pos.CENTER_LEFT);
        
        VBox statsBox = new VBox(10, year1Title, year1Cards, year2Title, year2Cards);
        return statsBox;
    }
    
    /**
     * Builds the row that shows percentage changes between the two years.
     * 
     * @return An HBox with change labels.
     */
    private HBox buildChangeRow()
    {
        avgChangeLabel = styledLabel("Avg change: –", 13, false);
        maxChangeLabel = styledLabel("Max change: –", 13, false);
        minChangeLabel = styledLabel("Min change: –", 13, false);
        
        HBox row = new HBox(20, avgChangeLabel, maxChangeLabel, minChangeLabel);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(4, 0, 4, 0));
        return row;
    }
    
    /**
     * Builds the container for the comparison bar chart.
     * 
     * @return A VBox to hold the chart.
     */
    private VBox buildChartContainer()
    {
        chartContainer = new VBox();
        chartContainer.setStyle("-fx-background-color: #12122a; -fx-background-radius: 6;");
        chartContainer.setMinHeight(350);
        return chartContainer;
    }
    
    /**
     * Runs the comparison between the two selected years.
     * Updates all stat labels, change labels, and the bar chart.
     */
    private void runComparison()
    {
        String year1 = year1Combo.getValue();
        String year2 = year2Combo.getValue();
        
        List<DataPoint> data1 = filter.getFilteredData(currentPollutant, year1);
        List<DataPoint> data2 = filter.getFilteredData(currentPollutant, year2);
        
        if (data1.isEmpty()) {
            year1AvgLabel.setText("–");
            year1MaxLabel.setText("–");
            year1MinLabel.setText("–");
            year1MedianLabel.setText("–");
            year1CountLabel.setText("–");
        } else {
            year1AvgLabel.setText(String.format("%.2f µg/m³", StatisticsCalc.average(data1)));
            year1MaxLabel.setText(String.format("%.2f µg/m³", StatisticsCalc.max(data1)));
            year1MinLabel.setText(String.format("%.2f µg/m³", StatisticsCalc.min(data1)));
            year1MedianLabel.setText(String.format("%.2f µg/m³", StatisticsCalc.median(data1)));
            year1CountLabel.setText(String.valueOf(StatisticsCalc.countValid(data1)));
        }
        
        if (data2.isEmpty()) {
            year2AvgLabel.setText("–");
            year2MaxLabel.setText("–");
            year2MinLabel.setText("–");
            year2MedianLabel.setText("–");
            year2CountLabel.setText("–");
        } else {
            year2AvgLabel.setText(String.format("%.2f µg/m³", StatisticsCalc.average(data2)));
            year2MaxLabel.setText(String.format("%.2f µg/m³", StatisticsCalc.max(data2)));
            year2MinLabel.setText(String.format("%.2f µg/m³", StatisticsCalc.min(data2)));
            year2MedianLabel.setText(String.format("%.2f µg/m³", StatisticsCalc.median(data2)));
            year2CountLabel.setText(String.valueOf(StatisticsCalc.countValid(data2)));
        }
        
        if (!data1.isEmpty() && !data2.isEmpty()) {
            double avg1 = StatisticsCalc.average(data1);
            double avg2 = StatisticsCalc.average(data2);
            double max1 = StatisticsCalc.max(data1);
            double max2 = StatisticsCalc.max(data2);
            double min1 = StatisticsCalc.min(data1);
            double min2 = StatisticsCalc.min(data2);
            
            avgChangeLabel.setText("Avg change: " + formatChange(avg1, avg2));
            maxChangeLabel.setText("Max change: " + formatChange(max1, max2));
            minChangeLabel.setText("Min change: " + formatChange(min1, min2));
            
            setChangeColour(avgChangeLabel, avg1, avg2);
            setChangeColour(maxChangeLabel, max1, max2);
            setChangeColour(minChangeLabel, min1, min2);
        } else {
            avgChangeLabel.setText("Avg change: –");
            avgChangeLabel.setStyle("-fx-text-fill: #e0e0e0;");
            maxChangeLabel.setText("Max change: –");
            maxChangeLabel.setStyle("-fx-text-fill: #e0e0e0;");
            minChangeLabel.setText("Min change: –");
            minChangeLabel.setStyle("-fx-text-fill: #e0e0e0;");
        }
        
        chartContainer.getChildren().clear();
        if (!data1.isEmpty() && !data2.isEmpty()) {
            BarChart<String, Number> chart = chartBuilder.buildTwoYearComparisonChart(
                data1, data2, year1, year2, currentPollutant);
            chartContainer.getChildren().add(chart);
        } else {
            Label noData = styledLabel("No data available for the selected combination.", 14, false);
            noData.setStyle("-fx-text-fill: #606080;");
            chartContainer.getChildren().add(noData);
        }
    }
    
    /**
     * Format a percentage change string between two values.
     * 
     * @param oldVal The value from the earlier year.
     * @param newVal The value from the later year.
     * @return A formatted string like "+5.2%" or "-3.1%".
     */
    private String formatChange(double oldVal, double newVal)
    {
        if (oldVal <= 0) {
            return "N/A";
        }
        double change = ((newVal - oldVal) / oldVal) * 100.0;
        if (change >= 0) {
            return String.format("+%.1f%%", change);
        } else {
            return String.format("%.1f%%", change);
        }
    }
    
    /**
     * Set the text colour of a change label based on whether pollution
     * increased (red) or decreased (green).
     * 
     * @param label The label to colour.
     * @param oldVal The earlier value.
     * @param newVal The later value.
     */
    private void setChangeColour(Label label, double oldVal, double newVal)
    {
        if (newVal < oldVal) {
            label.setStyle("-fx-text-fill: #38a800;");
        } else if (newVal > oldVal) {
            label.setStyle("-fx-text-fill: #c80000;");
        } else {
            label.setStyle("-fx-text-fill: #a0a0c0;");
        }
    }
    
    /**
     * Build a styled ComboBox for year selection.
     * 
     * @param defaultYear The initially selected year.
     * @return A configured ComboBox.
     */
    private ComboBox<String> buildComboBox(String defaultYear)
    {
        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().addAll(YEARS);
        combo.setValue(defaultYear);
        combo.setPrefWidth(90);
        combo.setStyle(
            "-fx-background-color: #22223a;" +
            "-fx-text-fill: #e0e0e0;" +
            "-fx-border-color: #3a3a5c;" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;"
        );
        return combo;
    }
    
    /**
     * Build a styled statistic card.
     * 
     * @param title The card heading.
     * @param valueLabel The label that displays the value.
     * @return A styled VBox card.
     */
    private VBox buildCard(String title, Label valueLabel)
    {
        Label titleLbl = styledLabel(title, 11, false);
        titleLbl.setStyle("-fx-text-fill: #8080a0;");
        
        VBox card = new VBox(4, titleLbl, valueLabel);
        card.setPadding(new Insets(8, 12, 8, 12));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle(
            "-fx-background-color: #22223a;" +
            "-fx-background-radius: 6;" +
            "-fx-border-color: #3a3a5c;" +
            "-fx-border-radius: 6;" +
            "-fx-border-width: 1;"
        );
        card.setPrefWidth(140);
        return card;
    }
    
    /**
     * Create a styled Label.
     * 
     * @param text The label text.
     * @param size Font size.
     * @param bold Whether to use bold font weight.
     * @return A styled Label.
     */
    private Label styledLabel(String text, int size, boolean bold)
    {
        Label label = new Label(text);
        FontWeight weight;
        if (bold) {
            weight = FontWeight.BOLD;
        } else {
            weight = FontWeight.NORMAL;
        }
        label.setFont(Font.font("Arial", weight, size));
        label.setStyle("-fx-text-fill: #e0e0e0;");
        return label;
    }
}
