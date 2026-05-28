import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;
/**
 * PollutionMarker is a JavaFX Circle that represents one DataPoint on the
 * London pollution map.
 *
 * Each marker is colour-coded using ColourMapper and shows a tooltip on
 * hover with the grid code, coordinates, and pollution value.
 *
 * @author [Your Name]
 * @version 1.0
 */
public class PollutionMarker extends Circle
{
    private static final double MARKER_RADIUS  = 4.0;
    private static final double MARKER_OPACITY = 0.75;
 
    private DataPoint dataPoint;
 
    /**
     * Construct a PollutionMarker for the given DataPoint.
     *
     * @param dataPoint  The data point this marker represents
     * @param pixelX     The pixel x position on the map image
     * @param pixelY     The pixel y position on the map image
     * @param colour     The JavaFX Color to fill this marker (from ColourMapper)
     * @param pollutant  The pollutant type string, used in the tooltip (e.g. "NO2")
     * @param year       The year string, used in the tooltip (e.g. "2023")
     */
    public PollutionMarker(DataPoint dataPoint, double pixelX, double pixelY,
                           Color colour, String pollutant, String year)
    {
        super(pixelX, pixelY, MARKER_RADIUS);
 
        this.dataPoint = dataPoint;
 
        // Visual styling
        setFill(colour);
        setStroke(colour.darker());
        setStrokeWidth(0.5);
        setOpacity(MARKER_OPACITY);
 
        // Tooltip shown on hover
        Tooltip tooltip = new Tooltip(buildTooltipText(pollutant, year));
        tooltip.setShowDelay(Duration.millis(100));
        tooltip.setStyle("-fx-font-size: 11px;");
        Tooltip.install(this, tooltip);
 
        // Highlight on mouse enter / restore on exit
        setOnMouseEntered(e -> {
            setRadius(MARKER_RADIUS * 1.6);
            setOpacity(1.0);
            toFront();
        });
        setOnMouseExited(e -> {
            setRadius(MARKER_RADIUS);
            setOpacity(MARKER_OPACITY);
        });
    }
 
    /**
     * Return the DataPoint associated with this marker.
     *
     * @return The DataPoint this marker represents
     */
    public DataPoint getDataPoint()
    {
        return dataPoint;
    }
 
    /**
     * Build the tooltip text string for this marker.
     *
     * @param pollutant The pollutant type label
     * @param year      The year label
     * @return A formatted multi-line tooltip string
     */
    private String buildTooltipText(String pollutant, String year)
    {
        return String.format(
            "Pollutant : %s (%s)\n" +
            "Grid Code : %d\n"      +
            "Easting   : %d\n"      +
            "Northing  : %d\n"      +
            "Value     : %.2f µg/m³",
            pollutant.toUpperCase(), year,
            dataPoint.gridCode(),
            dataPoint.x(),
            dataPoint.y(),
            dataPoint.value()
        );
    }
}