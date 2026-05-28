import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Insets;
 
import java.io.InputStream;
import java.util.List;

/**
 * MapPanel is a JavaFX Pane that displays a map image and overlays
 * PollutionMarker shapes for every data point in the current selection.
 *
 * Each region has its own map image file (London.png, Manchester.png, etc).
 * When the region changes, MapPanel attempts to load the matching image.
 * If the image file is not found, markers are shown on a plain background
 * with a region label.
 *
 * @author [Your Name]
 * @version 3.0
 */
public class MapPanel extends Pane
{
    private ImageView mapImageView;
    private ColourMapper colourMapper;
    private Label regionLabel;
    
    // Current region bounds (defaults to London)
    private int regionMinX = 510394;
    private int regionMaxX = 553297;
    private int regionMinY = 168504;
    private int regionMaxY = 193305;
    
    // Current region name
    private String currentRegion = "London";
    
    // Whether the current region has a map image loaded
    private boolean hasMapImage = false;
 
    /**
     * Construct the MapPanel. Loads the default London map image and sets
     * up the colour mapper.
     */
    public MapPanel()
    {
        // Create the ImageView (starts empty, loaded in setRegionBounds)
        mapImageView = new ImageView();
        mapImageView.setPreserveRatio(true);
        mapImageView.setSmooth(true);
        mapImageView.fitWidthProperty().bind(widthProperty());
        mapImageView.fitHeightProperty().bind(heightProperty());
        getChildren().add(mapImageView);
        
        // Region label (shown when no map image is available)
        regionLabel = new Label("");
        regionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        regionLabel.setStyle("-fx-text-fill: #606080;");
        regionLabel.setLayoutX(10);
        regionLabel.setLayoutY(10);
        regionLabel.setVisible(false);
        getChildren().add(regionLabel);
 
        colourMapper = new ColourMapper();
 
        setStyle("-fx-border-color: #555; -fx-border-width: 1; -fx-background-color: #12122a;");
        setPadding(new Insets(0));
        
        // Load the default London map
        loadRegionImage("London.png");
    }
    
    /**
     * Set the coordinate bounds, region name, and load the map image
     * for the new region. This should be called before refresh().
     * 
     * @param minX Minimum Easting (left bound)
     * @param maxX Maximum Easting (right bound)
     * @param minY Minimum Northing (bottom bound)
     * @param maxY Maximum Northing (top bound)
     * @param region The region name string
     */
    public void setRegionBounds(int minX, int maxX, int minY, int maxY, String region)
    {
        this.regionMinX = minX;
        this.regionMaxX = maxX;
        this.regionMinY = minY;
        this.regionMaxY = maxY;
        this.currentRegion = region;
        
        // Try to load the map image for this region
        String imageFile = region + ".png";
        loadRegionImage(imageFile);
    }
    
    /**
     * Try to load a map image file from the classpath.
     * If the file is found, it is displayed in the ImageView.
     * If not found, the ImageView is hidden and a region label is shown.
     * 
     * @param imageFile The filename to load (e.g. "Manchester.png")
     */
    private void loadRegionImage(String imageFile)
    {
        InputStream stream = getClass().getResourceAsStream(imageFile);
        
        if (stream != null) {
            // Image found - load and display it
            Image mapImage = new Image(stream);
            mapImageView.setImage(mapImage);
            mapImageView.setVisible(true);
            regionLabel.setVisible(false);
            hasMapImage = true;
        } else {
            // Image not found - show plain background with label
            mapImageView.setVisible(false);
            regionLabel.setText(currentRegion + " Region (no map image found)");
            regionLabel.setVisible(true);
            hasMapImage = false;
        }
    }
 
    /**
     * Refresh the map display with a new set of data points.
     * Removes all existing markers and redraws using the supplied list.
     *
     * @param dataPoints The list of DataPoints to display (should be
     *                   pre-filtered by PollutionFilter)
     * @param pollutant  The pollutant type string (e.g. "no2"), passed to
     *                   ColourMapper for threshold selection and to markers
     *                   for tooltip labelling
     * @param year       The year string (e.g. "2023"), used in marker tooltips
     */
    public void refresh(List<DataPoint> dataPoints, String pollutant, String year)
    {
        // Remove old markers (keep ImageView at index 0 and regionLabel at index 1)
        if (getChildren().size() > 2) {
            getChildren().remove(2, getChildren().size());
        }
 
        if (dataPoints == null || dataPoints.isEmpty()) {
            return;
        }
 
        // Work out the pixel dimensions to map coordinates onto
        int renderW;
        int renderH;
        
        if (hasMapImage && mapImageView.getImage() != null) {
            // Use the displayed image dimensions
            renderW = (int) Math.max(mapImageView.getFitWidth(), mapImageView.getImage().getWidth());
            renderH = (int) Math.max(mapImageView.getFitHeight(), mapImageView.getImage().getHeight());
            
            double boundsW = mapImageView.getBoundsInParent().getWidth();
            double boundsH = mapImageView.getBoundsInParent().getHeight();
            if (boundsW > 0) renderW = (int) boundsW;
            if (boundsH > 0) renderH = (int) boundsH;
        } else {
            // No map image - use the pane dimensions
            renderW = (int) getWidth();
            renderH = (int) getHeight();
            if (renderW <= 0) renderW = 800;
            if (renderH <= 0) renderH = 600;
        }
 
        // Create a mapper with the current region's coordinate bounds
        MapCoordMapper currentMapper = new MapCoordMapper(
            renderW, renderH, regionMinX, regionMaxX, regionMinY, regionMaxY);
 
        for (DataPoint dp : dataPoints) {
            if (dp.value() < 0) {
                continue;  // skip invalid data
            }
 
            int px = currentMapper.toPixelX(dp.x());
            int py = currentMapper.toPixelY(dp.y());
 
            if (px < 0 || py < 0) {
                continue;  // point outside map bounds
            }
 
            Color colour = colourMapper.getColour(dp.value(), pollutant);
            PollutionMarker marker = new PollutionMarker(dp, px, py, colour, pollutant, year);
            getChildren().add(marker);
        }
    }
}