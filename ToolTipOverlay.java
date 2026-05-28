import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
/**
 * ToolTipOverlay is a persistent info bar displayed below the MapPanel.
 * 
 * When the user clicks a PollutionMarker on the map, MainController calls
 * show(DataPoint, pollutant, year) and this bar updates to display the full
 * details of the selected data point.
 *
 * @author Begum Zeytun
 * @version 1.0
 */
public class ToolTipOverlay extends HBox
{
    // UI elements that are updated on each show() call
    private Circle colourSwatch;
    private Label pollutantYearLabel;
    private Label gridCodeLabel;
    private Label eastingLabel;
    private Label northingLabel;
    private Label valueLabel;
    
    // prompt shown before any selection
    private HBox promptBox;
    private HBox detailBox;
    
    private ColourMapper colourMapper;

    /**
     * Construct the TooltipOverlay in its default "no selection" state.
     */
    public ToolTipOverlay()
    {
        super(0);
        colourMapper = new ColourMapper();
        
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(8, 16, 8, 16));
        setMinHeight(44);
        setMaxHeight(44);
        setStyle(
            "-fx-background-color: #12122a;" +
            "-fx-border-color: #3a3a5c;" +
            "-fx-border-width: 1 0 0 0;"
            );
            promptBox = buildPromptBox();
            detailBox = buildDetailBox();
            getChildren().add(promptBox);
    }

    /**
     * Updates the overlay to show details for the given DataPoint.
     * Switches from the prompt view to the detail view.
     *
     * @param dp The clicked DataPoint.
     * @param pollutant The pollutant type string.
     * @param year The year string.
     */
    public void show(DataPoint dp, String pollutant, String year)
    {
        if (dp == null) 
        {
            reset();
            return;
        }
        Color colour = colourMapper.getColour(dp.value(), pollutant);
        colourSwatch.setFill(colour);
        
        pollutantYearLabel.setText(pollutant.toUpperCase() + "  ·  " + year);
        gridCodeLabel.setText("Grid: " + dp.gridCode());
        eastingLabel.setText("E: " + dp.x());
        northingLabel.setText("N: " + dp.y());
        valueLabel.setText(String.format("%.2f µg/m³", dp.value()));
        getChildren().setAll(detailBox);
    }
    
    /**
     * Reset the overlay back to the default prompt state.
     * Called when the selection is cleared.
     */
    public void reset()
    {
        getChildren().setAll(promptBox);
    }
    
    /**
     * Builds the prompt box shown before any marker is clicked.
     * 
     * @return An HBox with the prompt message.
     */
    private HBox buildPromptBox()
    {
        Label prompt = new Label("Click a marker on the map to see its details here.");
        prompt.setFont(Font.font("Arial", 13));
        prompt.setStyle("-fx-text-fill: #606080;");
        
        HBox box = new HBox(prompt);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }
    
    /**
     * Builds the detail box that displays the selected data point information.
     * All value labels are stored as fields so show() can update them.
     * 
     * @return An HBox with all detail labels.
     */
    private HBox buildDetailBox()
    {
        // colour swatch circle
        colourSwatch = new Circle(8);
        colourSwatch.setStroke(Color.gray(0.5));
        colourSwatch.setStrokeWidth(1);
        
        // labels
        pollutantYearLabel = boldLabel("–", 13);
        gridCodeLabel = thinLabel("–", 13);
        eastingLabel = thinLabel("–", 13);
        northingLabel = thinLabel("–", 13);
        valueLabel = boldLabel("–", 14);
        valueLabel.setStyle("-fx-text-fill: #e0e0a0;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        HBox box = new HBox(14,
            colourSwatch,
            pollutantYearLabel,
            separator(),
            gridCodeLabel,
            separator(),
            eastingLabel,
            separator(),
            northingLabel,
            spacer,
            valueLabel
            );
            box.setAlignment(Pos.CENTER_LEFT);
            return box;
    }
    
    /**
     * Builds a thin vertical separator label used between detail fields.
     * 
     * @return A styled separator Label.
     */
    private Label separator()
    {
        Label sep = new Label("|");
        sep.setStyle("-fx-text-fill: #3a3a5c;");
        return sep;
    }
    
    /**
     * Creates a bold styled Label.
     * 
     * @param text The label text.
     * @param size Font size.
     * @return A bold Label.
     */
    private Label boldLabel(String text, int size)
    {
        Label l = new Label(text);
        l.setFont(Font.font("Arial", FontWeight.BOLD, size));
        l.setStyle("-fx-text-fill: #e0e0e0;");
        return l;
    }
    
    /**
     * Creates a normal weight styled Label.
     * 
     * @param text The label text.
     * @param size Font size.
     * @return A normal weight Label.
     */
    private Label thinLabel(String text, int size)
    {
        Label l = new Label(text);
        l.setFont(Font.font("Arial", FontWeight.NORMAL, size));
        l.setStyle("-fx-text-fill: #a0a0c0;");
        return l;
    }
}