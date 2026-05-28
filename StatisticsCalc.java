import java.util.*;
/**
 * StatisticsCalc makes statistical calculations on pollution data.
 * It computes averages, maximums, minimums, and yearly trends
 * for any list of DataPoints.
 * 
 * @author Emre Gokdemir
 * @version 1.0
 */
public class StatisticsCalc
{
    /**
     * Calculate the average pollution value from a list of data points.
     * Only data points with valid values (>= 0) are included.
     * 
     * @param dataPoints The list of data points
     * @return The average pollution value, or -1.0 if no valid data points exist
     * @throws IllegalArgumentException if dataPoints is null
     */
    public static double average(List<DataPoint> dataPoints)
    {
        if (dataPoints == null) {
            throw new IllegalArgumentException("dataPoints must not be null");
        }

        List<DataPoint> valid = getValidPoints(dataPoints);
        if (valid.isEmpty()) {
            return -1.0;
        }

        double sum = 0.0;
        for (DataPoint dp : valid) {
            sum += dp.value();
        }
        return sum / valid.size();
    }

    /**
     * Find the maximum pollution value from a list of data points.
     * Only data points with valid values (>= 0) are considered.
     * 
     * @param dataPoints The list of data points
     * @return The maximum pollution value, or -1.0 if no valid data points exist
     * @throws IllegalArgumentException if dataPoints is null
     */
    public static double max(List<DataPoint> dataPoints)
    {
        if (dataPoints == null) {
            throw new IllegalArgumentException("dataPoints must not be null");
        }

        List<DataPoint> valid = getValidPoints(dataPoints);
        if (valid.isEmpty()) {
            return -1.0;
        }

        double maxVal = valid.get(0).value();
        for (int i = 1; i < valid.size(); i++) {
            if (valid.get(i).value() > maxVal) {
                maxVal = valid.get(i).value();
            }
        }
        return maxVal;
    }

    /**
     * Find the minimum pollution value from a list of data points.
     * Only data points with valid values (>= 0) are considered.
     * 
     * @param dataPoints The list of data points
     * @return The minimum pollution value, or -1.0 if no valid data points exist
     * @throws IllegalArgumentException if dataPoints is null
     */
    public static double min(List<DataPoint> dataPoints)
    {
        if (dataPoints == null) {
            throw new IllegalArgumentException("dataPoints must not be null");
        }

        List<DataPoint> valid = getValidPoints(dataPoints);
        if (valid.isEmpty()) {
            return -1.0;
        }

        double minVal = valid.get(0).value();
        for (int i = 1; i < valid.size(); i++) {
            if (valid.get(i).value() < minVal) {
                minVal = valid.get(i).value();
            }
        }
        return minVal;
    }

    /**
     * Find the data point with the highest pollution value.
     * Only data points with valid values (>= 0) are considered.
     * 
     * @param dataPoints The list of data points
     * @return The DataPoint with the highest value, or null if no valid points
     * @throws IllegalArgumentException if dataPoints is null
     */
    public static DataPoint maxDataPoint(List<DataPoint> dataPoints)
    {
        if (dataPoints == null) {
            throw new IllegalArgumentException("dataPoints must not be null");
        }

        List<DataPoint> valid = getValidPoints(dataPoints);
        if (valid.isEmpty()) {
            return null;
        }

        DataPoint maxPoint = valid.get(0);
        for (int i = 1; i < valid.size(); i++) {
            if (valid.get(i).value() > maxPoint.value()) {
                maxPoint = valid.get(i);
            }
        }
        return maxPoint;
    }

    /**
     * Find the data point with the lowest pollution value.
     * Only data points with valid values (>= 0) are considered.
     * 
     * @param dataPoints The list of data points
     * @return The DataPoint with the lowest value, or null if no valid points
     * @throws IllegalArgumentException if dataPoints is null
     */
    public static DataPoint minDataPoint(List<DataPoint> dataPoints)
    {
        if (dataPoints == null) {
            throw new IllegalArgumentException("dataPoints must not be null");
        }

        List<DataPoint> valid = getValidPoints(dataPoints);
        if (valid.isEmpty()) {
            return null;
        }

        DataPoint minPoint = valid.get(0);
        for (int i = 1; i < valid.size(); i++) {
            if (valid.get(i).value() < minPoint.value()) {
                minPoint = valid.get(i);
            }
        }
        return minPoint;
    }
    
    /**
     * Count the number of valid data points (for value >= 0).
     * 
     * @param dataPoints The list of data points
     * @return The number of valid data points
     * @throws IllegalArgumentException if dataPoints is null
     */
    public static int countValid(List<DataPoint> dataPoints)
    {
        if (dataPoints == null) {
            throw new IllegalArgumentException("dataPoints must not be null");
        }

        return getValidPoints(dataPoints).size();
    }

