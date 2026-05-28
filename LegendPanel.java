import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * LegendPanel displays a colour legend for the pollution map.
 *
 * It shows a green-to-amber-to-red gradient bar with threshold labels
 * specific to the currently selected pollutant. The thresholds shown
 * match those used by ColourMapper:
 *
 *   NO2:   green < 25 µg/m³  |  amber 25-40  |  red > 40
 *   PM10:  green < 20 µg/m³  |  amber 20-35  |  red > 35
 *   PM2.5: green < 10 µg/m³  |  amber 10-20  |  red > 20
 *
 * Call refresh(pollutant) whenever the user changes the pollutant
 * selection so the threshold labels update accordingly.
 *
 * @author Elif Deniz Turkmen
 * @version 1.0
 */
public class LegendPanel extends VBox
{
    private static final int BAR_WIDTH  = 200;
    private static final int BAR_HEIGHT = 16;

    private Label titleLabel;
    private Label lowLabel;
    private Label midLabel;
    private Label highLabel;
    private Canvas gradientBar;

    /**
     * Construct the LegendPanel with a default NO2 legend.
     */
    public LegendPanel()
    {
        super(4);
        setPadding(new Insets(10, 14, 10, 14));
        setAlignment(Pos.CENTER_LEFT);
        setStyle(
            "-fx-background-color: #1a1a2e;" +
            "-fx-border-color: #3a3a5c;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;"
        );

        titleLabel = buildLabel("Pollution Level", 12, true);

        gradientBar = new Canvas(BAR_WIDTH, BAR_HEIGHT);
        drawGradientBar();

        HBox thresholdRow = buildThresholdRow();

        getChildren().addAll(titleLabel, gradientBar, thresholdRow);

        // default
        refresh("no2");
    }

    /**
     * Update the threshold labels for the given pollutant.
     * Call this whenever the user changes the pollutant selection.
     *
     * @param pollutant The pollutant type: "no2", "pm10", or "pm2.5"
     */
    public void refresh(String pollutant)
    {
        double[] thresholds = getThresholds(pollutant);
        lowLabel.setText("0");
        midLabel.setText(String.valueOf((int) thresholds[0]));
        highLabel.setText((int) thresholds[1] + "+ µg/m³");
    }

    /**
     * Draw the green-amber-red gradient bar onto the canvas.
     */
    private void drawGradientBar()
    {
        GraphicsContext gc = gradientBar.getGraphicsContext2D();

        // Build gradient: green -> amber -> red
        LinearGradient gradient = new LinearGradient(
            0, 0, BAR_WIDTH, 0, false, CycleMethod.NO_CYCLE,
            new Stop(0.0,  Color.rgb(56, 168,  0)),
            new Stop(0.5,  Color.rgb(255, 165, 0)),
            new Stop(1.0,  Color.rgb(200,   0, 0))
        );

        gc.setFill(gradient);
        gc.fillRoundRect(0, 0, BAR_WIDTH, BAR_HEIGHT, 4, 4);
    }

    /**
     * Build the row of threshold labels shown below the gradient bar.
     *
     * @return An HBox with low, mid and high labels.
     */
    private HBox buildThresholdRow()
    {
        lowLabel  = buildLabel("0",  10, false);
        midLabel  = buildLabel("25", 10, false);
        highLabel = buildLabel("40+ µg/m³", 10, false);

        // space between labels to align with the bar
        javafx.scene.layout.Region spacer1 = new javafx.scene.layout.Region();
        javafx.scene.layout.Region spacer2 = new javafx.scene.layout.Region();
        javafx.scene.layout.HBox.setHgrow(spacer1, javafx.scene.layout.Priority.ALWAYS);
        javafx.scene.layout.HBox.setHgrow(spacer2, javafx.scene.layout.Priority.ALWAYS);

        HBox row = new HBox(spacer1, midLabel, spacer2);
        row.getChildren().add(0, lowLabel);
        row.getChildren().add(highLabel);
        row.setPrefWidth(BAR_WIDTH);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    /**
     * Return the low and high thresholds for the given pollutant.
     * These match the thresholds used in ColourMapper.
     *
     * @param pollutant The pollutant type string (case-insensitive)
     * @return A double array { lowThreshold, highThreshold }
     */
    private double[] getThresholds(String pollutant)
    {
        if (pollutant == null) {
            return new double[]{ 10.0, 25.0 };
        }
        switch (pollutant.toLowerCase()) {
            case "no2":   return new double[]{ 25.0, 40.0 };
            case "pm10":  return new double[]{ 20.0, 35.0 };
            case "pm2.5": return new double[]{ 10.0, 20.0 };
            default:      return new double[]{ 10.0, 25.0 };
        }
    }

    /**
     * Helper to create a styled Label.
     *
     * @param text  The label text.
     * @param size  Font size in points.
     * @param bold  Whether to use bold weight.
     * @return A configured Label.
     */
    private Label buildLabel(String text, int size, boolean bold)
    {
        Label label = new Label(text);
        label.setFont(Font.font("Arial",
                bold ? FontWeight.BOLD : FontWeight.NORMAL, size));
        label.setStyle("-fx-text-fill: #c0c0d0;");
        return label;
    }
}