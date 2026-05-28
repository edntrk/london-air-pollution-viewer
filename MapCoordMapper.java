/**
 * MapCoordMapper converts Ordnance Survey Easting/Northing coordinates
 * into pixel x,y positions on the map image.
 *
 * Now supports multiple UK regions. The bounds can be set dynamically
 * when the user switches regions. Linear interpolation is used to map
 * any point within the current bounds to a pixel coordinate.
 *
 * @author Yigit Karaibrahimoglu
 * @version 2.0
 */
public class MapCoordMapper
{
    // Current map bounds (defaults to London)
    private int mapMinEasting  = 510394;
    private int mapMaxEasting  = 553297;
    private int mapMinNorthing = 168504;  // bottom
    private int mapMaxNorthing = 193305;  // top
 
    private int imageWidth;
    private int imageHeight;
 
    /**
     * Construct a MapCoordMapper for a map image of the given dimensions.
     * Uses the default London bounds.
     *
     * @param imageWidth  The pixel width of the map image
     * @param imageHeight The pixel height of the map image
     * @throws IllegalArgumentException if width or height is not positive
     */
    public MapCoordMapper(int imageWidth, int imageHeight)
    {
        if (imageWidth <= 0 || imageHeight <= 0) {
            throw new IllegalArgumentException("Image dimensions must be positive.");
        }
        this.imageWidth  = imageWidth;
        this.imageHeight = imageHeight;
    }
    
    /**
     * Construct a MapCoordMapper with custom region bounds.
     * 
     * @param imageWidth  The pixel width of the map image
     * @param imageHeight The pixel height of the map image
     * @param minEasting  The minimum Easting value (left edge)
     * @param maxEasting  The maximum Easting value (right edge)
     * @param minNorthing The minimum Northing value (bottom edge)
     * @param maxNorthing The maximum Northing value (top edge)
     * @throws IllegalArgumentException if dimensions are not positive
     */
    public MapCoordMapper(int imageWidth, int imageHeight,
                          int minEasting, int maxEasting,
                          int minNorthing, int maxNorthing)
    {
        if (imageWidth <= 0 || imageHeight <= 0) {
            throw new IllegalArgumentException("Image dimensions must be positive.");
        }
        this.imageWidth    = imageWidth;
        this.imageHeight   = imageHeight;
        this.mapMinEasting  = minEasting;
        this.mapMaxEasting  = maxEasting;
        this.mapMinNorthing = minNorthing;
        this.mapMaxNorthing = maxNorthing;
    }
    
    /**
     * Update the map bounds to a new region.
     * Call this when the user switches to a different city/region.
     * 
     * @param minEasting  The minimum Easting value (left edge)
     * @param maxEasting  The maximum Easting value (right edge)
     * @param minNorthing The minimum Northing value (bottom edge)
     * @param maxNorthing The maximum Northing value (top edge)
     */
    public void setBounds(int minEasting, int maxEasting,
                          int minNorthing, int maxNorthing)
    {
        this.mapMinEasting  = minEasting;
        this.mapMaxEasting  = maxEasting;
        this.mapMinNorthing = minNorthing;
        this.mapMaxNorthing = maxNorthing;
    }
 
    /**
     * Convert an Easting coordinate to a pixel x position on the map image.
     * Returns -1 if the easting is outside the map bounds.
     *
     * @param easting The Ordnance Survey Easting value
     * @return The corresponding pixel x position, or -1 if out of bounds
     */
    public int toPixelX(int easting)
    {
        if (!isInHorizontalBounds(easting)) {
            return -1;
        }
        double ratio = (double)(easting - mapMinEasting) /
                       (double)(mapMaxEasting - mapMinEasting);
        return (int) Math.round(ratio * imageWidth);
    }
 
    /**
     * Convert a Northing coordinate to a pixel y position on the map image.
     * Note: Northing increases upward, but pixel y increases downward,
     * so the value is inverted.
     * Returns -1 if the northing is outside the map bounds.
     *
     * @param northing The Ordnance Survey Northing value
     * @return The corresponding pixel y position, or -1 if out of bounds
     */
    public int toPixelY(int northing)
    {
        if (!isInVerticalBounds(northing)) {
            return -1;
        }
        double ratio = (double)(mapMaxNorthing - northing) /
                       (double)(mapMaxNorthing - mapMinNorthing);
        return (int) Math.round(ratio * imageHeight);
    }
 
    /**
     * Check whether a given Easting/Northing coordinate falls within the
     * bounds of the current map region.
     *
     * @param easting  The Ordnance Survey Easting value
     * @param northing The Ordnance Survey Northing value
     * @return true if the point is within the map bounds
     */
    public boolean isOnMap(int easting, int northing)
    {
        return isInHorizontalBounds(easting) && isInVerticalBounds(northing);
    }
 
    /**
     * Return the pixel width of the map image this mapper was created for.
     *
     * @return The image width in pixels
     */
    public int getImageWidth()
    {
        return imageWidth;
    }
 
    /**
     * Return the pixel height of the map image this mapper was created for.
     *
     * @return The image height in pixels
     */
    public int getImageHeight()
    {
        return imageHeight;
    }
    
    /**
     * Return the current minimum Easting bound.
     * @return The minimum Easting.
     */
    public int getMapMinEasting() { return mapMinEasting; }
    
    /**
     * Return the current maximum Easting bound.
     * @return The maximum Easting.
     */
    public int getMapMaxEasting() { return mapMaxEasting; }
    
    /**
     * Return the current minimum Northing bound.
     * @return The minimum Northing.
     */
    public int getMapMinNorthing() { return mapMinNorthing; }
    
    /**
     * Return the current maximum Northing bound.
     * @return The maximum Northing.
     */
    public int getMapMaxNorthing() { return mapMaxNorthing; }
 
    /**
     * Check whether an Easting value is within the horizontal map bounds.
     *
     * @param easting The Easting value to check
     * @return true if within bounds
     */
    private boolean isInHorizontalBounds(int easting)
    {
        return easting >= mapMinEasting && easting <= mapMaxEasting;
    }
 
    /**
     * Check whether a Northing value is within the vertical map bounds.
     *
     * @param northing The Northing value to check
     * @return true if within bounds
     */
    private boolean isInVerticalBounds(int northing)
    {
        return northing >= mapMinNorthing && northing <= mapMaxNorthing;
    }
}