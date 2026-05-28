import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.List;
/**
 * GridPanel displays the individual DataPoint records for the current pollutant
 * and year selection in a searchable table.
 * 
 * The Panel also provides a highlight() method so that clicking a PollutionMarker
 * on the map scrolls to and selects the matching row.
 *
 * @author Begum Zeytun
 * @version 1.0
 */
public class GridPanel extends VBox
{
    private TableView<DataPoint> table;
    private ObservableList<DataPoint> allRows;
    private TextField searchField;
    
    /**
     * Construct an empty GridPanel.
     * Call refresh() to populate it.
     */
    public GridPanel()
    {
        super(10);
        setPadding(new Insets(16, 20, 16, 20));
        setStyle("-fx-background-color: #1a1a2e;");
        allRows = FXCollections.observableArrayList();
        
        getChildren().addAll(
            buildHeader(),
            buildSearchBar(),
            buildTable()
            );
            VBox.setVgrow(table, Priority.ALWAYS);
    }

    /**
     * Populate the table with a new list of data points.
     *
     * @param dataPoints The filtered list of DataPoints to display.
     */
    public void refresh(List<DataPoint> dataPoints)
    {
        allRows.setAll(dataPoints);
        searchField.clear();
        table.setItems(allRows);
    }
    
    /**
     * Scroll and select the row matching the given DataPoint.
     * 
     * @param dp The DataPoint to highlight in the table.
     */
    public void highlight(DataPoint dp)
    {
        if(dp == null)
        {
            return;
        }
        for(int i = 0; i < table.getItems().size(); i++) 
        {
            if (table.getItems().get(i).gridCode() == dp.gridCode())
            {
                table.getSelectionModel().select(i);
                table.scrollTo(i);
                return;
            }
        }
    }
    
    /**
     * Build the panel title label.
     * 
     * @return A styled header Label.
     */
    private Label buildHeader()
    {
        Label title = new Label("Grid Data");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setStyle("-fx-text-fill: #e0e0e0;");
        return title;
    }
    
    /**
     * Build the search/filter bar that narrows the table rows by grid code.
     * 
     * @return An HBox containing a label 
     */
    private HBox buildSearchBar()
    {
        Label lbl = new Label("Filter by grid code:");
        lbl.setFont(Font.font("Arial", 13));
        lbl.setStyle("-fx-text-fill: #a0a0c0;");
        
        searchField = new TextField();
        searchField.setPromptText("e.g. 123456");
        searchField.setStyle(
            "-fx-background-color: #22223a;" +
            "-fx-text-fill: #e0e0e0;" +
            "-fx-prompt-text-fill: #606080;" +
            "-fx-border-color: #3a3a5c;" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;"
            );
            searchField.setPrefWidth(180);
            
            // live filter: updates table as the user types
             searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilter(newVal));
             
             HBox bar = new HBox(10, lbl, searchField);
             bar.setAlignment(Pos.CENTER_LEFT);
             return bar;
    }
    
    /**
     * Builds the TableView with four columns(Grid Code, Easting, Northing, Value).
     * 
     * @return The configured TableView.
     */
    private TableView<DataPoint> buildTable()
    {
        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setStyle(
            "-fx-background-color: #12122a;" +
            "-fx-table-cell-border-color: #2a2a4a;" +
            "-fx-text-fill: #cccccc;"
            );
            table.setPlaceholder(styledLabel("No data loaded.", 13));
            
             table.getColumns().addAll(
             buildIntColumn("Grid Code", 120, dp -> new SimpleIntegerProperty(dp.gridCode()).asObject()),
             buildIntColumn("Easting",   100, dp -> new SimpleIntegerProperty(dp.x()).asObject()),
             buildIntColumn("Northing",  100, dp -> new SimpleIntegerProperty(dp.y()).asObject()),
             buildDoubleColumn("Value (µg/m³)", 120, dp -> new SimpleDoubleProperty(dp.value()).asObject()) 
             );
             return table;
        
    }
    
    /**
     * Build an integer valued TableColumn with the given header and width.
     * 
     * @param header Column header text.
     * @param width Column width in pixels.
     * @param extractor A lambda that extracts the integer value from a DataPoint.
     * @return A configured TableColumn.
     */
    private TableColumn<DataPoint, Integer> buildIntColumn(
            String header, int width,
            javafx.util.Callback<DataPoint, javafx.beans.value.ObservableValue<Integer>> extractor)
    {
        TableColumn<DataPoint, Integer> col = new TableColumn<>(header);
        col.setCellValueFactory(cellData -> extractor.call(cellData.getValue()));
        col.setPrefWidth(width);
        col.setStyle("-fx-alignment: CENTER;");
        return col;
    }
    
    /**
     * Builds a double valued TableColumn for the pollution value.
     * 
     * @param header Column header text.
     * @param width Column width in pixels.
     * @param extractor A lambda that extracts the double value from a DataPoint.
     * @return A configured TableColumn.
     */
     private TableColumn<DataPoint, Double> buildDoubleColumn(
            String header, int width,
            javafx.util.Callback<DataPoint, javafx.beans.value.ObservableValue<Double>> extractor)
    {
        TableColumn<DataPoint, Double> col = new TableColumn<>(header);
        col.setCellValueFactory(cellData -> extractor.call(cellData.getValue()));
        col.setPrefWidth(width);
        col.setStyle("-fx-alignment: CENTER;");
        return col;
    }
    
    /**
     * Filter the table rows to those whose grid code contains the search text.
     * An empty or blank search string restores all rows.
     * 
     * @param query The text entered in the search field.
     */
    private void applyFilter(String query)
    {
        if (query == null) 
        {
            table.setItems(allRows);
            return;
        }
        if (query.isBlank()) 
        {
            table.setItems(allRows);
            return;
        }
        ObservableList<DataPoint> filtered = FXCollections.observableArrayList();
        for (DataPoint dp : allRows)
        {
            if (String.valueOf(dp.gridCode()).contains(query.trim()))
            {
                filtered.add(dp);
            }
        }
        table.setItems(filtered);
    }
    
    /**
     * Creates a simple styled label.
     * 
     * @param text The label text.
     * @param size Font size.
     * @return A styled Label.
     */
    private Label styledLabel(String text, int size)
    {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", size));
        label.setStyle("-fx-text-fill: #606080;");
        return label;
    }
}