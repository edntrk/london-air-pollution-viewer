import java.util.*;

/**
 * PollutionFilter provides methods to filter pollution data points based on
 * pollutant type, year, geographic map bounds, and pollution threshold.
 * 
 * Supports multiple UK regions: London, Manchester, Birmingham, Leeds, Bristol.
 * Each region has its own Easting/Northing bounds centred on the city.
 * All regions cover roughly 43km x 25km to match the London map scale.
 * 
 * @author Emre Gokdemir
 * @version 2.0
 */
public class PollutionFilter
{
    // London map corner coordinates (Easting/Northing)
    private static final int LONDON_MIN_X = 510394;
    private static final int LONDON_MAX_X = 553297;
    private static final int LONDON_MIN_Y = 168504;
    private static final int LONDON_MAX_Y = 193305;
    
    // Manchester region bounds (centred on E383819, N398052)
    private static final int MANCHESTER_MIN_X = 362000;
    private static final int MANCHESTER_MAX_X = 405000;
    private static final int MANCHESTER_MIN_Y = 385000;
    private static final int MANCHESTER_MAX_Y = 410000;
    
    // Birmingham region bounds (centred on E406689, N286822)
    private static final int BIRMINGHAM_MIN_X = 385000;
    private static final int BIRMINGHAM_MAX_X = 428000;
    private static final int BIRMINGHAM_MIN_Y = 274000;
    private static final int BIRMINGHAM_MAX_Y = 299000;
    
    // Leeds region bounds (centred on E429719, N433916)
    private static final int LEEDS_MIN_X = 408000;
    private static final int LEEDS_MAX_X = 451000;
    private static final int LEEDS_MIN_Y = 421000;
    private static final int LEEDS_MAX_Y = 446000;
    
    // Bristol region bounds (centred on E358337, N172855)
    private static final int BRISTOL_MIN_X = 337000;
    private static final int BRISTOL_MAX_X = 380000;
    private static final int BRISTOL_MIN_Y = 160000;
    private static final int BRISTOL_MAX_Y = 185000;

    private DataManager dataManager;
    
    // Current region for filtering (defaults to London)
    private String currentRegion = "London";

    /**
     * Constructor for PollutionFilter.
     * 
     * @param dataManager The DataManager providing access to all loaded datasets
     */
    public PollutionFilter(DataManager dataManager)
    {
        this.dataManager = dataManager;
    }
    
    /**
     * Set the current map region for filtering.
     * 
     * @param region The region name (e.g. "London", "Manchester")
     */
    public void setRegion(String region)
    {
        this.currentRegion = region;
    }
    
    /**
     * Get the current map region.
     * 
     * @return The current region name.
     */
    public String getRegion()
    {
        return currentRegion;
    }

    /**
     * Get all data points for a given pollutant and year that fall within
     * the currently selected region's map bounds.
     * 
     * @param pollutant The pollutant type ("no2", "pm10", or "pm2.5")
     * @param year      The year as a string (e.g. "2023")
     * @return A list of DataPoints within the current region, or an empty list
     *         if the dataset is not found
     */
    public List<DataPoint> getFilteredData(String pollutant, String year)
    {
        DataSet dataSet = dataManager.getDataSet(pollutant, year);
        if (dataSet == null) {
            return new ArrayList<>();
        }
        int[] bounds = getRegionBounds(currentRegion);
        return filterByBounds(dataSet.getData(), bounds[0], bounds[1], bounds[2], bounds[3]);
    }
    
    /**
     * Get filtered data with an additional pollution value threshold.
     * Only data points with values at or above the threshold are returned.
     * 
     * @param pollutant The pollutant type
     * @param year The year string
     * @param threshold The minimum pollution value to include
     * @return A list of filtered DataPoints
     */
    public List<DataPoint> getFilteredDataWithThreshold(String pollutant, String year, double threshold)
    {
        List<DataPoint> regionFiltered = getFilteredData(pollutant, year);
        if (threshold <= 0) {
            return regionFiltered;
        }
        return filterByMinValue(regionFiltered, threshold);
    }

    /**
     * Filter a list of data points to only include those within the
     * current region's map bounds.
     * 
     * @param dataPoints The full list of data points
     * @return A filtered list containing only points within the map area
     */
    public List<DataPoint> filterByMapBounds(List<DataPoint> dataPoints)
    {
        int[] bounds = getRegionBounds(currentRegion);
        return filterByBounds(dataPoints, bounds[0], bounds[1], bounds[2], bounds[3]);
    }

    /**
     * Filter a list of data points to only include those within custom
     * coordinates.
     * 
     * @param dataPoints The full list of data points
     * @param minX       (left bound)
     * @param maxX       (right bound)
     * @param minY       (bottom bound)
     * @param maxY       (top bound)
     * @return A filtered list containing only points within the specified coordinate bounds.
     */
    public List<DataPoint> filterByBounds(List<DataPoint> dataPoints,
                                           int minX, int maxX, int minY, int maxY)
    {
        ArrayList<DataPoint> filtered = new ArrayList<>();
        for (DataPoint dp : dataPoints) {
            if (dp.x() >= minX && dp.x() <= maxX 
                && dp.y() >= minY && dp.y() <= maxY) {
                filtered.add(dp);
            }
        }
        return filtered;
    }

