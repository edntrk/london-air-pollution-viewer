import java.util.*;
/**
 * The DataManager is responsible for loading all pollution data CSV files
 * at application startup and providing access to datasets by pollutant and year.
 * 
 * It stores 18 datasets in a map, the keys of the map are strings of the format "pollutant-year" (e.g. "no2-2023").
 * 
 * @author Emre Gokdemir
 * @version 1.0
 */
public class DataManager
{
    private HashMap<String, DataSet> datasets;
    private static final String[] POLLUTANTS = {"no2", "pm10", "pm2.5"};
    private static final String[] YEARS = {"2018", "2019", "2020", "2021", "2022", "2023"};
    private static final String BASE_PATH = "UKAirPollutionData/";

    /**
     * Constructor. Loads all 18 data files immediately.
     */
    public DataManager()
    {
        datasets = new HashMap<>();
        loadAllFiles();
    }

    /**
     * Load all pollution data files for every pollutant and year combination.
     */
    private void loadAllFiles()
    {
        DataLoader loader = new DataLoader();

        for (String year : YEARS) {
            for (String pollutant : POLLUTANTS) {
                String filePath = buildFilePath(pollutant, year);
                DataSet dataSet = loader.loadDataFile(filePath);
                if (dataSet != null) {
                    String key = makeKey(pollutant, year);
                    datasets.put(key, dataSet);
                }
            }
        }

        System.out.println("DataManager: Loaded " + datasets.size() + " datasets.");
    }

    /**
     * Build the file path for a given pollutant and year.
     * 
     * @param pollutant The pollutant type ("no2", "pm10", or "pm2.5")
     * @param year      The year as a string (e.g. "2023")
     * @return The relative file path to the CSV file
     */
    private String buildFilePath(String pollutant, String year)
    {
        if (pollutant.equals("no2")) {
            return BASE_PATH + "NO2/mapno2" + year + ".csv";
        } else if (pollutant.equals("pm10")) {
            return BASE_PATH + "pm10/mappm10" + year + "g.csv";
        } else if (pollutant.equals("pm2.5")) {
            return BASE_PATH + "pm2.5/mappm25" + year + "g.csv";
        } else {
            throw new IllegalArgumentException("Unknown pollutant: " + pollutant);
        }
    }

    /**
     * Create a lookup key from a pollutant name and year.
     * e.g. "no2-2023".
     * 
     * @param pollutant The pollutant type
     * @param year      The year
     * @return A combined key string
     */
    private String makeKey(String pollutant, String year)
    {
        return pollutant.toLowerCase() + "-" + year;
    }

    /**
     * Retrieve a dataset for a specific pollutant and year.
     * 
     * @param pollutant The pollutant type ("no2", "pm10", or "pm2.5")
     * @param year      The year as a string (e.g. "2023")
     * @return The DataSet for the given pollutant and year, or null if not found
     */
    public DataSet getDataSet(String pollutant, String year)
    {
        return datasets.get(makeKey(pollutant, year));
    }

    /**
     * Retrieve all datasets for a specific pollutant across all years.
     * The returned map is keyed by year.
     * 
     * @param pollutant The pollutant type ("no2", "pm10", or "pm2.5")
     * @return A map from year to DataSet for the given pollutant
     */
    public Map<String, DataSet> getDataSetsForPollutant(String pollutant)
    {
        HashMap<String, DataSet> result = new HashMap<>();
        for (String year : YEARS) {
            DataSet ds = getDataSet(pollutant, year);
            if (ds != null) {
                result.put(year, ds);
            }
        }
        return result;
    }

    /**
     * Return the list of available pollutant names.
     * @return An array of pollutant name strings
     */
    public String[] getPollutants()
    {
        return POLLUTANTS.clone();
    }

    /**
     * Return the list of available years.
     * @return An array of year strings
     */
    public String[] getYears()
    {
        return YEARS.clone();
    }

    /**
     * Check whether a dataset exists for the given pollutant and year.
     * 
     * @param pollutant The pollutant type
     * @param year      The year
     * @return true if the dataset was loaded successfully
     */
    public boolean hasDataSet(String pollutant, String year)
    {
        return datasets.containsKey(makeKey(pollutant, year));
    }

    /**
     * Return the total number of datasets that were successfully loaded.
     * @return The number of loaded datasets
     */
    public int getDataSetCount()
    {
        return datasets.size();
    }
}
