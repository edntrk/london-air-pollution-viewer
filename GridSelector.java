import java.util.List;

/**
 * GridSelector translates a mouse click position on the MapPanel into
 * the nearest DataPoint from the current dataset.
 *
 * When the user clicks anywhere on the map, the raw pixel coordinates
 * of the click are compared against the pixel positions of every visible
 * PollutionMarker. GridSelector finds the marker whose centre is closest
 * to the click position (using Euclidean distance) and returns the
 * corresponding DataPoint.
 *
 * A configurable snap radius (default 20 px) prevents spurious selections
 * when the user clicks in empty space: if no marker centre falls within
 * the snap radius, null is returned instead.
 *
 * Usage:
 *   GridSelector selector = new GridSelector(mapPanel);
 *   mapPanel.setOnMouseClicked(e -> {
 *       DataPoint dp = selector.findNearest(e.getX(), e.getY());
 *       if (dp != null) { ... }
 *   });
 *
 * @author Elif Deniz Turkmen
 * @version 1.0
 */
public class GridSelector
{
    /** Maximum pixel distance within which a click is snapped to a marker. */
    private static final double DEFAULT_SNAP_RADIUS = 20.0;

    private MapPanel mapPanel;
    private double   snapRadius;

    /**
     * Construct a GridSelector with the default snap radius.
     *
     * @param mapPanel The MapPanel whose markers will be searched.
     * @throws IllegalArgumentException if mapPanel is null.
     */
    public GridSelector(MapPanel mapPanel)
    {
        this(mapPanel, DEFAULT_SNAP_RADIUS);
    }

    /**
     * Construct a GridSelector with a custom snap radius.
     *
     * @param mapPanel   The MapPanel whose markers will be searched.
     * @param snapRadius Maximum pixel distance for a click to register.
     * @throws IllegalArgumentException if mapPanel is null or snapRadius <= 0.
     */
    public GridSelector(MapPanel mapPanel, double snapRadius)
    {
        if (mapPanel == null)
        {
            throw new IllegalArgumentException("mapPanel must not be null.");
        }
        if (snapRadius <= 0)
        {
            throw new IllegalArgumentException("snapRadius must be positive.");
        }
        this.mapPanel   = mapPanel;
        this.snapRadius = snapRadius;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Public API
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Find the DataPoint whose marker is closest to the given pixel position.
     * Returns null if no marker falls within the snap radius.
     *
     * @param clickX The x pixel coordinate of the mouse click on the MapPanel.
     * @param clickY The y pixel coordinate of the mouse click on the MapPanel.
     * @return The nearest DataPoint, or null if none is close enough.
     */
    public DataPoint findNearest(double clickX, double clickY)
    {
        PollutionMarker nearest  = null;
        double          bestDist = Double.MAX_VALUE;

        for (javafx.scene.Node node : mapPanel.getChildren())
        {
            if (!(node instanceof PollutionMarker))
            {
                continue;
            }
            PollutionMarker marker = (PollutionMarker) node;
            double dist = distance(clickX, clickY,
                                   marker.getCenterX(), marker.getCenterY());
            if (dist < bestDist)
            {
                bestDist = dist;
                nearest  = marker;
            }
        }

        // Only return a result if the nearest marker is within the snap radius
        if (nearest != null && bestDist <= snapRadius)
        {
            return nearest.getDataPoint();
        }
        return null;
    }

    /**
     * Return the current snap radius in pixels.
     *
     * @return The snap radius.
     */
    public double getSnapRadius()
    {
        return snapRadius;
    }

    /**
     * Update the snap radius.
     *
     * @param snapRadius The new snap radius in pixels (must be positive).
     * @throws IllegalArgumentException if snapRadius <= 0.
     */
    public void setSnapRadius(double snapRadius)
    {
        if (snapRadius <= 0)
        {
            throw new IllegalArgumentException("snapRadius must be positive.");
        }
        this.snapRadius = snapRadius;
    }

    /**
     * Calculate the Euclidean distance between two points.
     *
     * @param x1 X coordinate of the first point.
     * @param y1 Y coordinate of the first point.
     * @param x2 X coordinate of the second point.
     * @param y2 Y coordinate of the second point.
     * @return The Euclidean distance.
     */
    private double distance(double x1, double y1, double x2, double y2)
    {
        double dx = x1 - x2;
        double dy = y1 - y2;
        return Math.sqrt(dx * dx + dy * dy);
    }
}