    /**
     * Filter data points to only include those with pollution values
     * above a given threshold.
     * 
     * @param dataPoints The list of data points to filter
     * @param threshold  The minimum pollution value to include
     * @return A list of data points with values above the threshold
     */
    public List<DataPoint> filterByMinValue(List<DataPoint> dataPoints, double threshold)
    {
        ArrayList<DataPoint> filtered = new ArrayList<>();
        for (DataPoint dp : dataPoints) {
            if (dp.value() >= threshold) {
                filtered.add(dp);
            }
        }
        return filtered;
    }

    /**
     * Filter data points to only include those with pollution values
     * within a given range.
     * 
     * @param dataPoints The list of data points to filter
     * @param minValue   The minimum pollution value
     * @param maxValue   The maximum pollution value
     * @return A list of data points with values in the specified range
     */
    public List<DataPoint> filterByValueRange(List<DataPoint> dataPoints, double minValue, double maxValue)
    {
        ArrayList<DataPoint> filtered = new ArrayList<>();
        for (DataPoint dp : dataPoints) {
            if (dp.value() >= minValue && dp.value() <= maxValue) {
                filtered.add(dp);
            }
        }
        return filtered;
    }
    
    /**
     * Get filtered data for all years for a given pollutant in the current region.
     * Returns a map from year to the list of filtered data points.
     * 
     * @param pollutant The pollutant type ("no2", "pm10", or "pm2.5")
     * @return A map from year to filtered data points for that year
     */
    public Map<String, List<DataPoint>> getFilteredDataAllYears(String pollutant)
    {
        HashMap<String, List<DataPoint>> result = new HashMap<>();
        for (String year : dataManager.getYears()) {
            List<DataPoint> filtered = getFilteredData(pollutant, year);
            if (!filtered.isEmpty()) {
                result.put(year, filtered);
            }
        }
        return result;
    }
    
    /**
     * Get the Easting/Northing bounds for a named region.
     * Returns an array of [minX, maxX, minY, maxY].
     * 
     * @param region The region name (case-sensitive).
     * @return An int array with the four bounds.
     */
    public int[] getRegionBounds(String region)
    {
        if (region == null) {
            return new int[]{ LONDON_MIN_X, LONDON_MAX_X, LONDON_MIN_Y, LONDON_MAX_Y };
        }
        
        if (region.equals("Manchester")) {
            return new int[]{ MANCHESTER_MIN_X, MANCHESTER_MAX_X, MANCHESTER_MIN_Y, MANCHESTER_MAX_Y };
        } else if (region.equals("Birmingham")) {
            return new int[]{ BIRMINGHAM_MIN_X, BIRMINGHAM_MAX_X, BIRMINGHAM_MIN_Y, BIRMINGHAM_MAX_Y };
        } else if (region.equals("Leeds")) {
            return new int[]{ LEEDS_MIN_X, LEEDS_MAX_X, LEEDS_MIN_Y, LEEDS_MAX_Y };
        } else if (region.equals("Bristol")) {
            return new int[]{ BRISTOL_MIN_X, BRISTOL_MAX_X, BRISTOL_MIN_Y, BRISTOL_MAX_Y };
        } else {
            // Default to London
            return new int[]{ LONDON_MIN_X, LONDON_MAX_X, LONDON_MIN_Y, LONDON_MAX_Y };
        }
    }
    
    /**
     * Get the map image filename for a named region.
     * Each region has its own PNG file: London.png, Manchester.png, etc.
     * 
     * @param region The region name.
     * @return The filename string for the map image.
     */
    public String getMapImageFile(String region)
    {
        if (region == null) {
            return "London.png";
        }
        
        if (region.equals("Manchester")) {
            return "Manchester.png";
        } else if (region.equals("Birmingham")) {
            return "Birmingham.png";
        } else if (region.equals("Leeds")) {
            return "Leeds.png";
        } else if (region.equals("Bristol")) {
            return "Bristol.png";
        } else {
            return "London.png";
        }
    }

    /**
     * Return the London map minimum X (Easting) bound.
     */
    public static int getMapMinX() { return LONDON_MIN_X; }

    /**
     * Return the London map maximum X (Easting) bound.
     */
    public static int getMapMaxX() { return LONDON_MAX_X; }

    /**
     * Return the London map minimum Y (Northing) bound.
     */
    public static int getMapMinY() { return LONDON_MIN_Y; }

    /**
     * Return the London map maximum Y (Northing) bound.
     */
    public static int getMapMaxY() { return LONDON_MAX_Y; }
}