    /**
     * Calculate the yearly average trend for a pollutant across multiple years.
     * Takes a map of year to list of data points and returns a map of
     * year to average value.
     * 
     * @param yearlyData A map from year string to list of DataPoints for that year
     * @return A map from year string to the average pollution value for that year
     * @throws IllegalArgumentException if yearlyData is null
     */
    public static Map<String, Double> yearlyAverageTrend(Map<String, List<DataPoint>> yearlyData)
    {
        if (yearlyData == null) {
            throw new IllegalArgumentException("yearlyData must not be null");
        }

        HashMap<String, Double> trend = new HashMap<>();
        for (String year : yearlyData.keySet()) {
            trend.put(year, average(yearlyData.get(year)));
        }
        return trend;
    }

    /**
     * Calculate the yearly maximum trend for a pollutant across multiple years.
     * Takes a map of year to list of data points and returns a map of
     * year to max value.
     * 
     * @param yearlyData A map from year string to list of DataPoints for that year
     * @return A map from year string to the max pollution value for that year
     * @throws IllegalArgumentException if yearlyData is null
     */
    public static Map<String, Double> yearlyMaxTrend(Map<String, List<DataPoint>> yearlyData)
    {
        if (yearlyData == null) {
            throw new IllegalArgumentException("yearlyData must not be null");
        }

        HashMap<String, Double> trend = new HashMap<>();
        for (String year : yearlyData.keySet()) {
            trend.put(year, max(yearlyData.get(year)));
        }
        return trend;
    }

    /**
     * Calculate the yearly minimum trend for a pollutant across multiple years.
     * Takes a map of year to list of data points and returns a map of
     * year to min value.
     * 
     * @param yearlyData A map from year string to list of DataPoints for that year
     * @return A map from year string to the min pollution value for that year
     * @throws IllegalArgumentException if yearlyData is null
     */
    public static Map<String, Double> yearlyMinTrend(Map<String, List<DataPoint>> yearlyData)
    {
        if (yearlyData == null) {
            throw new IllegalArgumentException("yearlyData must not be null");
        }

        HashMap<String, Double> trend = new HashMap<>();
        for (String year : yearlyData.keySet()) {
            trend.put(year, min(yearlyData.get(year)));
        }
        return trend;
    }
    
    /**
     * Calculate the standard deviation of pollution values from a list of data points.
     * Only valid data points (value >= 0) are included.
     * 
     * @param dataPoints The list of data points
     * @return The standard deviation, or -1.0 if fewer than 2 valid points exist
     * @throws IllegalArgumentException if dataPoints is null
     */
    public static double standardDeviation(List<DataPoint> dataPoints)
    {
        if (dataPoints == null) {
            throw new IllegalArgumentException("dataPoints must not be null");
        }

        List<DataPoint> valid = getValidPoints(dataPoints);
        if (valid.size() < 2) {
            return -1.0;
        }

        double avg = average(valid);
        double sumSquaredDiffs = 0.0;
        for (DataPoint dp : valid) {
            double diff = dp.value() - avg;
            sumSquaredDiffs += diff * diff;
        }
        return Math.sqrt(sumSquaredDiffs / valid.size());
    }

    /**
     * Calculate the median pollution value from a list of data points.
     * Only valid data points (value >= 0) are included.
     * 
     * @param dataPoints The list of data points
     * @return The median value, or -1.0 if no valid data points exist
     * @throws IllegalArgumentException if dataPoints is null
     */
    public static double median(List<DataPoint> dataPoints)
    {
        if (dataPoints == null) {
            throw new IllegalArgumentException("dataPoints must not be null");
        }

        List<DataPoint> valid = getValidPoints(dataPoints);
        if (valid.isEmpty()) {
            return -1.0;
        }

        // Extract values and sort them
        ArrayList<Double> values = new ArrayList<>();
        for (DataPoint dp : valid) {
            values.add(dp.value());
        }
        Collections.sort(values);

        int size = values.size();
        if (size % 2 == 0) {
            return (values.get(size / 2 - 1) + values.get(size / 2)) / 2.0;
        } else {
            return values.get(size / 2);
        }
    }
    
    /**
     * Filter the list to data points with valid values.
     * The DataLoader stores -1.0 for missing/invalid values, so this method
     * excludes those.
     * 
     * @param dataPoints The list of data points
     * @return A list containing only data points with value >= 0
     */
    private static List<DataPoint> getValidPoints(List<DataPoint> dataPoints)
    {
        ArrayList<DataPoint> valid = new ArrayList<>();
        for (DataPoint dp : dataPoints) {
            if (dp.value() >= 0) {
                valid.add(dp);
            }
        }
        return valid;
    }
}
